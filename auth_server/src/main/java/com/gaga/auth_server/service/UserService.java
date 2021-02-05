package com.gaga.auth_server.service;

import com.gaga.auth_server.dto.UserDTO;
import com.gaga.auth_server.utils.*;
import com.gaga.auth_server.utils.mail.MailDTO;
import com.gaga.auth_server.dto.request.UserInfoRequestDTO;
import com.gaga.auth_server.dto.request.LoginDTO;
import com.gaga.auth_server.dto.response.*;
import com.gaga.auth_server.enums.TokenEnum;
import com.gaga.auth_server.exception.*;
import com.gaga.auth_server.model.User;
import com.gaga.auth_server.repository.UserInfoRepository;
import com.gaga.auth_server.utils.mail.CustomMailSender;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserInfoRepository userInfoRepository;
    private final StringRedisTemplate redisTemplate;
    private final CustomMailSender customMailSender;
    private final JwtUtils jwtUtils;
    private final RandomCode randomCode;
    private final Encryption encryption;
    private ResponseMessage responseMSG;
    private UserGrade grade;

    @PostConstruct
    protected void init() {
        responseMSG = new ResponseMessage();
        grade = new UserGrade();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    public void removeEmailRecord(String email) {
        User user = findByEmailOrThrow(email);
        userInfoRepository.delete(user);
    }

    //int -> void로 바꿔야함. -> 테스트를 위해
    public int sendEmail(String email) {
        User user = findByEmailOrThrow(email);
        if (Boolean.TRUE.equals(userInfoRepository.existsByEmail(email))) {
            if(user.getGrade() == grade.EMAIL_CHECK_COMPLETE)
                throw new AlreadyCheckedException(responseMSG.ALREADY_CHECKED_EMAIL);
            else if(user.getGrade() != grade.BASIC_GRADE)
                throw new AlreadyExistException(responseMSG.ALREADY_USED_EMAIL);
        }

        if (Boolean.TRUE.equals(redisTemplate.hasKey(email))) redisTemplate.delete(email);

        int authorizationCode = randomCode.randomEmailCode();
        String message = responseMSG.SEND_EMAIL_CONTENT + authorizationCode + responseMSG.SEND_LAST_CONTENT;

        log.info("checkSendEmail redis start");
        redisTemplate.opsForValue().set(email, Integer.toString(authorizationCode));
        redisTemplate.expire(email, 10, TimeUnit.MINUTES);
        log.info("checkSendEmail redis end");
        sendMail(email, responseMSG.SEND_CERTIFICATION, message);
        return authorizationCode;
    }

    public void checkEmailCode(String email, String code) {
        Object object = redisTemplate.opsForValue().get(email);

        if (object == null || !code.equals(object.toString()))
            throw new NotFoundException(responseMSG.NOT_FOUND_CODE);

        redisTemplate.delete(email);
        User user = User.builder()
                .email(email)
                .grade(grade.EMAIL_CHECK_COMPLETE)
                .point((long) 50000)
                .build();
        userInfoRepository.save(user);
    }

    @Transactional
    public void insertUser(UserInfoRequestDTO userInfo) {
        User user = findByEmailOrThrow(userInfo.getEmail().toLowerCase());
        if (user.getGrade() == grade.MEMBER) throw new AlreadyExistException(responseMSG.ALREADY_OUR_MEMBER);
        if (user.getGrade() != grade.EMAIL_CHECK_COMPLETE)
            throw new UnauthorizedException(responseMSG.VERIFY_EMAIL_FIRST);
        user.setNickname(userInfo.getNickname());
        user.setPassword(encryption.encode(userInfo.getPassword()));
        user.setSalt(encryption.getSalt());
        user.setGrade(grade.MEMBER);
        userInfoRepository.save(user);
    }

    public TokenDTO getUserToken(LoginDTO loginDTO) {
        String userEmail = loginDTO.getEmail().toLowerCase();
        User user = findByEmailOrThrow(userEmail);

        if(user.getGrade() != grade.MEMBER || user.getGrade() != grade.MANAGER)
            throw new NotFoundException(responseMSG.SIGN_UP_FIRST);

        if (!encryption.encodeWithSalt(loginDTO.getPassword(), user.getSalt()).equals(user.getPassword()))
            throw new NotFoundException(responseMSG.NOT_CORRECT_PW);

        TokenDTO responseDTO = jwtUtils.generateToken(UserDTO.fromEntity(user));
        String refreshToken = responseDTO.getRefreshToken();

        try {
            redisTemplate.opsForValue().set(refreshToken, userEmail);
            redisTemplate.expire(refreshToken, 7, TimeUnit.DAYS);
        } catch (RedisException e) {
            //## Redis에 저장하지 못했을 때, 다른 곳에 저장할 수 있는 방법을 찾아보자!
            throw new RedisException(responseMSG.Redis_FAIL);
        }

        user.setLoginDate(new Date());
        userInfoRepository.save(user);
        return responseDTO;
    }

    public TokenDTO getReissueToken(String refreshToken) {
        jwtUtils.isValidateToken(refreshToken, TokenEnum.REFRESH);

        if (Boolean.FALSE.equals(redisTemplate.hasKey(refreshToken)))
            throw new UnauthorizedException(responseMSG.EXPIRED_TOKEN);

        UserDTO userDTO = jwtUtils.decodeJWT(refreshToken);
        TokenDTO tokenDTO = jwtUtils.generateToken(userDTO);
        log.info(userDTO.getNickname());

        redisTemplate.delete(refreshToken);
        redisTemplate.opsForValue().set(tokenDTO.getRefreshToken(), userDTO.getEmail());
        redisTemplate.expire(tokenDTO.getRefreshToken(), 7, TimeUnit.DAYS);
        return tokenDTO;
    }

    public void findPassword(String email) {
        User user = findByEmailOrThrow(email);
        String tempPW = randomCode.randomString();

        sendMail(user.getEmail(), responseMSG.TEMP_PW, tempPW);
        insertPassword(user, tempPW);
    }

    public void changePassword(String email, String oldPW, String pw) {
        User user = findByEmailOrThrow(email);

        if (!encryption.encodeWithSalt(user.getPassword(), user.getSalt()).equals(oldPW))
            throw new NotFoundException(responseMSG.NOT_CORRECT_PW);

        insertPassword(user, pw);
    }

    public void leaveUser(String email) {
        User user = findByEmailOrThrow(email);
        user.setGrade(grade.LEAVE_SERVICE);
        userInfoRepository.save(user);
    }

    @Transactional
    public void insertPassword(User user, String pw) {
        user.setPassword(encryption.encode(pw));
        user.setSalt(encryption.getSalt());
        userInfoRepository.save(user);
    }

    public void checkNickname(String nickname) {
        if (Boolean.TRUE.equals(userInfoRepository.existsByNickname(nickname)))
            throw new AlreadyExistException(responseMSG.ALREADY_USED_NICKNAME);
    }

    public User findByEmailOrThrow(String email) {
        return userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new NoExistEmailException(responseMSG.NOT_FOUND_EMAIL));
    }

    public void sendMail(String sendEmail, String title, String sendMessage) {
        MailDTO mailDTO = new MailDTO(title, sendEmail, sendMessage);
        customMailSender.sendMail(mailDTO);
    }

    public void changeNickname(String email, String nickname) {
        checkNickname(nickname);
        User user = findByEmailOrThrow(email);
        user.setNickname(nickname);
        userInfoRepository.save(user);
    }
}
