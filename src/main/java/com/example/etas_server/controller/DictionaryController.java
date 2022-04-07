package com.example.etas_server.controller;

import com.example.etas_server.model.Dictionary;
import com.example.etas_server.model.User;
import com.example.etas_server.repository.DictionaryRepo;
import com.example.etas_server.repository.UserRepo;
import com.example.etas_server.security.AuthorizationChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("dictionaries")
public class DictionaryController {


    final AuthorizationChecker authorizationChecker;

    UserRepo userRepo;
    DictionaryRepo dictionaryRepo;

    @Autowired
    public DictionaryController(AuthorizationChecker authorizationChecker, UserRepo userRepo, DictionaryRepo dictionaryRepo) {
        this.authorizationChecker = authorizationChecker;
        this.userRepo = userRepo;
        this.dictionaryRepo = dictionaryRepo;
    }

    @PostMapping("/{user_id}")
    public String addDictionary(@PathVariable Long user_id,
                                @RequestParam String user_password,
                                @RequestBody Dictionary dictionary) {
        if (!authorizationChecker.checkPassword(user_id, user_password))
            return "Invalid user id or password";

        User user = userRepo.findById(user_id).get();
        dictionary.setUser(user);
        dictionaryRepo.save(dictionary);

        for (var dict : user.getDictionaries())
            System.out.println(dict.toString());

        return "Dictionary has been added";
    }

}
