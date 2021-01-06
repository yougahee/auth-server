package com.gaga.apigateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class PreFilter extends ZuulFilter {

    @Value("${jwt.secret.at}")
    private String KEY;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        log.info("pre filter");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String authorizationHeader = request.getHeader("token");
        if(authorizationHeader == null) return null;

        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        String message = validateToken(authorizationHeader);
        log.info("message : " + message);
        if(message != null) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
            ctx.setResponseBody(message);
        }
        return null;
    }

    public String validateToken(String tokenHeader) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(KEY)).build();
            jwtVerifier.verify(tokenHeader);
            log.info("Token validate");
        } catch (TokenExpiredException te) {
            return "토큰이 만료되었습니다.";
        } catch (SignatureVerificationException sve) {
            return "토큰이 변조되었습니다.";
        } catch (JWTDecodeException jde) {
            return "토큰의 유형이 아닙니다.";
        } catch (Exception e) {
            return "이 토큰이 맞아요?";
        }
        return null;
    }
}
