package com.example.etas_server.repository;

import com.example.etas_server.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findByLogin(String login);

}
