package com.ml4fakenews.accounts;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Data
@Entity
public class Account {

    @Id
    private int id;
    private String name;
    private String email;
    private boolean isVerified;

    public Account(final int id, final String name, final String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isVerified = false;
    }

    public void verify() {
        this.isVerified = true;
    }

}
