package com.gaga.auth_server.utils;

import com.gaga.auth_server.dto.response.TokenResponseDTO;
import com.gaga.auth_server.enums.TokenEnum;
import com.gaga.auth_server.exception.UnauthorizedException;
import com.gaga.auth_server.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token);

        if(claims.getBody().getExpiration().before(new Date())) throw new UnauthorizedException();
    }

    public TokenResponseDTO generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        //claims.put("nickname", user.getNickname());
        return createToken(claims, email);
    }

    public TokenResponseDTO createToken(Map<String, Object> claims, String sub) {
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setClaims(claims) // token에 담을 정보
                .setSubject(sub) //token 제목
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS256, ACCESS_SECRET_KEY) //알고리즘, 키
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(sub)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY)
                .compact();

        return new TokenResponseDTO(accessToken, refreshToken);
    }
}
