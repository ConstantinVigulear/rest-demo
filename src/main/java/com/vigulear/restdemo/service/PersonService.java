package com.vigulear.restdemo.service;

import com.vigulear.restdemo.entity.Person;
import org.springframework.stereotype.Service;

/**
 * @author Constantin Vigulear
 */
public interface PersonService {
  Person getPersonById(Long id);
}
