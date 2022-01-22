package com.ml4fakenews.emailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import org.springframework.mail.javamail.JavaMailSender;
@RestController
public class EmailController {

    @Autowired
    private JavaMailSender javaMailSender;

    public void createEmail(String message, String subject, String recipent) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(recipent);
        msg.setSubject(subject);
        msg.setText(message);
        System.out.println("wysylam");
        javaMailSender.send(msg);
    }

    static private final List<String> emailHistory = new ArrayList<>();
    @GetMapping
    List<String> getEmailHistory()
    {
        return emailHistory;
    }

    @PostMapping("/send-email")
    String sendEmail(@RequestBody String email) {
        emailHistory.add(email);
        createEmail("Witamy w systemie ml4fakenews - nowoczesnym rozwiązaniu do wykrywania fake newsów. Możesz już się zalogować na Twoje konto przy użyciu loginu oraz hasła wprowadzonych przy rejestracji", "Witamy w systemie ml4fakenews", email);
        return "Email sent to " + email;
    }
}
