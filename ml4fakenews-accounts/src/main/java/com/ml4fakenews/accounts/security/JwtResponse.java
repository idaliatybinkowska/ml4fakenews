package com.ml4fakenews.accounts.security;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private int id;

    private String username;

    private List roles;

    private String token;

    private LocalDateTime tokenExpirationDate;

    public JwtResponse(int id, String username, List roles, String token, LocalDateTime tokenExpirationDate) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.token = token;
        this.tokenExpirationDate = tokenExpirationDate;
    }
}
