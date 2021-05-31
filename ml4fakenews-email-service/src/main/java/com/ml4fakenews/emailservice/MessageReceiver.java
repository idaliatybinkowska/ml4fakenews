package com.ml4fakenews.emailservice;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    @Bean
    public Queue account_created(){
        return new Queue("account_created", false);
    }

    @Autowired
    EmailController emailController;

    @RabbitListener(queues = "account_created")
    public void receive(String message){
        emailController.sendEmail(message);
    }
}
