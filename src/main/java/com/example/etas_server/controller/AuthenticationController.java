package com.example.etas_server.controller;

import com.example.etas_server.dto.Request;
import com.example.etas_server.dto.Response;
import com.example.etas_server.model.User;
import com.example.etas_server.repository.UserRepo;
import com.example.etas_server.security.AuthorizationChecker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("login")
public class AuthenticationController {

    UserRepo userRepo;
    AuthorizationChecker authorizationChecker;

    @Autowired
    public AuthenticationController(UserRepo userRepo,
                                    AuthorizationChecker authorizationChecker) {
        this.userRepo = userRepo;
        this.authorizationChecker = authorizationChecker;
    }

    @PostMapping
    public @ResponseBody Response authenticate(@RequestBody Request<Object> request)
    {
        Optional<User> user = userRepo.findByLogin(request.getUser().getLogin());
        Long userId = (user.isPresent())? user.get().getId() : -1;

        if(authorizationChecker.checkPassword(userId, request.getUser().getPassword()))
            return new Response(200, new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(user.get()));
        return new Response(-1, "Incorrect login or password");
    }

    @GetMapping
    public @ResponseBody Response getSalt(@RequestParam String login) {
        Optional<User> user = userRepo.findByLogin(login);
        if(user.isPresent())
           return new Response(200, user.get().getSalt());
        return new Response(-1, "Incorrect login");
    }

}
