package com.example.etas_server.controller;

import com.example.etas_server.dto.Request;
import com.example.etas_server.dto.Response;
import com.example.etas_server.dto.TranslationRequest;
import com.example.etas_server.model.Dictionary;
import com.example.etas_server.model.Translation;
import com.example.etas_server.repository.DictionaryRepo;
import com.example.etas_server.repository.TranslationRepo;
import com.example.etas_server.security.AuthorizationChecker;
import com.example.etas_server.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public @ResponseBody Set<Translation> getAllByDictId(@RequestBody Request<Long> request) {
        if(!authorizationChecker.checkPassword(request.getUser().getId(), request.getUser().getPassword()))
            return null;

        Long dictId = request.getData();
        Optional<Dictionary> dictionary = dictionaryRepo.findById(dictId);
        if(!dictionary.isPresent())
            return null;

        Set<Translation> translations = translationRepo.findAllByDictionary(dictionary.get());
        for(Translation t : translations)
            System.out.println(t.toString());

        return translations;
    }

    @PostMapping
    public @ResponseBody Response add(@RequestBody Request<Translation> request,
                                      @RequestParam(name="dict_id") Long dictId)
    {
        if(!authorizationChecker.checkPassword(request.getUser().getId(), request.getUser().getPassword()))
            return new Response(-1, "Incorrect user_id or password");

        System.out.println(dictId);
        Optional<Dictionary> dictionary = dictionaryRepo.findById(dictId);
        if(!dictionary.isPresent())
            return new Response(-1, "Incorrect dict_id");

        Translation translation = request.getData();
        translation.setDictionary(dictionary.get());
        translation = translationRepo.save(translation);
        return new Response(200, translation.getId().toString());
    }

    @DeleteMapping
    public @ResponseBody Response delete(@RequestBody Request<Translation[]> request,
                                         @RequestParam(name="dict_id") Long dictId) {
        if(!authorizationChecker.checkPassword(request.getUser().getId(), request.getUser().getPassword()))
            return new Response(-1, "Incorrect user_id or password");

        Optional<Dictionary> dict = dictionaryRepo.findById(dictId);
        if(!dict.isPresent())
            return new Response(-1, "Incorrect dict_id");

        for(Translation translation : request.getData()) {
            long translationId = translation.getId();
            dict.get().getTranslations().removeIf(t -> t.getId().equals(translationId));
            translationRepo.deleteById(translationId);
        }
        return new Response(200, "Successfully deleted");
    }

    @PostMapping("translate")
    public @ResponseBody Response translate(@RequestBody Request<TranslationRequest> request) {
        return translationService.getTranslation(request.getData());
    }

}
