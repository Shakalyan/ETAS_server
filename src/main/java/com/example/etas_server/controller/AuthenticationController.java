package com.example.etas_server.controller;

import com.example.etas_server.dto.Request;
import com.example.etas_server.dto.Response;
import com.example.etas_server.model.User;
import com.example.etas_server.repository.UserRepo;
import com.example.etas_server.security.AuthorizationChecker;
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
        if(authorizationChecker.checkPassword(request.getUser().getId(), request.getUser().getPassword()))
            return new Response(200, "Successfully authenticated");
        return new Response(-1, "Incorrect login or password");
    }

}
