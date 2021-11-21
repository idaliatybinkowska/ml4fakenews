package com.ml4fakenews.accounts.services;

import com.ml4fakenews.accounts.dtos.RegistrationData;
import com.ml4fakenews.accounts.entities.Account;
import com.ml4fakenews.accounts.repositories.AccountRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final RabbitTemplate queueClient;

    @Autowired
    public AccountService(AccountRepository accountRepository, RabbitTemplate queueClient) {
        this.accountRepository = accountRepository;
        this.queueClient = queueClient;
    }

    public List<Account> getAllAccounts() {
        List<Account> allAccounts = StreamSupport.stream(accountRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return allAccounts;
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public Account registerAccount(RegistrationData registrationData) {
        Account newAccount = new Account(registrationData.getName(), registrationData.getEmail());
        accountRepository.save(newAccount);
        queueClient.convertAndSend("account_created", newAccount.getEmail());
        return newAccount;
    }

}
