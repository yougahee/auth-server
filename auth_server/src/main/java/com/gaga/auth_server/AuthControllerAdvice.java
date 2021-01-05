package com.gaga.auth_server;

import com.gaga.auth_server.dto.Message;
import com.gaga.auth_server.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AuthControllerAdvice {
    Message errorMessage = new Message();

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Message> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        e.getBindingResult().getAllErrors().forEach((error) -> {
            log.error("filedName : " + ((FieldError) error).getField());
            errorMessage.setMessage(error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<Message> unauthorizedException(UnauthorizedException ue) {
        log.error(ue.getMessage(), ue);
        return ResponseEntity.badRequest().body(new Message(ue.getMessage()));
    }

    @ExceptionHandler(value = {NoExistEmailException.class})
    public ResponseEntity<Message> noExistEmailException(NoExistEmailException nee) {
        log.error(nee.getMessage(), nee);
        return ResponseEntity.badRequest().body(new Message(nee.getMessage()));
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Message> notFoundException(NotFoundException nfe) {
        log.error(nfe.getMessage(), nfe);
        return ResponseEntity.badRequest().body(new Message(nfe.getMessage()));
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<Message> nullPointerException(NullPointerException ne) {
        log.error(ne.getMessage(), ne);
        return ResponseEntity.badRequest().body(new Message(ne.getMessage()));
    }

    @ExceptionHandler(value = {MailException.class})
    public ResponseEntity<Message> mailSendException(MailException mse) {
        log.error(mse.getMessage(), mse);
        return ResponseEntity.badRequest().body(new Message(mse.getMessage()));
    }

    @ExceptionHandler(value = {AlreadyExistException.class})
    public ResponseEntity<Message> existNickNameException(AlreadyExistException ene) {
        log.error(ene.getMessage(), ene);
        return ResponseEntity.badRequest().body(new Message(ene.getMessage()));
    }

    @ExceptionHandler(value = {NoNegativeNumberException.class})
    public ResponseEntity<Message> noNegativeNumberException(NoNegativeNumberException nne) {
        log.error(nne.getMessage(), nne);
        return ResponseEntity.badRequest().body(new Message(nne.getMessage()));
    }
}
