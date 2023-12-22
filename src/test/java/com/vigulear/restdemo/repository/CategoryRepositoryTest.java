package com.vigulear.restdemo.repository;

import com.vigulear.restdemo.bootstrap.BootstrapData;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.entity.Category;
import com.vigulear.restdemo.service.impl.CatCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : crme059, Constantin Vigulear
 */
@DataJpaTest
@Import({BootstrapData.class, CatCsvServiceImpl.class})
class CategoryRepositoryTest {

  @Autowired CategoryRepository categoryRepository;
  @Autowired CatRepository catRepository;

  Cat testCat;

  @BeforeEach
  void setUp() {
    testCat = catRepository.findAll().getFirst();
  }

  @Test
  void testAddCategory() {
    Category savedCategory =
        categoryRepository.save(Category.builder().description("Street").build());

    testCat.addCategory(savedCategory);
    Cat savedCat = catRepository.save(testCat);

    assertThat(savedCat.getCategories().size()).isEqualTo(1);
  }
}
