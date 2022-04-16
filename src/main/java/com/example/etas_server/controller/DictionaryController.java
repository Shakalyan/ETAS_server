package com.example.etas_server.controller;

import com.example.etas_server.dto.Response;
import com.example.etas_server.model.Dictionary;
import com.example.etas_server.model.User;
import com.example.etas_server.repository.DictionaryRepo;
import com.example.etas_server.repository.TranslationRepo;
import com.example.etas_server.repository.UserRepo;
import com.example.etas_server.security.AuthorizationChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("dictionaries")
public class DictionaryController {

    AuthorizationChecker authorizationChecker;
    UserRepo userRepo;
    DictionaryRepo dictionaryRepo;
    TranslationRepo translationRepo;

    @Autowired
    public DictionaryController(AuthorizationChecker authorizationChecker,
                                UserRepo userRepo,
                                DictionaryRepo dictionaryRepo,
                                TranslationRepo translationRepo) {
        this.authorizationChecker = authorizationChecker;
        this.userRepo = userRepo;
        this.dictionaryRepo = dictionaryRepo;
        this.translationRepo = translationRepo;
    }

    @PostMapping
    public @ResponseBody Response add(@RequestParam("user_id") long user_id,
                                      @RequestParam("user_pswd") String user_password,
                                      @RequestBody Dictionary dictionary) {
        if (!authorizationChecker.checkPassword(user_id, user_password))
            return new Response(-1, "Incorrect user id or password");

        User user = userRepo.findById(user_id).get();
        dictionary.setUser(user);
        dictionaryRepo.save(dictionary);

        for (Dictionary dict : user.getDictionaries())
            System.out.println(dict.toString());

        return new Response(200, "Dictionary has been added");
    }

    @DeleteMapping
    public @ResponseBody Response delete(@RequestParam("user_id") long userId,
                                         @RequestParam("user_pswd") String userPassword,
                                         @RequestParam("dict_id") long dictId) {

        if(!authorizationChecker.checkPassword(userId, userPassword))
            return new Response(-1, "Incorrect user_id or password");

        User user = userRepo.findById(userId).get();
        if(!user.getDictionaries().removeIf(d -> d.getId().equals(dictId)))
            return new Response(-1, "User doesn't have such a dictionary");

        Dictionary dictionary = dictionaryRepo.findById(dictId).get();

        dictionary.getTranslations().stream().forEach(t -> {
            t.setDictionary(null);
            translationRepo.delete(t);
        });
        dictionary.getTranslations().clear();
        dictionaryRepo.deleteById(dictId);
        return new Response(200, "Successfully deleted");
    }

}
