package com.gaga.auth_server.utils.mail;

import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomMailSender {
    private final JavaMailSender javaMailSender;
    @Value("spring.mail.username")
    private String FROM_EMAIL;

    public void sendMail(MailDTO mailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(ResponseMessage.SEND_EMAIL_TITLE);
        message.setFrom(FROM_EMAIL);
        message.setTo(mailDTO.getAddress());
        message.setText(mailDTO.getMessage());

        javaMailSender.send(message);
    }
}
