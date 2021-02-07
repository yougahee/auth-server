package com.gaga.auth_server.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gaga.auth_server.dto.UserDTO;
import com.gaga.auth_server.dto.response.TokenDTO;
import com.gaga.auth_server.enums.TokenEnum;
import com.gaga.auth_server.exception.NotFoundException;
import com.gaga.auth_server.exception.SignatureVerificationException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret.at}")
    private String ACCESS_SECRET_KEY;

    @Value("${jwt.secret.rt}")
    private String REFRESH_SECRET_KEY;

    private final static String CLAIM_NICKNAME = "nickname";
    private final static String USER_IDX = "user_idx";

    //accessToken -> 1hour
    private static final long ACCESS_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60;
    //refreshToken -> 1week
    private static final long REFRESH_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60 * 24 * 7;

    @PostConstruct
    protected void init() {
        ACCESS_SECRET_KEY = Base64.getEncoder().encodeToString(ACCESS_SECRET_KEY.getBytes());
        REFRESH_SECRET_KEY = Base64.getEncoder().encodeToString(REFRESH_SECRET_KEY.getBytes());
    }

    public void isValidateToken(String token, TokenEnum access) {

        String key = ACCESS_SECRET_KEY;
        if(access == TokenEnum.REFRESH) key = REFRESH_SECRET_KEY;

        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
        } catch (TokenExpiredException te) {
            log.error(te.getMessage());
            throw new TokenExpiredException(ResponseMessage.EXPIRED_TOKEN);
        } catch (SignatureVerificationException sve) {
            log.error(sve.getMessage());
            throw new SignatureVerificationException(ResponseMessage.SIGNATURE_VERIFICATION_TOKEN);
        } catch (JWTDecodeException jde) {
            log.error(jde.getMessage());
            throw new JWTDecodeException(ResponseMessage.JWT_DECODE_TOKEN);
        } catch (JwtException  e) {
            log.error(e.getMessage());
            throw new JwtException(ResponseMessage.JWT_EXCEPTION);
        }
    }

    public TokenDTO generateToken(UserDTO user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_NICKNAME, user.getNickname());
        claims.put(USER_IDX, user.getUserIdx());
        return createToken(claims, user.getEmail());
    }

    public TokenDTO createToken(Map<String, Object> claims, String sub) {
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(sub)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS256, ACCESS_SECRET_KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(sub)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY)
                .compact();

        return new TokenDTO(accessToken, refreshToken);
    }

    public UserDTO decodeJWT(String token) {
        if(token == null || token.equals("")) throw new NotFoundException(ResponseMessage.NO_TOKEN);
        UserDTO userDTO = new UserDTO();
        DecodedJWT jwt = JWT.decode(token);
        userDTO.setEmail(jwt.getSubject());
        userDTO.setNickname(jwt.getClaim(CLAIM_NICKNAME).asString());
        userDTO.setUserIdx(jwt.getClaim(USER_IDX).asLong());
        return userDTO;
    }
}
