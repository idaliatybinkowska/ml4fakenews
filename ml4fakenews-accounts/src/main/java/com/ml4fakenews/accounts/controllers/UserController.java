package com.ml4fakenews.accounts.controllers;

import com.ml4fakenews.accounts.dtos.LoginData;
import com.ml4fakenews.accounts.dtos.RegistrationData;
import com.ml4fakenews.accounts.entities.User;
import com.ml4fakenews.accounts.services.CaptchaService;
import com.ml4fakenews.accounts.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class UserController {
    private UserService userService;
    CaptchaService captchaService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllAccounts() {
        return userService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public User getAccountById(@PathVariable int id) {
        return userService.getAccountById(id);
    }


    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.loadUserByUsername(username);
    }

    @PostMapping("/register")
    ResponseEntity<?> registerAccount(@RequestBody RegistrationData registrationData) {
        if(!userService.isPasswordCorrect(registrationData)) {
            return new ResponseEntity("Hasło jest zbyt słabe. Musi mieć długość od 8 do 20 znaków, małe i duże litery, znak specjalny oraz cyfrę", HttpStatus.BAD_REQUEST);
        } else if(userService.isAccountAlreadyCreated(registrationData.getEmail(), registrationData.getUsername())) {
            return new ResponseEntity("Konto już istnieje ! ", HttpStatus.BAD_REQUEST);
        }
        userService.registerAccount(registrationData);
        return new ResponseEntity("Konto zostało pomyślnie utworzone", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginData data) {
        //boolean isValidCaptcha = captchaService.validateCaptcha(data.getCaptchaToken());
        //if(!isValidCaptcha){
          //  return new ResponseEntity("Błędna captcha", HttpStatus.BAD_REQUEST);
        //}
        return userService.authenticateUser(data.getUsername(), data.getPassword());
    }


}
