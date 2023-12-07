package com.vigulear.restdemo.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : crme059
 * @created : 07-Dec-23, Thursday
 */
class CatTest {

  @BeforeEach
  void setUp() {}

  @Test
  void testHashCode() {
      var cat1 = Cat.builder().name("Couscous").build();
      var cat2 = Cat.builder().name("Couscous").build();
      var cat3 = Cat.builder().name("Tiramisu").build();

      assertThat(cat1.hashCode()).isEqualTo(cat2.hashCode());
      assertThat(cat1.hashCode()).isNotEqualTo(cat3.hashCode());
  }

  @Test
  void sound() {
      var cat1 = Cat.builder().name("Couscous").build();
      String sound = "Meow";

      assertThat(sound).isEqualTo(cat1.sound());
  }
}
