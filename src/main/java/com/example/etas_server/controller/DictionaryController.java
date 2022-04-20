package com.example.etas_server.controller;

import com.example.etas_server.dto.Request;
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
    public @ResponseBody Response add(@RequestBody Request<Dictionary> request) {
        if (!authorizationChecker.checkPassword(request.getUser().getId(), request.getUser().getPassword()))
            return new Response(-1, "Incorrect user id or password");

        User user = userRepo.findById(request.getUser().getId()).get();
        Dictionary dictionary = request.getData();
        dictionary.setUser(user);
        dictionary = dictionaryRepo.save(dictionary);

        return new Response(200, dictionary.getId().toString());
    }

    @DeleteMapping
    public @ResponseBody Response deleteById(@RequestBody Request<Long> request) {

        if(!authorizationChecker.checkPassword(request.getUser().getId(), request.getUser().getPassword()))
            return new Response(-1, "Incorrect user_id or password");

        User user = userRepo.findById(request.getUser().getId()).get();
        Long dictId = request.getData();

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
