package com.gaga.auth_server;

import com.gaga.auth_server.dto.message.ErrorMessage;
import com.gaga.auth_server.exception.*;
import com.gaga.auth_server.utils.ResponseMessage;
import io.jsonwebtoken.JwtException;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.SignatureException;

@Slf4j
@ControllerAdvice
public class AuthControllerAdvice {
    ErrorMessage errorMessage = new ErrorMessage();
    ResponseMessage responseMessage = new ResponseMessage();

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        e.getBindingResult().getAllErrors().forEach((error) -> {
            log.error("filedName : " + ((FieldError) error).getField());
            errorMessage.setMessage(error.getDefaultMessage());
        });
        return ResponseEntity
                .badRequest()
                .body(errorMessage);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<ErrorMessage> unauthorizedException(HttpServletRequest req, UnauthorizedException ue) {
        log.error(ue.getMessage(), ue);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(ue.getMessage(), 400, req.getRequestURI()));
    }

    @ExceptionHandler(value = {NoExistEmailException.class})
    public ResponseEntity<ErrorMessage> noExistEmailException(HttpServletRequest req, NoExistEmailException nee) {
        log.error(nee.getMessage(), nee);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(nee.getMessage(), 400, req.getRequestURI()));
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ErrorMessage> notFoundException(HttpServletRequest req, NotFoundException nfe) {
        log.error(nfe.getMessage(), nfe);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(nfe.getMessage(), 400, req.getRequestURI()));
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<ErrorMessage> nullPointerException(HttpServletRequest req, NullPointerException ne) {
        log.error(ne.getMessage(), ne);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(ne.getMessage(), 400, req.getRequestURI()));
    }

    @ExceptionHandler(value = {MailException.class})
    public ResponseEntity<ErrorMessage> mailSendException(HttpServletRequest req, MailException mse) {
        log.error(mse.getMessage(), mse);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(mse.getMessage(), 400, req.getRequestURI()));
    }

    @ExceptionHandler(value = {AlreadyExistException.class})
    public ResponseEntity<ErrorMessage> existNickNameException(HttpServletRequest req, AlreadyExistException ene) {
        log.error(ene.getMessage(), ene);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(ene.getMessage(), 400, req.getRequestURI()));
    }

    // ## 오류는 아니고 그냥 건너뛰어라
    @ExceptionHandler(value = {AlreadyCheckedException.class})
    public ResponseEntity<ErrorMessage> alreadyCheckedException(HttpServletRequest req, AlreadyCheckedException ace) {
        log.error(ace.getMessage(), ace);
        return ResponseEntity
                .status(HttpStatus.CONTINUE)
                .body(new ErrorMessage(ace.getMessage(), 100, req.getRequestURI()));
    }

    @ExceptionHandler(value = {NoNegativeNumberException.class})
    public ResponseEntity<ErrorMessage> noNegativeNumberException(HttpServletRequest req, NoNegativeNumberException nne) {
        log.error(nne.getMessage(), nne);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(nne.getMessage(), 400, req.getRequestURI()));
    }

    @ExceptionHandler(value = {SignatureException.class})
    public ResponseEntity<ErrorMessage> signatureException(HttpServletRequest req, SignatureException se) {
        log.error(se.getMessage(), se);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(se.getMessage(), 500, req.getRequestURI()));
    }
    //token
    @ExceptionHandler(value = {NotTokenException.class})
    public ResponseEntity<ErrorMessage> notTokenException(HttpServletRequest req, NotTokenException nte) {
        log.error(nte.getMessage(), nte);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(nte.getMessage(), HttpStatus.UNAUTHORIZED.value(), req.getRequestURI()));
    }

    @ExceptionHandler(value = {SignatureVerificationException.class})
    public ResponseEntity<ErrorMessage> signatureVerificationException(HttpServletRequest req, SignatureVerificationException sve) {
        log.error(sve.getMessage(), sve);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(sve.getMessage(), HttpStatus.UNAUTHORIZED.value(), req.getRequestURI()));
    }

    @ExceptionHandler(value = {JwtException.class})
    public ResponseEntity<ErrorMessage> signatureVerificationException(HttpServletRequest req, JwtException je) {
        log.error(je.getMessage(), je);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(je.getMessage(), HttpStatus.UNAUTHORIZED.value(), req.getRequestURI()));
    }

    //redis
    @ExceptionHandler(value = {RedisException.class})
    public ResponseEntity<ErrorMessage> signatureVerificationException(HttpServletRequest req, RedisException re) {
        log.error(re.getMessage(), re);
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(re.getMessage(), 500, req.getRequestURI()));
    }

    @ExceptionHandler(value = {RedisConnectionFailureException.class})
    public ResponseEntity<ErrorMessage> redisConnectionFailureException(HttpServletRequest req, RedisConnectionFailureException rcfe) {
        log.error(rcfe.getMessage(), rcfe);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(responseMessage.INTERNAL_SERVER_ERROR, 500, req.getRequestURI()));
    }
}
