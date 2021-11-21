package com.ml4fakenews.accounts;


import com.ml4fakenews.accounts.entities.Account;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountEntityTests {

    @Test
    void verifyingAccount() {
        Account acc = new Account("RandomUser", "some@email.com");
        acc.verify();
        assertThat(acc.isVerified()).isTrue();
    }

}
