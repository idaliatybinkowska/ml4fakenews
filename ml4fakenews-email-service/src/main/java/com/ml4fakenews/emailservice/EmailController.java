package com.ml4fakenews.emailservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EmailController {

    static private final List<String> emailHistory = new ArrayList<>();
    @GetMapping
    List<String> getEmailHistory()
    {
        return emailHistory;
    }

    @PostMapping("/send-email")
    String sendEmail(@RequestBody String email) {
        emailHistory.add(email);
        return "Email sent to " + email;
    }
}
