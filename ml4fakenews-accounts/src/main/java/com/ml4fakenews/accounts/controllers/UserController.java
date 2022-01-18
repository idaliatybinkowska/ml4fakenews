package com.ml4fakenews.accounts.controllers;

import com.ml4fakenews.accounts.dtos.LoginData;
import com.ml4fakenews.accounts.dtos.RegistrationData;
import com.ml4fakenews.accounts.dtos.UserData;
import com.ml4fakenews.accounts.entities.User;
import com.ml4fakenews.accounts.services.CaptchaService;
import com.ml4fakenews.accounts.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private UserService userService;
    private CaptchaService captchaService;

    @Autowired
    public UserController(UserService userService, CaptchaService captchaService) {
        this.userService = userService;
        this.captchaService = captchaService;
    }

    @GetMapping
    public List<UserData> getAllAccounts() {
        ArrayList<UserData> data = new ArrayList<>();
        userService.getAllAccounts().forEach(account ->  {
            data.add(userToUserData(account));

        });
        return data;
    }

    @GetMapping("/{id}")
    public UserData getAccountById(@PathVariable int id) {
        return userToUserData(userService.getAccountById(id));
    }


    @GetMapping("/username/{username}")
    public UserData getUserByUsername(@PathVariable String username) {
        return userToUserData(userService.loadUserByUsername(username));
    }

    private UserData userToUserData(User user) {
        ArrayList<String> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(role.getRolename()));
        return new UserData(user.getId(), user.getUsername(),user.getEmail(),authorities);
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
        boolean isValidCaptcha = captchaService.validateCaptcha(data.getCaptchaToken());
        if(!isValidCaptcha) {
            return new ResponseEntity("Błędna captcha", HttpStatus.BAD_REQUEST);
        }
        return userService.authenticateUser(data.getUsername(), data.getPassword());
    }


}
