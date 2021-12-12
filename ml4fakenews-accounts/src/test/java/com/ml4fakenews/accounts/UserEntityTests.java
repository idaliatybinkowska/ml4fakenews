package com.ml4fakenews.accounts;


import com.ml4fakenews.accounts.entities.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTests {

    @Test
    void verifyingAccount() {
        User acc = new User("RandomUser","asdjhł$$3A", "some@email.com");
        acc.verify();
        assertThat(acc.isVerified()).isTrue();
    }

}
