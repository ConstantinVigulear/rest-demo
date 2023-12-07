package com.vigulear.restdemo.controller;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.InvalidValueException;
import com.vigulear.restdemo.service.CatService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : crme059
 * @created : 06-Dec-23, Wednesday
 */
@SpringBootTest
@AutoConfigureMockMvc
class CatControllerIntegrationTest {

  @Autowired MockMvc mockMvc;
  @Autowired CatService catService;
  List<Cat> cats;

  @BeforeEach
  void init() {
    cats =
        List.of(
            Cat.builder().name("Humus").age(1).build(),
            Cat.builder().name("Couscous").age(0).build(),
            Cat.builder().name("Tiramisu").age(7).build(),
            Cat.builder().name("Object").age(3).build(),
            Cat.builder().name("Engin").age(9).build(),
            Cat.builder().name("Revision").age(8).build(),
            Cat.builder().name("Jacobson").age(10).build(),
            Cat.builder().name("Ivar").age(4).build(),
            Cat.builder().name("Enzo").age(6).build(),
            Cat.builder().name("Finland").age(15).build(),
            Cat.builder().name("Robert").age(13).build());
  }

  @AfterEach
  void tearDown() {
    List<CatDto> catsFromDatabase = catService.findAll();
    catsFromDatabase.forEach(
        cat -> {
          try {
            catService.deleteById(cat.getId());
          } catch (InvalidValueException e) {
            throw new RuntimeException(e);
          }
        });
  }

  @Test
  void createCat() {
    Cat catNew = Cat.builder().name("Napoleon").age(11).build();
    CatDto catNewDto = catService.createCat(catNew);

    List<CatDto> allCats = catService.findAll();

    assertThat(allCats).contains(catNewDto);
  }

  @Test
  void getAllCats() {
    Integer initialSize = catService.findAll().size();

    catService.createAllCats(cats);
    Integer updatedSize = catService.findAll().size();

    assertThat(updatedSize).isNotEqualTo(initialSize).isGreaterThan(initialSize);
  }

  @Test
  void getCatById() {
    List<CatDto> createdCats = catService.createAllCats(cats);
    CatDto catDtoSaved = createdCats.getFirst();

    CatDto catDtoQueried = catService.findById(catDtoSaved.getId());

    assertThat(catDtoSaved).isEqualTo(catDtoQueried);
  }

  @Test
  void getTopByField() {}

  @Test
  void getTopThree() {}

  @Test
  void findTheYoungest() {}

  @Test
  void findTotalBy() {}
}
