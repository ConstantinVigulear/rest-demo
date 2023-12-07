package com.vigulear.restdemo.controller;

import com.vigulear.restdemo.service.CatService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author : crme059
 * @created : 06-Dec-23, Wednesday
 */
@SpringBootTest
@AutoConfigureMockMvc
class CatControllerIntegrationTest {

  @Autowired MockMvc mockMvc;
  @Autowired CatService catService;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void createCat() {}

  @Test
  void getAllCats() {}

  @Test
  void getCatById() {}

  @Test
  void getTopByField() {}

  @Test
  void getTopThree() {}

  @Test
  void findTheYoungest() {}

  @Test
  void findTotalBy() {}
}
