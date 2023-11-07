package com.vigulear.restdemo.service;

import com.vigulear.restdemo.entity.Person;

/**
 * @author Constantin Vigulear
 */
public interface PersonService {
  Person getPersonById(Long id);
}
