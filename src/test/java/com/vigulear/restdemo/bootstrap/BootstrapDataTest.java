package com.vigulear.restdemo.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import com.vigulear.restdemo.repository.CatRepository;
import com.vigulear.restdemo.repository.CustomerRepository;
import com.vigulear.restdemo.service.CatCsvService;
import com.vigulear.restdemo.service.impl.CatCsvServiceImpl;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * @author : crme059, Constantin Vigulear
 */
@DataJpaTest
@Import(CatCsvServiceImpl.class)
class BootstrapDataTest {

  @Autowired CatRepository catRepository;
  @Autowired
  CustomerRepository customerRepository;
  @Autowired CatCsvService catCsvService;
  BootstrapData bootstrapData;

  @BeforeEach
  void setUp() {
    bootstrapData = new BootstrapData(catRepository, catCsvService, customerRepository);
  }

  @Test
  void run() throws FileNotFoundException {
    bootstrapData.run();

    assertThat(catRepository.count()).isEqualTo(100011L);
  }
}
