package com.ml4fakenews.accounts.dtos;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UserData {
    private Integer id;
    private String username;
    private String email;
    private ArrayList<String> authorities;

    public UserData(Integer id, String username, String email, ArrayList<String> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }
}
