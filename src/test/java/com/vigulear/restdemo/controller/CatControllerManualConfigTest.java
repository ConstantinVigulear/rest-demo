package com.vigulear.restdemo.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vigulear.restdemo.dto.CatDTO;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.mapper.CatMapperImpl;
import com.vigulear.restdemo.service.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author : crme059
 * @created : 05-Dec-23, Tuesday
 */
@ExtendWith(MockitoExtension.class)
class CatControllerManualConfigTest {
  @Mock CatService catService;
  @InjectMocks CatController catController;

  MockMvc mockMvc;
  ObjectMapper objectMapper;
  CatMapper catMapper;
  String CAT_PATH = "/api/v1/cat";
  String CAT_PATH_ID = "/api/v1/cat/{catId}";

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(catController)
            .addPlaceholderValue("cat.property.cat-path", CAT_PATH)
            .addPlaceholderValue("cat.property.cat-path-id", CAT_PATH_ID)
            .build();
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    catMapper = new CatMapperImpl(); // becomes available only after compilation
  }

  @Disabled
  @Test()
  public void createCat_returnsValidEntity() throws Exception {
    Cat cat =
        Cat.builder()
            .id(UUID.randomUUID())
            .name("Humus")
            .age(1)
            .version(0)
            .createdOn(LocalDateTime.now())
            .updatedOn(LocalDateTime.now())
            .build();

    CatDTO catDTO = catMapper.catToCatDto(cat);
    String catDTOJson = objectMapper.writeValueAsString(catDTO);

    given(catService.createCat(catDTO)).willReturn(catDTO);

    mockMvc
        .perform(
            post(CAT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(catDTO)))
        .andExpectAll(
            status().isCreated(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(catDTOJson),
            jsonPath("$.id").value(catDTO.getId().toString()),
            jsonPath("$.name").value(catDTO.getName()),
            jsonPath("$.age").value(catDTO.getAge()));

    verify(catService, times(1)).createCat(any());
  }

  /*
  Todo: The rest test are identical to one from CatControllerWebMvcTest
   */
}
