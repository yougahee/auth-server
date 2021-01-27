package com.gaga.auth_server.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gaga.auth_server.dto.UserDTO;
import com.gaga.auth_server.dto.request.UserInfoRequestDTO;
import com.gaga.auth_server.dto.response.TokenDTO;
import com.gaga.auth_server.enums.TokenEnum;
import com.gaga.auth_server.exception.NotFoundException;
import com.gaga.auth_server.exception.NotTokenException;
import com.gaga.auth_server.exception.SignatureVerificationException;
import com.gaga.auth_server.exception.UnauthorizedException;
import com.gaga.auth_server.model.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
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

    private final String CLAIM_NICKNAME = "nickname";
    private final String USER_IDX = "user_idx";

    //accessToken -> 1hour
    private static final long ACCESS_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60;
    //refreshToken -> 1week
    private static final long REFRESH_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60 * 24 * 7;

    @PostConstruct
    protected void init() {
        ACCESS_SECRET_KEY = Base64.getEncoder().encodeToString(ACCESS_SECRET_KEY.getBytes());
        REFRESH_SECRET_KEY = Base64.getEncoder().encodeToString(REFRESH_SECRET_KEY.getBytes());
    }

    public void isValidateToken(String token, TokenEnum access) throws JwtException{

        String key = ACCESS_SECRET_KEY;
        if(access == TokenEnum.REFRESH) key = REFRESH_SECRET_KEY;

        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);

            log.info("토큰 validate");
        } catch (TokenExpiredException te) {
            log.error(te.getMessage());
            throw new TokenExpiredException("토큰이 만료되었습니다.");
        } catch (SignatureVerificationException sve) {
            log.error(sve.getMessage());
            throw new SignatureVerificationException("토큰이 변조되었습니다.");
        } catch (JWTDecodeException jde) {
            log.error(jde.getMessage());
            throw new JWTDecodeException("토큰의 유형이 아닙니다.");
        } catch (JwtException  e) {
            log.error(e.getMessage());
            throw new JwtException("Jwt Exception");
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

        return new TokenDTO(accessToken, refreshToken);
    }

    public UserDTO decodeJWT(String token) {
        if(token == null || token.equals("")) throw new NotFoundException("token이 없습니다.");
        UserDTO userDTO = new UserDTO();
        DecodedJWT jwt = JWT.decode(token);
        userDTO.setEmail(jwt.getSubject());
        userDTO.setNickname(jwt.getClaim(CLAIM_NICKNAME).asString());
        userDTO.setUserIdx(jwt.getClaim(USER_IDX).asLong());
        return userDTO;
    }

    public static void main(String[] args) {

        //isValidateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhaHJmdXMzNEBnbWFpbC5jb20iLCJuaWNrbmFtZSI6ImJhYmEiLCJleHAiOjE2MTIzNDY4NDMsImlhdCI6MTYxMTc0MjA0M30.gY1Pnw5lORzXeR-Hsc6YdMCBgAYVCHsL3BW5o66b_zs"
        //, TokenEnum.REFRESH);
        System.out.println(JWT.decode("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhaHJmdXMzNEBnbWFpbC5jb20iLCJleHAiOjE2MTAxMjAzMzIsImlhdCI6MTYxMDExNjczMn0.eswD_DvXp6ySkf3XnpPPKiUHuuZEeAA5z-IW-td5FvY").getSubject());
    }
}
