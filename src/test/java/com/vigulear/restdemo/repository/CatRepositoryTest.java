package com.vigulear.restdemo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vigulear.restdemo.bootstrap.BootstrapData;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.service.impl.CatCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

/**
 * @author : crme059
 * @created : 01-Dec-23, Friday
 */
@DataJpaTest
@Import({BootstrapData.class, CatCsvServiceImpl.class})
class CatRepositoryTest {

  @Autowired CatRepository catRepository;

//  @BeforeEach
//  void setUp() {
//    List<Cat> cats =
//        new ArrayList<>() {
//          {
//            add(Cat.builder().name("Georges").age(1).build());
//            add(Cat.builder().name("Couscous").age(0).build());
//            add(Cat.builder().name("Object").age(3).build());
//            add(Cat.builder().name("Engin").age(6).build());
//            add(Cat.builder().name("Revision").age(8).build());
//            add(Cat.builder().name("Jacobson").age(10).build());
//            add(Cat.builder().name("Ivar").age(4).build());
//            add(Cat.builder().name("Enzo").age(6).build());
//            add(Cat.builder().name("Finland").age(15).build());
//            add(Cat.builder().name("Robert").age(13).build());
//          }
//        };
//    catRepository.saveAll(cats);
//  }

  @Test
  void save_returnValidCatDto() {
    var catNew = Cat.builder().name("Neko").age(11).build();

    var catSaved = catRepository.save(catNew);

    catRepository.flush();

    assertThat(catSaved).isNotNull();
    assertThat(catSaved.getId()).isNotNull();
  }

  @Test
  void save_nameTooLong() {
    Cat catNew =
        Cat.builder()
            .name("Neko01234567890123456789012345678901234567890123456789")
            .age(11)
            .build();
    assertThatThrownBy(
            () -> {
              catRepository.save(catNew);
              catRepository.flush();
            })
        .isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void findTopByField_validQuantityAndFieldName_returnValidCats() {
    Integer quantity = 2;
    String fieldName = "age";

    List<Cat> cats = catRepository.findTopByField(quantity, fieldName);

    assertThat(cats).isNotNull();
    assertThat(cats.size()).isEqualTo(2);
    assertThat(cats.get(0).getAge()).isEqualTo(0);
    assertThat(cats.get(0).getAge()).isLessThanOrEqualTo(cats.get(1).getAge());
  }

  @Test
  void testFindCatsByName() {
    Page<Cat> page = catRepository.findAllByNameIsLikeIgnoreCase("%MIL%", null);

    assertThat(page.getContent().size()).isEqualTo(831);
  }

  /*
  Todo: test other methods
   */
}
