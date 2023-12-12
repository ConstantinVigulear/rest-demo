package com.vigulear.restdemo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.InvalidValueException;
import com.vigulear.restdemo.exceptions.NotFoundException;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.service.CatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author : crme059
 * @created : 01-Dec-23, Friday
 */

// setups mockmvc and objectMapper, if no controller indicated, will bring up all
// controllers
@WebMvcTest(CatController.class)
class CatControllerWebMvcTest {

  @Autowired MockMvc mockMvc;

  // @MockBean integrates catService into Spring context
  @MockBean CatService catService;

  @Autowired ObjectMapper objectMapper;

  // ArgumentCaptor allows to verify if the required parameter was parsed correctly
  @Captor ArgumentCaptor<Cat> catArgumentCaptor;
  @Captor ArgumentCaptor<Long> longArgumentCaptor;
  @Captor ArgumentCaptor<String> stringArgumentCaptor;
  @Captor ArgumentCaptor<Integer> integerArgumentCaptor;

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
  @DisplayName("POST returns new catDto and Http 201 Created ")
  void createCat_returnsValidEntity() throws Exception {
    Cat cat = Cat.builder().id(1001L).name("Couscous").age(10).build();
    String catJson = objectMapper.writeValueAsString(cat);
    CatDto catDto = CatMapper.mapToCatDto(cat);
    String catDtoJson = objectMapper.writeValueAsString(catDto);

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

  @Test
  @DisplayName("PUT with valid Payload returns Http 200 Ok")
  void updateById_validPayload_returnsUpdatedCatDto() throws Exception {
    Cat catToUpdate = Cat.builder().id(1001L).name("Millefoglie").age(10).build();
    String catToUpdateJson = objectMapper.writeValueAsString(catToUpdate);
    CatDto catToUpdateDto = CatMapper.mapToCatDto(catToUpdate);
    String catToUpdateDtoJson = objectMapper.writeValueAsString(catToUpdateDto);

    when(catService.updateById(catToUpdate.getId(), catToUpdate)).thenReturn(catToUpdateDto);
    mockMvc
        .perform(
            put(CAT_PATH_ID, catToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(catToUpdateJson))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(catToUpdateDtoJson),
            jsonPath("$.name").value("Millefoglie"));

    verify(catService, times(1)).updateById(catToUpdate.getId(), catToUpdate);
  }

  @Test
  @DisplayName("PATCH with valid payload returns Http 204 No Content")
  void patchById_validPayload_returnsCoContent() throws Exception {
    Cat catToPatch = Cat.builder().id(1001L).name("Millefoglie").build();
    Cat patch = Cat.builder().name("Millefoglie").build();
    String patchJson = objectMapper.writeValueAsString(patch);

    doNothing().when(catService).patchById(catToPatch.getId(), patch);

    mockMvc
        .perform(
            patch(CAT_PATH_ID, catToPatch.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(patchJson))
        .andExpect(status().isNoContent());

    verify(catService, times(1))
        .patchById(longArgumentCaptor.capture(), catArgumentCaptor.capture());

    assertThat(patch.getName()).isEqualTo(catArgumentCaptor.getValue().getName());
    assertThat(catToPatch.getId()).isEqualTo(longArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "PUT with invalid id throws InvalidValueException Http 400 Bad Request")
  void updateById_invalidId_throwsInvalidValueException() throws Exception {
    Cat catToUpdate = Cat.builder().id(-1L).name("Millefoglie").age(10).build();
    String catToUpdateJson = objectMapper.writeValueAsString(catToUpdate);

    when(catService.updateById(catToUpdate.getId(), catToUpdate))
        .thenThrow(new InvalidValueException("There is no cat with id = " + catToUpdate.getId()));
    mockMvc
        .perform(
            put(CAT_PATH_ID, catToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(catToUpdateJson))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string("There is no cat with id = " + catToUpdate.getId()));

    verify(catService, times(1))
        .updateById(longArgumentCaptor.capture(), catArgumentCaptor.capture());

    assertThat(catToUpdate.getId()).isEqualTo(longArgumentCaptor.getValue());
    assertThat(catToUpdate).isEqualTo(catArgumentCaptor.getValue());
  }

  @Test
  @DisplayName("GET with valid id returns catDto and Http 201 Ok ")
  void getCatById_validPayload_returnCatDto() throws Exception {
    Cat cat = Cat.builder().id(1001L).name("Couscous").age(10).build();
    CatDto catDto = CatMapper.mapToCatDto(cat);
    String catDtoJson = objectMapper.writeValueAsString(catDto);

    when(catService.findById(cat.getId())).thenReturn(catDto);

    mockMvc
        .perform(get(CAT_PATH_ID, cat.getId()).contentType(MediaType.TEXT_PLAIN_VALUE))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(catDtoJson),
            jsonPath("$.id").value(cat.getId()),
            jsonPath("$.name").value(cat.getName()),
            jsonPath("$.age").value(cat.getAge()));

    verify(catService, times(1)).findById(longArgumentCaptor.capture());

    assertThat(cat.getId()).isEqualTo(longArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "GET with not found id throws NotFoundException and HTTP 404 Not Found")
  void getCatById_notFoundId_throwsNotFoundException() throws Exception {
    Long id = -1L;

    when(catService.findById(id)).thenThrow( new NotFoundException("Cat with id = '" + id + "' is not found"));

    mockMvc
        .perform(get(CAT_PATH_ID, id))
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string("Cat with id = '" + id + "' is not found"));

    verify(catService, times(1)).findById(longArgumentCaptor.capture());

    assertThat(id).isEqualTo(longArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "GET with invalid Payload throws InvalidValueException and Http 400 Bad Request")
  void getCatById_invalidPayload_throwsInvalidValueException() throws Exception {
    String id = "*";

    mockMvc
        .perform(get(CAT_PATH_ID, id))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string("Invalid value \"" + id + "\" for parameter"));

    verify(catService, times(0)).findById(any());
  }

  @Test
  @DisplayName("GET returns list of cat dtos and Http 200 Ok")
  void getAllCats() throws Exception {
    List<Cat> cats =
        List.of(
            Cat.builder().id(1001L).name("Couscous").age(0).build(),
            Cat.builder().id(1002L).name("Tiramisu").age(7).build());
    List<CatDto> catDtos = cats.stream().map(CatMapper::mapToCatDto).toList();
    String catDtosJson = objectMapper.writeValueAsString(catDtos);

    when(catService.findAll()).thenReturn(catDtos);

    mockMvc
        .perform(get(CAT_PATH))
        .andExpectAll(status().isOk(), content().json(catDtosJson), jsonPath("$.size()").value(2));
    verify(catService, times(1)).findAll();
  }

  @Test
  @DisplayName(
      "GET topByField with valid Payload returns list of cat dtos and Http 200 Ok")
  void getTopByField_validPayload_returnValidListOfCatDtos() throws Exception {
    Integer top = 2;
    String fieldName = "age";
    List<Cat> cats =
        List.of(
            Cat.builder().id(1001L).name("Couscous").age(0).build(),
            Cat.builder().id(1002L).name("Tiramisu").age(7).build());
    List<CatDto> catDtos = cats.stream().map(CatMapper::mapToCatDto).toList();

    when(catService.findTopByField(top, fieldName)).thenReturn(catDtos);

    mockMvc
        .perform(get(CAT_PATH + "/top?top={top}&fieldName={fieldName}", top, fieldName))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(objectMapper.writeValueAsString(catDtos)));
    verify(catService, times(1)).findTopByField(any(), any());
  }

  @Test
  @DisplayName(
      "GET topByField with invalid Top throws InvalidValueException and Http 400 Bad Request")
  void getTopByField_invalidTop_throwsInvalidValueException() throws Exception {
    Integer top = -2;
    String fieldName = "any";

    when(catService.findTopByField(top, fieldName)).thenThrow(new InvalidValueException("Invalid value \"" + top + "\" for parameter \"top\""));

    mockMvc
        .perform(get(CAT_PATH_TOP, top, fieldName))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string("Invalid value \"" + top + "\" for parameter \"top\""));

    verify(catService, times(1)).findTopByField(integerArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertThat(top).isEqualTo(integerArgumentCaptor.getValue());
    assertThat(fieldName).isEqualTo(stringArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "GET topByField with invalid fieldName throws InvalidValueException and Http 400 Bad Request")
  void getTopByField_invalidFieldName_throwsInvalidValueException() throws Exception {
    Integer top = 2;
    String fieldName = "invalid";

    when(catService.findTopByField(top, fieldName)).thenThrow(new InvalidValueException("No such field as '" + fieldName + "'"));

    mockMvc
        .perform(get(CAT_PATH_TOP, top, fieldName))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string("No such field as '" + fieldName + "'"));

    verify(catService, times(1)).findTopByField(integerArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertThat(top).isEqualTo(integerArgumentCaptor.getValue());
    assertThat(fieldName).isEqualTo(stringArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "GET top3ByField with valid fieldName returns list of cat dtos and Http 200 Ok")
  void getTopThree_validFieldName_returnValidListOfCatDtos() throws Exception {
    String fieldName = "age";
    List<Cat> cats =
        List.of(
            Cat.builder().id(1001L).name("Couscous").age(0).build(),
            Cat.builder().id(1002L).name("Tiramisu").age(7).build(),
            Cat.builder().id(1003L).name("Humus").age(1).build());
    List<CatDto> catDtos = cats.stream().map(CatMapper::mapToCatDto).toList();

    when(catService.findFirst3(fieldName)).thenReturn(catDtos);

    mockMvc
        .perform(get(CAT_PATH_TOP3, fieldName))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(objectMapper.writeValueAsString(catDtos)));
    verify(catService, times(1)).findFirst3(stringArgumentCaptor.capture());

    assertThat(fieldName).isEqualTo(stringArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "GET top3ByField with invalid fieldName throws InvalidValueException and Http 400 Bad Request")
  void getTopThree_invalidFieldName_throwsInvalidValueException() throws Exception {
    String fieldName = "diet";

    when(catService.findFirst3(fieldName)).thenThrow(new InvalidValueException("No such field as '" + fieldName + "'"));

    mockMvc
        .perform(get(CAT_PATH + "/top3?fieldName={fieldName}", fieldName))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string("No such field as '" + fieldName + "'"));

    verify(catService, times(1)).findFirst3(stringArgumentCaptor.capture());

    assertThat(fieldName).isEqualTo(stringArgumentCaptor.getValue());
  }

  @Test
  @DisplayName("GET youngest returns valid cat and Http 200 Ok")
  void findTheYoungest_returnValidCat() throws Exception {
    var cat = Cat.builder().id(1001L).name("Couscous").age(0).build();
    var catDto = CatMapper.mapToCatDto(cat);
    String catDtoJson = objectMapper.writeValueAsString(catDto);
    when(catService.findFirstByOrderByAge()).thenReturn(catDto);

    mockMvc
        .perform(get(CAT_PATH + "/youngest"))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(catDtoJson),
            jsonPath("$.id").value(cat.getId()),
            jsonPath("$.name").value(cat.getName()),
            jsonPath("$.age").value(cat.getAge()));
    verify(catService, times(1)).findFirstByOrderByAge();
  }

  @Test
  @DisplayName("GET youngest throws InvalidValueException and Http 400 Bad Request")
  void findTheYoungest_throwsInvalidValueException() throws Exception {

    when(catService.findFirstByOrderByAge()).thenThrow(new NotFoundException("There are no records in data base"));

    mockMvc
        .perform(get(CAT_PATH_YOUNGEST))
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string("There are no records in data base"));

    verify(catService, times(1)).findFirstByOrderByAge();
  }

