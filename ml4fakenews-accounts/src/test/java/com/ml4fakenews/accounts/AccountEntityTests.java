package com.ml4fakenews.accounts;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountEntityTests {

    @Test
    void verifyingAccount() {
        Account acc = new Account(1, "RandomUser", "some@email.com");
        acc.verify();
        assertThat(acc.isVerified()).isTrue();
    }

}
