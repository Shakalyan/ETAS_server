package com.example.etas_server.controller;

import com.example.etas_server.model.Dictionary;
import com.example.etas_server.model.Translation;
import com.example.etas_server.model.User;
import com.example.etas_server.repository.DictionaryRepo;
import com.example.etas_server.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("data")
public class InfoController {

    UserRepo userRepo;
    DictionaryRepo dictionaryRepo;

    @Autowired
    public InfoController(UserRepo userRepo,
                          DictionaryRepo dictionaryRepo) {
        this.userRepo = userRepo;
        this.dictionaryRepo = dictionaryRepo;
    }

    @GetMapping("users")
    public String printAllUsers() {
        StringBuilder info = new StringBuilder();
        for (User user : userRepo.findAll())
        {
            info.append(user.toString()).append("<br>Dictionaries:<br>");
            for (Dictionary d : user.getDictionaries()) {
                info.append(d.getName()).append("<br>Translations:<br>");
                for (Translation t : d.getTranslations())
                    info.append(t).append("<br>");
            }
            info.append("<br><br>");
        }
        System.out.print(info);
        return info.toString();
    }

    @GetMapping("users/{id}")
    public String printUser(@RequestParam long id, @RequestParam boolean pr_dict) {
        Optional<User> user = userRepo.findById(id);
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

    @GetMapping("dictionaries")
    public String printDictionaries() {
        StringBuilder response = new StringBuilder();
        for(Dictionary dict : dictionaryRepo.findAll()) {
            response.append(dict.toString()).append("<br>Translations:<br>");
            for (Translation tr : dict.getTranslations())
                response.append(tr.toString()).append("<br>");
            response.append("<br>");
        }
        return response.toString();
    }

}
