package com.ml4fakenews.subscriptions;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Data
@Entity
public class Subscription {

    @Id
    private int id;
    private int accountId;
    private String name;

    public Subscription(final int id,  final int accountId, final String name) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
    }
}
