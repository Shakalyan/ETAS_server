package com.example.etas_server.controller;

import com.example.etas_server.model.Dictionary;
import com.example.etas_server.model.Translation;
import com.example.etas_server.repository.DictionaryRepo;
import com.example.etas_server.repository.TranslationRepo;
import com.example.etas_server.security.AuthorizationChecker;
import com.example.etas_server.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("translations")
public class TranslationController {

    TranslationRepo translationRepo;
    DictionaryRepo dictionaryRepo;
    AuthorizationChecker authorizationChecker;
    TranslationService translationService;

    @Autowired
    public TranslationController(TranslationRepo translationRepo,
                                 DictionaryRepo dictionaryRepo,
                                 AuthorizationChecker authorizationChecker,
                                 TranslationService translationService) {
        this.translationRepo = translationRepo;
        this.dictionaryRepo = dictionaryRepo;
        this.authorizationChecker = authorizationChecker;
        this.translationService = translationService;
    }

    @GetMapping
    public Set<Translation> getAll(@RequestParam("user_id") long userId,
                                   @RequestParam("user_pswd") String userPassword,
                                   @RequestParam("dict_id") long dictId) {
        if(!authorizationChecker.checkPassword(userId, userPassword))
            return null;

        Optional<Dictionary> dictionary = dictionaryRepo.findById(dictId);
        if(!dictionary.isPresent())
            return null;

        Set<Translation> translations = translationRepo.findAllByDictionary(dictionary.get());
        for(Translation t : translations)
            System.out.println(t.toString());

        return translations;
    }

    @PostMapping
    public boolean add(@RequestParam("user_id") long userId,
                       @RequestParam("user_pswd") String userPassword,
                       @RequestParam("dict_id") long dictId,
                       @RequestBody Translation translation)
    {
        if(!authorizationChecker.checkPassword(userId, userPassword))
            return false;

        Optional<Dictionary> dictionary = dictionaryRepo.findById(dictId);
        if(!dictionary.isPresent())
            return false;

        translation.setDictionary(dictionary.get());
        translationRepo.save(translation);
        return true;
    }

    @PostMapping("translate")
    public String translate(@RequestParam String sentence,
                            @RequestParam String source,
                            @RequestParam String target) {
        return translationService.getTranslation(sentence, source, target);
    }

}
