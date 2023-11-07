package com.vigulear.restdemo.repository;

import com.vigulear.restdemo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Constantin Vigulear
 */
public interface PersonRepository extends JpaRepository<Person, Long> {}
