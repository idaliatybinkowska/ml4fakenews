package com.ml4fakenews.accounts.repositories;

import com.ml4fakenews.accounts.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findUserByEmailOrUsername(String email, String username);
    Optional<User> findByUsername(String username);

}
