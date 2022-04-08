package com.example.etas_server.controller;

import com.example.etas_server.model.User;
import com.example.etas_server.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login/")
public class AuthenticationController {

    UserRepo userRepo;

    @Autowired
    public AuthenticationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping
    public boolean authenticate(@RequestBody User user)
    {
        var eUser = userRepo.findByLogin(user.getLogin());
        return eUser.isPresent() && eUser.get().getPassword().equals(user.getPassword());
    }

}
