package com.ml4fakenews.accounts;

import com.ml4fakenews.accounts.dtos.RegistrationData;
import com.ml4fakenews.accounts.entities.Account;
import com.ml4fakenews.accounts.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/accounts/{id}")
    public Account getAccountById(@PathVariable int id) {
        return accountService.getAccountById(id);
    }

    @PostMapping("/accounts")
    ResponseEntity<Account> registerAccount(@RequestBody RegistrationData registrationData) {
        Account registeredAccount = accountService.registerAccount(registrationData);
        return new ResponseEntity(registeredAccount, HttpStatus.CREATED);
    }
}
