package com.vigulear.restdemo.controller;

import com.vigulear.restdemo.entity.Person;
import com.vigulear.restdemo.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Constantin Vigulear
 */
@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/people/{id}")
    public Person getPersonById(@PathVariable Long id) throws Exception {
        Person person = personService.getPersonById(id);

        if (person == null) {
            throw new Exception("There is no user with ID = " + id + " in database");
        }

        return person;
    }
}
