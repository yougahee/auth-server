package com.gaga.auth_server;

import com.gaga.auth_server.dto.response.DefaultResponseDTO;
import com.gaga.auth_server.exception.NoExistEmailException;
import com.gaga.auth_server.exception.NotFoundException;
import com.gaga.auth_server.exception.UnauthorizedException;
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
    private DefaultResponseDTO defaultResponseDTO = new DefaultResponseDTO();

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        e.getBindingResult().getAllErrors().forEach((error) -> {
            log.error("filedName : " + ((FieldError) error).getField());
            defaultResponseDTO.setMessage(error.getDefaultMessage());
        });
        return ResponseEntity.ok().body(defaultResponseDTO);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<DefaultResponseDTO> unauthorizedException(UnauthorizedException ue) {
        log.error(ue.getMessage(), ue);
        defaultResponseDTO.setMessage(ue.getErrorMessage());
        return ResponseEntity.ok().body(defaultResponseDTO);
    }

    @ExceptionHandler(value = {NoExistEmailException.class})
    public ResponseEntity<DefaultResponseDTO> noExistEmailException(NoExistEmailException nee) {
        log.error(nee.getMessage(), nee);
        defaultResponseDTO.setMessage(nee.errorMessage);
        return ResponseEntity.ok().body(defaultResponseDTO);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<DefaultResponseDTO> notFoundException(NotFoundException nfe) {
        log.error(nfe.getMessage(), nfe);
        defaultResponseDTO.setMessage(nfe.getMessage());
        return ResponseEntity.ok().body(defaultResponseDTO);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<DefaultResponseDTO> nullPointerException(NullPointerException ne) {
        log.error(ne.getMessage(), ne);
        defaultResponseDTO.setMessage(ne.getMessage());
        return ResponseEntity.ok().body(defaultResponseDTO);
    }

    @ExceptionHandler(value = {MailException.class})
    public ResponseEntity<DefaultResponseDTO> mailSendException(MailException mse) {
        log.error(mse.getMessage(), mse);
        defaultResponseDTO.setMessage(mse.getMessage());
        return ResponseEntity.ok().body(defaultResponseDTO);
    }
}
