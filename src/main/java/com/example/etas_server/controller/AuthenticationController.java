package com.example.etas_server.controller;

import com.example.etas_server.dto.Response;
import com.example.etas_server.model.User;
import com.example.etas_server.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("login")
public class AuthenticationController {

    UserRepo userRepo;

    @Autowired
    public AuthenticationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping
    public @ResponseBody Response authenticate(@RequestBody User user)
    {
        Optional<User> eUser = userRepo.findByLogin(user.getLogin());
        if(!eUser.isPresent())
            return new Response(-1, "User with this login doesn't exist");
        if(!eUser.get().getPassword().equals(user.getPassword()))
            return new Response(-1, "Incorrect password");
        return new Response(200, "Successfully authenticated");
    }

}
