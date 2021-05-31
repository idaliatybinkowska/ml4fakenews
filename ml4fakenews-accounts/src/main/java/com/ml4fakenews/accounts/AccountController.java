package com.ml4fakenews.accounts;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
public class AccountController {
    private List<Account> accounts = Arrays.asList(
            new Account(1, "Joe Bloggs", "joe.bloggs@example.com"),
            new Account(2, "Jane Doe", "jane.doe@example.com"));

    @GetMapping
    public List<Account> getAllCustomers() {
        return accounts;
    }

    @GetMapping("/{id}")
    public Account getCustomerById(@PathVariable int id) {
        return accounts.stream()
                .filter(customer -> customer.getId() == id)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @PostMapping
    Account addUser(@RequestBody Account newAccount) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbit");  //set for docker
        //factory.setHost("localhost");
        try(Connection connection = factory.newConnection()
        ) {
            Channel channel = connection.createChannel();
            channel.queueDeclare("account_created", false, false, false, null);
            String message = newAccount.getEmail();
            channel.basicPublish("", "account_created", null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return newAccount;
    }
}
