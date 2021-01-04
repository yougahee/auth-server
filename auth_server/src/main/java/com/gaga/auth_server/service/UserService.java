package com.gaga.auth_server.service;

import com.gaga.auth_server.algorithm.Encryption;
import com.gaga.auth_server.dto.MailDTO;
import com.gaga.auth_server.dto.request.UserInfoRequestDTO;
import com.gaga.auth_server.dto.request.LoginDTO;
import com.gaga.auth_server.dto.response.*;
import com.gaga.auth_server.enums.TokenEnum;
import com.gaga.auth_server.exception.NoExistEmailException;
import com.gaga.auth_server.exception.NotFoundException;
import com.gaga.auth_server.exception.UnauthorizedException;
import com.gaga.auth_server.model.User;
import com.gaga.auth_server.repository.UserInfoRepository;
import com.gaga.auth_server.utils.CustomMailSender;
import com.gaga.auth_server.utils.JwtUtils;
import com.gaga.auth_server.utils.ResponseMessage;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    public final int RANDOM_MIN_NUMBER = 10000;
    public final int RANDOM_MAX_NUMBER = 99999;

    @PostConstruct
    protected void init() {
        encryption = new Encryption();
        responseMSG = new ResponseMessage();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    public String insertUser(UserInfoRequestDTO userInfo) {
        User user = findByEmail(userInfo.getEmail().toLowerCase());
        user.setNickname(userInfo.getNickname());
        user.setPassword(encryption.encode(userInfo.getPassword()));
        user.setSalt(encryption.getSalt());
        user.setCreatedAt(new Date());
        userInfoRepository.save(user);

        return responseMSG.SING_UP_SUCCESS;
    }

    public LoginTokenResponseDTO getUserToken(LoginDTO loginDTO) {
        String userEmail = loginDTO.getEmail().toLowerCase();
        String userPW = loginDTO.getPassword();

        if (!BCrypt.checkpw(userPW, encryption.encode(userPW)))
            throw new NotFoundException(responseMSG.NOT_CORRECT_PW);

        User user = findByEmail(userEmail);
        TokenResponseDTO responseDTO = jwtUtils.generateToken(userEmail);
        String accessToken = responseDTO.getAccessToken();
        String refreshToken = responseDTO.getRefreshToken();

        try {
            redisTemplate.opsForValue().set(refreshToken, userEmail);
            redisTemplate.expire(refreshToken, 7, TimeUnit.DAYS);
        } catch (RedisException e) {
            //##try-catch문은 처리!
            //## 만약, redis에 refreshToken이 안들어가면 어떤 처리를 할 것인가? 다른 곳에 저장할 것인가?
            throw new UnauthorizedException("");
        }
        log.info("redisTemplate start");
        user.setLoginAt(new Date());
        userInfoRepository.save(user);
        return new LoginTokenResponseDTO(accessToken, refreshToken);
    }

    public TokenResponseDTO getReissueToken(String refreshToken) {
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
        jwtUtils.isValidateToken(refreshToken, TokenEnum.REFRESH);

        Object object = redisTemplate.opsForValue().get(refreshToken);
        if (object != null) {
            String email = object.toString();
            tokenResponseDTO = jwtUtils.generateToken(email);

            redisTemplate.delete(refreshToken);
            redisTemplate.opsForValue().set(tokenResponseDTO.getRefreshToken(), email);
            redisTemplate.expire(tokenResponseDTO.getRefreshToken(), 7, TimeUnit.DAYS);
        }

        return tokenResponseDTO;
    }

    public DefaultResponseDTO findPassword(String email) {
        User user = findByEmail(email);
        String tempPW = randomString();
        String encodeTempPW = encryption.encode(tempPW);
        user.setPassword(encodeTempPW);
        user.setSalt(encryption.getSalt());
        userInfoRepository.save(user);

        isSendMailSuccess(user.getEmail(), responseMSG.TEMP_PW, tempPW);
        return new DefaultResponseDTO(responseMSG.SEND_EMAIL);
    }

    public DefaultResponseDTO checkNickname(String nickname) {
        if (userInfoRepository.existsByNickname(nickname))
            return new DefaultResponseDTO(400, false, responseMSG.ALREADY_USED_NICKNAME);
        else
            return new DefaultResponseDTO(responseMSG.CAN_USE_NICKNAME);
    }

    public DefaultResponseDTO sendEmail(String email) {
        if (checkEmail(email)) throw new NoExistEmailException(responseMSG.ALREADY_USED_EMAIL);
        int randomCode = (int) Math.floor(Math.random() * RANDOM_MAX_NUMBER) + RANDOM_MIN_NUMBER;
        String message = responseMSG.SEND_EMAIL_CONTENT + randomCode + responseMSG.SEND_LAST_CONTENT;

        log.info("checkSendEmail redis start");
        try {
            redisTemplate.opsForValue().set(email, Integer.toString(randomCode));
            redisTemplate.expire(email, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error(e.toString());
            return new DefaultResponseDTO(responseMSG.SEND_FAIL_EMAIL);
        }
        log.info("checkSendEmail redis end");

        isSendMailSuccess(email, responseMSG.SEND_CERTIFICATION, message);
        return new DefaultResponseDTO(responseMSG.SEND_EMAIL);
    }

    public DefaultResponseDTO checkEmailCode(String email, String code) {
        email = email.toLowerCase();
        Object object = redisTemplate.opsForValue().get(email);

        if (code.equals(object.toString())) {
            redisTemplate.delete(email);
            User user = new User();
            user.setEmail(email);
            userInfoRepository.save(user);

            return new DefaultResponseDTO(responseMSG.CERTIFICATION);
        }

        return new DefaultResponseDTO(responseMSG.NOT_FOUND_CODE);
    }

    public boolean checkEmail(String email) {
        return userInfoRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new NoExistEmailException(responseMSG.NOT_FOUND_EMAIL));
    }

    public void isSendMailSuccess(String sendEmail, String title, String sendMessage) {
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
