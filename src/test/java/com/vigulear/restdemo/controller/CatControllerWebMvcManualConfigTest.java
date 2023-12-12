package com.vigulear.restdemo.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.service.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author : crme059
 * @created : 05-Dec-23, Tuesday
 */
@ExtendWith(MockitoExtension.class)
class CatControllerWebMvcManualConfigTest {
  @Mock private CatService catService;

  @InjectMocks private CatController catController;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  String CAT_PATH;

  @BeforeEach
  void setUp() {
    // throws error because spring cannot standaloneSetup(controller) without loading from application.properties
    mockMvc = MockMvcBuilders.standaloneSetup(catController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  public void createCat_returnsValidEntity() throws Exception {
    Cat cat = Cat.builder().id(1001L).name("Couscous").age(10).build();
    String catJson = objectMapper.writeValueAsString(cat);
    CatDto catDto = CatMapper.mapToCatDto(cat);
    String catDtoJson = objectMapper.writeValueAsString(catDto);

    when(catService.createCat(cat)).thenReturn(catDto);


    //    when(catService.createCat(cat)).thenReturn(catDto);
    given(catService.createCat(cat)).willReturn(catDto);

    mockMvc
            .perform(
                    post(CAT_PATH)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(catJson))
            .andExpectAll(
                    status().isCreated(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    content().json(catDtoJson),
                    jsonPath("$.id").value(cat.getId()),
                    jsonPath("$.name").value(cat.getName()),
                    jsonPath("$.age").value(cat.getAge()));

    verify(catService, times(1)).createCat(any());
  }

  /*
  Todo: The rest test are identical to one from CatControllerWebMvcTest
   */
}
