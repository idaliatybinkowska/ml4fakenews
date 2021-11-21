package com.ml4fakenews.accounts.repositories;

import com.ml4fakenews.accounts.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {}
