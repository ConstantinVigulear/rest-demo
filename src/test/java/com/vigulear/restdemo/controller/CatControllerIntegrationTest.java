package com.vigulear.restdemo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Test
  void createCat() throws Exception {
    Cat cat = Cat.builder().name("Napoleon").age(11).build();
    String catJson = objectMapper.writeValueAsString(cat);

    mockMvc
        .perform(post("/cat/create").contentType(MediaType.APPLICATION_JSON).content(catJson))
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
            .perform(get("/cat"))
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
        .perform(get("/cat/{id}", id))
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
            .perform(get("/cat/?top={top}&fieldName={fieldName}", quantity, fieldName))
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
            .perform(get("/cat/top3?fieldName={fieldName}", fieldName))
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
            .perform(get("/cat/youngest"))
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
            .perform(get("/cat/total?fieldName={fieldName}", fieldName))
            .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(Integer.parseInt(response)).isGreaterThan(0);
  }
}
