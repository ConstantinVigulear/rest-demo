package com.vigulear.restdemo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author : crme059
 * @created : 06-Dec-23, Wednesday
 */
@SpringBootTest
@AutoConfigureMockMvc
class CatControllerIntegrationTest {

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  @Value("${cat.property.cat-path}")
  String CAT_PATH;
  @Value("${cat.property.cat-path-id}")
  String CAT_PATH_ID;
  @Value("${cat.property.cat-path}" + "/total?fieldName={fieldName}")
  String CAT_PATH_TOTAL;
  @Value("${cat.property.cat-path}" + "/youngest")
  String CAT_PATH_YOUNGEST;
  @Value("${cat.property.cat-path}" + "/top?top={top}&fieldName={fieldName}")
  String CAT_PATH_TOP = CAT_PATH;
  @Value("${cat.property.cat-path}" + "/top3?fieldName={fieldName}")
  String CAT_PATH_TOP3;

  @Test
  void createCat() throws Exception {
    Cat cat = Cat.builder().name("Napoleon").age(11).build();
    String catJson = objectMapper.writeValueAsString(cat);

    mockMvc
        .perform(post(CAT_PATH).contentType(MediaType.APPLICATION_JSON).content(catJson))
        .andExpectAll(
            status().isCreated(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.name").value("Napoleon"),
            jsonPath("$.age").value(11));
  }

  @Test
  void getAllCats() throws Exception {
    String answer =
        mockMvc
            .perform(get(CAT_PATH))
            .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<CatDto> catDtos = objectMapper.readValue(answer, new TypeReference<>() {});

    assertThat(catDtos).isNotNull().isNotEmpty();
  }

  @Test
  void getCatById() throws Exception {
    Long id = 1001L;

    mockMvc
        .perform(get(CAT_PATH_ID, id))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.id").value(id))
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  @Test
  void getTopByField() throws Exception {
    int quantity = 6;
    String fieldName = "age";

    String response =
        mockMvc
            .perform(get(CAT_PATH_TOP, quantity, fieldName))
            .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<CatDto> catDtosResponse = objectMapper.readValue(response, new TypeReference<>() {});

    assertThat(catDtosResponse).isNotNull().isNotEmpty().size().isEqualTo(quantity);
    for (int i = 0; i < catDtosResponse.size() - 1; i++) {
      assertThat(catDtosResponse.get(i).getAge())
          .isLessThanOrEqualTo(catDtosResponse.get(i + 1).getAge());
    }
  }

  @Test
  void getTopThree() throws Exception {
    String fieldName = "name";

    String response =
        mockMvc
            .perform(get(CAT_PATH_TOP3, fieldName))
            .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<CatDto> catDtosResponse = objectMapper.readValue(response, new TypeReference<>() {});

    assertThat(catDtosResponse).isNotNull().isNotEmpty().size().isEqualTo(3);
    for (int i = 0; i < catDtosResponse.size() - 1; i++) {
      assertThat(catDtosResponse.get(i).getName())
          .isLessThanOrEqualTo(catDtosResponse.get(i + 1).getName());
    }
  }

  @Test
  void findTheYoungest() throws Exception {
    int smallestAge = 0;

    String response =
        mockMvc
            .perform(get(CAT_PATH_YOUNGEST))
            .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    CatDto catDto = objectMapper.readValue(response, CatDto.class);

    assertThat(catDto.getAge()).isEqualTo(smallestAge);
  }

  @Test
  void findTotalBy() throws Exception {
    String fieldName = "age";
    String response =
        mockMvc
            .perform(get(CAT_PATH_TOTAL, fieldName))
            .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(Integer.parseInt(response)).isGreaterThan(0);
  }
}
