package com.example.etas_server.controller;

import com.example.etas_server.dto.Request;
import com.example.etas_server.dto.Response;
import com.example.etas_server.model.User;
import com.example.etas_server.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("registration")
public class RegistrationController {

    UserRepo userRepo;

    @Autowired
    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping
    public @ResponseBody Response register(@RequestBody Request<Object> request) {
        if (userRepo.findByLogin(request.getUser().getLogin()).isPresent())
            return new Response(-1, "User with this login already exists");
        User user = request.getUser();
        user.setId(null);
        userRepo.save(user);
        return new Response(200, "Successfully registered");
    }
}