  @Test
  @DisplayName(
      "GET totalByField with valid fieldName returns valid total and Http 200 Ok")
  void findTotalBy_validFieldName_returnValidTotal() throws Exception {
    String fieldName = "age";
    Integer expectedTotal = 123;

    when(catService.findTotalBy(fieldName)).thenReturn(expectedTotal);

    mockMvc
        .perform(get(CAT_PATH_TOTAL, fieldName))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(objectMapper.writeValueAsString(expectedTotal)));

    verify(catService, times(1)).findTotalBy(stringArgumentCaptor.capture());

    assertThat(fieldName).isEqualTo(stringArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "GET totalByField with invalid fieldName throws InvalidValueException and Http 400 Bad Request")
  void findTotalBy_invalidFieldName_throwsInvalidValueException() throws Exception {
    String fieldName = "name";

    when(catService.findTotalBy(fieldName)).thenThrow(new InvalidValueException("No such field as '" + fieldName + "'"));

    mockMvc
        .perform(get(CAT_PATH_TOTAL, fieldName))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string("No such field as '" + fieldName + "'"));

    verify(catService, times(1)).findTotalBy(stringArgumentCaptor.capture());

    assertThat(fieldName).isEqualTo(stringArgumentCaptor.getValue());
  }

  @Test
  @DisplayName("DELETE with valid id returns Http 204 No Content")
  void deleteById_validId_returnsNoContent() throws Exception {
    var catToDelete = Cat.builder().id(1L).build();

    doNothing().when(catService).deleteById(catToDelete.getId());

    mockMvc
        .perform(delete(CAT_PATH_ID, catToDelete.getId()))
        .andExpect(status().isNoContent());

    ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    verify(catService, times(1)).deleteById(longArgumentCaptor.capture());

    assertThat(catToDelete.getId()).isEqualTo(longArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "DELETE with invalid id throws InvalidValueException and Http 400 Bad Request")
  void deleteById_invalidId_throwsInvalidValueException() throws Exception {
    Long id = -1L;
    String errorMessage = "There is no cat with id = " + id;

    doThrow(new InvalidValueException("There is no cat with id = " + id))
        .when(catService)
        .deleteById(id);

    mockMvc
        .perform(delete(CAT_PATH_ID, id))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string(errorMessage));

    verify(catService, times(1)).deleteById(longArgumentCaptor.capture());
    assertThat(id).isEqualTo(longArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "DELETE with invalid id throws MethodArgumentTypeMismatchException and Http 400 Bad Request")
  void deleteById_invalidId_throwsMethodArgumentTypeMismatchException() throws Exception {
    String id = "id";
    String errorMessage = "Invalid value \"id\" for parameter";

    mockMvc
        .perform(delete(CAT_PATH_ID, id))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string(errorMessage));

    verify(catService, times(0)).deleteById(any());
  }
}
