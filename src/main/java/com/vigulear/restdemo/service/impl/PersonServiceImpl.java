package com.vigulear.restdemo.service.impl;

import com.vigulear.restdemo.entity.Person;
import com.vigulear.restdemo.repository.PersonRepository;
import com.vigulear.restdemo.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Constantin Vigulear
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    public PersonServiceImpl(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Person getPersonById(Long id) {
        Optional<Person> result = repository.findById(id);
        return result.orElse(null);
    }
}
