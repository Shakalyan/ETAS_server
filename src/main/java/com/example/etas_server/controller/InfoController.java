package com.example.etas_server.controller;

import com.example.etas_server.model.Dictionary;
import com.example.etas_server.model.User;
import com.example.etas_server.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("data")
public class InfoController {

    UserRepo userRepo;

    @Autowired
    public InfoController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("users")
    public String printAllUsers() {
        StringBuilder info = new StringBuilder();
        for (var user : userRepo.findAll())
        {
            info.append(user.toString()).append("<br>");
        }
        System.out.print(info);
        return info.toString();
    }

    @GetMapping("users/{id}")
    public String printUser(@PathVariable int id, @RequestParam boolean pr_dict)
    {
        var user = userRepo.findById((long)id);
        if (!user.isPresent())
            return String.format("User with id %d doesn't exist", id);

        StringBuilder response = new StringBuilder(user.toString());
        if (pr_dict) {
            response.append("<br>Dictionaries:<br>");
            for (Dictionary d : user.get().getDictionaries())
                response.append(d.getName());
        }

        return response.toString();
    }

}
