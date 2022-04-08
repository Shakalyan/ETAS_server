package com.example.etas_server.security;

import com.example.etas_server.model.User;
import com.example.etas_server.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorizationChecker
{

    UserRepo userRepo;

    @Autowired
    public AuthorizationChecker(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean checkPassword(Long user_id, String password) {
        Optional<User> user = userRepo.findById(user_id);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

}
