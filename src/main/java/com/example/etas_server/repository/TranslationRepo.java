package com.example.etas_server.repository;

import com.example.etas_server.model.Dictionary;
import com.example.etas_server.model.Translation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TranslationRepo extends CrudRepository<Translation, Long> {

    Set<Translation> findAllByDictionary(Dictionary dictionary);

}
