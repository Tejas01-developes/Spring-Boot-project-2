package employees.data.project2.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class email {
    @Value("${spring.mail.username}")
    private String mailfrom;

    private final JavaMailSender mailSender;


    public email(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
@Async
    public void sendmails(String to, String subject, String text) throws MessagingException {
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
    helper.setFrom(String.valueOf(mailfrom));
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(text);
    mailSender.send(message);






    }






}
