package com.ml4fakenews.accounts.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Data
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private boolean isVerified;

    public Account(final int id, final String name, final String email, final boolean isVerified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isVerified = isVerified;
    }

    public Account(final String name, final String email) {
        this.name = name;
        this.email = email;
        this.isVerified = false;
    }

    public void verify() {
        this.isVerified = true;
    }
}
