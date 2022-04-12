package com.example.etas_server.repository;

import com.example.etas_server.model.Dictionary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepo extends CrudRepository<Dictionary, Long> {

}
