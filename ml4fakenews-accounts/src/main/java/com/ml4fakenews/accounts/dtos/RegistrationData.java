package com.ml4fakenews.accounts.dtos;

import lombok.Data;

@Data
public class RegistrationData {
    private String username;
    private String password;
    private String email;

}