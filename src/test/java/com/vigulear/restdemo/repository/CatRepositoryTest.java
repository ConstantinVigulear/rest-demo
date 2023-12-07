package com.vigulear.restdemo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.vigulear.restdemo.entity.Cat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * @author : crme059
 * @created : 01-Dec-23, Friday
 */
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) // This works by default
class CatRepositoryTest {

  @Autowired CatRepository catRepository;

  @BeforeEach
  void setUp() {
    List<Cat> cats =
        new ArrayList<>() {
          {
            add(Cat.builder().name("Georges").age(1).build());
            add(Cat.builder().name("Couscous").age(0).build());
            add(Cat.builder().name("Object").age(3).build());
            add(Cat.builder().name("Engin").age(6).build());
            add(Cat.builder().name("Revision").age(8).build());
            add(Cat.builder().name("Jacobson").age(10).build());
            add(Cat.builder().name("Ivar").age(4).build());
            add(Cat.builder().name("Enzo").age(6).build());
            add(Cat.builder().name("Finland").age(15).build());
            add(Cat.builder().name("Robert").age(13).build());
          }
        };
    catRepository.saveAll(cats);
  }

  @Test
  void findTopByField_validPayload_returnValidCats() {
    Integer quantity = 2;
    String fieldName = "age";

    List<Cat> cats = catRepository.findTopByField(quantity, fieldName);

    assertThat(cats).isNotNull();
    assertThat(cats.size()).isEqualTo(2);
    assertThat(cats.get(0).getAge()).isEqualTo(0);
    assertThat(cats.get(0).getAge()).isLessThan(cats.get(1).getAge());
  }

  /*
  Todo: test other methods
   */
}
