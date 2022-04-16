package com.example.etas_server.controller;

import com.example.etas_server.dto.Response;
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
    public @ResponseBody Set<Translation> getAll(@RequestParam("user_id") long userId,
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
    public @ResponseBody Response add(@RequestParam("user_id") long userId,
                                      @RequestParam("user_pswd") String userPassword,
                                      @RequestParam("dict_id") long dictId,
                                      @RequestBody Translation translation)
    {
        if(!authorizationChecker.checkPassword(userId, userPassword))
            return new Response(-1, "Incorrect user_id or password");

        Optional<Dictionary> dictionary = dictionaryRepo.findById(dictId);
        if(!dictionary.isPresent())
            return new Response(-1, "Incorrect dict_id");

        translation.setDictionary(dictionary.get());
        translationRepo.save(translation);
        return new Response(200, "Successfully created");
    }

    @DeleteMapping
    public @ResponseBody Response delete(@RequestParam("user_id") long userId,
                                         @RequestParam("user_pswd") String userPassword,
                                         @RequestParam("dict_id") long dictId,
                                         @RequestParam("tr_id") long translationId) {
        if(!authorizationChecker.checkPassword(userId, userPassword))
            return new Response(-1, "Incorrect user_id or password");
        Optional<Dictionary> dict = dictionaryRepo.findById(dictId);
        if(!dict.isPresent())
            return new Response(-1, "Incorrect dict_id");
        if(!dict.get().getTranslations().removeIf(t -> t.getId().equals(translationId)))
            return new Response(-1, "Dictionary doesn't contain translation with this id");
        translationRepo.deleteById(translationId);
        return new Response(200, "Successfully deleted");
    }

    @PostMapping("translate")
    public @ResponseBody Response translate(@RequestParam String sentence,
                                            @RequestParam String source,
                                            @RequestParam String target) {
        return translationService.getTranslation(sentence, source, target);
    }

}
