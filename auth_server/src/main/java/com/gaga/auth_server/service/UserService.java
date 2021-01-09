package com.gaga.auth_server.service;

import com.gaga.auth_server.utils.*;
import com.gaga.auth_server.dto.MailDTO;
import com.gaga.auth_server.dto.request.UserInfoRequestDTO;
import com.gaga.auth_server.dto.request.LoginDTO;
import com.gaga.auth_server.dto.response.*;
import com.gaga.auth_server.enums.TokenEnum;
import com.gaga.auth_server.exception.*;
import com.gaga.auth_server.model.User;
import com.gaga.auth_server.repository.UserInfoRepository;
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
    private ResponseMessage responseMSG;
    private Encryption encryption;
    private UserGrade grade;

    public final int RANDOM_MIN_NUMBER = 10000;
    public final int RANDOM_MAX_NUMBER = 99999;

    @PostConstruct
    protected void init() {
        encryption = new Encryption();
        responseMSG = new ResponseMessage();
        grade = new UserGrade();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    //test용 - 클라요청
    public void removeEmailRecord(String email) { userInfoRepository.deleteByEmail(email); }

    @Transactional
    public void insertUser(UserInfoRequestDTO userInfo) {
        log.info("insertUser start");
        User user = findByEmailOrThrow(userInfo.getEmail().toLowerCase());
        if(user.getGrade() == grade.SIGNUP_COMPLETE) throw new AlreadyExistException(responseMSG.ALREADY_OUR_MEMBER);
        if(user.getGrade() != grade.EMAIL_CHECK_COMPLETE) throw new UnauthorizedException(responseMSG.VERIFY_EMAIL_FIRST);

        user.setNickname(userInfo.getNickname());
        user.setPassword(encryption.encode(userInfo.getPassword()));
        user.setSalt(encryption.getSalt());
        user.setCreatedAt(new Date());
        user.setUpdateAt(new Date());
        user.setGrade(grade.SIGNUP_COMPLETE);
        userInfoRepository.save(user);
        log.info("insertUser end");
    }

    public TokenDTO getUserToken(LoginDTO loginDTO) {
        String userEmail = loginDTO.getEmail().toLowerCase();

        User user = findByEmailOrThrow(userEmail);
        if (!encryption.encodeWithSalt(loginDTO.getPassword(), user.getSalt()).equals(user.getPassword()))
            throw new NotFoundException(responseMSG.NOT_CORRECT_PW);

        TokenDTO responseDTO = jwtUtils.generateToken(userEmail);
        String refreshToken = responseDTO.getRefreshToken();

        try {
            redisTemplate.opsForValue().set(refreshToken, userEmail);
            redisTemplate.expire(refreshToken, 7, TimeUnit.DAYS);
        } catch (RedisException e) {
            //##try-catch문은 처리!
            //## 만약, redis에 refreshToken이 안들어가면 어떤 처리를 할 것인가? 다른 곳에 저장할 것인가?
            throw new UnauthorizedException(responseMSG.INTERNAL_SERVER_ERROR);
        }
        log.info("redisTemplate end");

        user.setLoginAt(new Date());
        userInfoRepository.save(user);
        return responseDTO;
    }

    public TokenDTO getReissueToken(String refreshToken) {
        jwtUtils.isValidateToken(refreshToken, TokenEnum.REFRESH);

        Object object = redisTemplate.opsForValue().get(refreshToken);
        if (object == null) throw new UnauthorizedException(responseMSG.EXPIRED_TOKEN);

        String email = object.toString();
        TokenDTO tokenDTO = jwtUtils.generateToken(email);

        redisTemplate.delete(refreshToken);
        redisTemplate.opsForValue().set(tokenDTO.getRefreshToken(), email);
        redisTemplate.expire(tokenDTO.getRefreshToken(), 7, TimeUnit.DAYS);

        return tokenDTO;
    }

    public void findPassword(String email) {
        User user = findByEmailOrThrow(email);
        String tempPW = randomString();

        insertPassword(user, tempPW);
        sendMail(user.getEmail(), responseMSG.TEMP_PW, tempPW);
    }

    public void changePassword(String email, String pw) {
        User user = findByEmailOrThrow(email);
        insertPassword(user, pw);
    }

    public void leaveUser(String email) {
        User user = findByEmailOrThrow(email);
        user.setGrade((byte) 9);
        userInfoRepository.save(user);
    }

    public void sendEmail(String email) {
        if(userInfoRepository.existsByEmail(email))
            throw new AlreadyExistException(responseMSG.ALREADY_USED_EMAIL);

        int randomCode = (int) Math.floor(Math.random() * RANDOM_MAX_NUMBER) + RANDOM_MIN_NUMBER;
        String message = responseMSG.SEND_EMAIL_CONTENT + randomCode + responseMSG.SEND_LAST_CONTENT;
        log.info("randomCode : "+ randomCode);

        log.info("checkSendEmail redis start");
        try {
            redisTemplate.opsForValue().set(email, Integer.toString(randomCode));
            redisTemplate.expire(email, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            //throw new MailException(responseMSG.SEND_FAIL_EMAIL);
            log.error(e.toString());
        }
        log.info("checkSendEmail redis end");

        sendMail(email, responseMSG.SEND_CERTIFICATION, message);
    }

    public void checkEmailCode(String email, String code) {
        Object object = redisTemplate.opsForValue().get(email);

        if (object == null || !code.equals(object.toString()))
            throw new NotFoundException(responseMSG.NOT_FOUND_CODE);

        redisTemplate.delete(email);
        User user = User.builder()
                .email(email)
                .grade(grade.EMAIL_CHECK_COMPLETE)
                .build();
        userInfoRepository.save(user);
    }

    @Transactional
    public void insertPassword(User user, String pw) {
        user.setPassword(encryption.encode(pw));
        user.setSalt(encryption.getSalt());
        userInfoRepository.save(user);
    }

    public void checkNickname(String nickname) {
        if (userInfoRepository.existsByNickname(nickname))
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

    public String randomString() {
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            switch (i % 3) {
                case 0:
                    sb.append((char) ((rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    sb.append((char) ((rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    sb.append((rnd.nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }
}
