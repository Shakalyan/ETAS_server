package com.example.etas_server.controller;

import com.example.etas_server.model.User;
import com.example.etas_server.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registration/")
public class RegistrationController {

    UserRepo userRepo;

    @Autowired
    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping
    public boolean register(@RequestBody User user) {
        if (userRepo.findByLogin(user.getLogin()).isPresent())
            return false;
        user.setId(null);
        userRepo.save(user);
        return true;
    }

}
