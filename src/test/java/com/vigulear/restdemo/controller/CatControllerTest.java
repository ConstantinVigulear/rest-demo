package com.vigulear.restdemo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.vigulear.restdemo.dto.CatDTO;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.NotFoundException;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.mapper.CatMapperImpl;
import com.vigulear.restdemo.service.CatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author : crme059
 * @created : 01-Dec-23, Friday
 */

// setups mockmvc and objectMapper, if no controller indicated, will bring up all
// controllers
@WebMvcTest(CatController.class)
class CatControllerTest {

  @Autowired MockMvc mockMvc;

  // @MockBean integrates catService into Spring context
  @MockBean CatService catService;

  @Autowired ObjectMapper objectMapper;
  CatMapper catMapper = new CatMapperImpl();

  // ArgumentCaptor allows to verify if the required parameter was parsed correctly
  @Captor ArgumentCaptor<CatDTO> catDTOArgumentCaptor;
  @Captor ArgumentCaptor<UUID> uuidArgumentCaptor;
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
  @DisplayName("POST returns new catDto and Http 201 CREATED ")
  void createCat_returnsValidEntity() throws Exception {
    CatDTO catDTO =
        CatDTO.builder()
            .id(UUID.randomUUID())
            .name("Humus")
            .age(1)
            .version(0)
            .createdOn(LocalDateTime.now())
            .updatedOn(LocalDateTime.now())
            .build();
    String catDTOJson = objectMapper.writeValueAsString(catDTO);

    given(catService.createCat(catDTO)).willReturn(catDTO);

    mockMvc
        .perform(
            post(CAT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(catDTOJson))
        .andExpectAll(
            status().isCreated(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(catDTOJson),
            jsonPath("$.id").value(catDTO.getId().toString()),
            jsonPath("$.name").value(catDTO.getName()),
            jsonPath("$.age").value(catDTO.getAge()));

    verify(catService, times(1)).createCat(any());
  }

  @Test
  @DisplayName(
      "POST with empty payload throws MethodArgumentNotValidException and Http 400 BAD REQUEST")
  void createCat_withEmptyPayload_throws() throws Exception {
    CatDTO catDTO = CatDTO.builder().build();
    String catDTOJson = objectMapper.writeValueAsString(catDTO);

    given(catService.createCat(catDTO)).willReturn(catDTO);

    mockMvc
        .perform(
            post(CAT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(catDTOJson))
        .andExpectAll(status().isBadRequest(), jsonPath("$.length()").value(3));
  }

  @Test
  @DisplayName("PUT with valid Payload returns Http 200 Ok")
  void updateById_validPayload_returnsUpdatedCatDto() throws Exception {
    CatDTO catToUpdateDto =
        CatDTO.builder().id(UUID.randomUUID()).name("Millefoglie").age(10).build();
    String catToUpdateDtoJson = objectMapper.writeValueAsString(catToUpdateDto);

    when(catService.updateById(catToUpdateDto.getId(), catToUpdateDto))
        .thenReturn(Optional.of(catToUpdateDto));
    mockMvc
        .perform(
            put(CAT_PATH_ID, catToUpdateDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(catToUpdateDtoJson))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(catToUpdateDtoJson),
            jsonPath("$.name").value("Millefoglie"));

    verify(catService, times(1))
        .updateById(uuidArgumentCaptor.capture(), catDTOArgumentCaptor.capture());

    assertThat(catToUpdateDto.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    assertThat(catToUpdateDto).isEqualTo(catDTOArgumentCaptor.getValue());
  }

  @Test
  @DisplayName("PUT with invalid id throws InvalidValueException Http 400 Bad Request")
  void updateById_invalidId_throwsInvalidValueException() throws Exception {
    CatDTO catToUpdateDto =
        CatDTO.builder().id(UUID.randomUUID()).name("Millefoglie").age(10).build();
    String catToUpdateDtoJson = objectMapper.writeValueAsString(catToUpdateDto);
    String errorMessage = "There is no cat with id = " + catToUpdateDto.getId();

    when(catService.updateById(catToUpdateDto.getId(), catToUpdateDto))
        .thenThrow(new NotFoundException(errorMessage));

    mockMvc
        .perform(
            put(CAT_PATH_ID, catToUpdateDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(catToUpdateDtoJson))
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string(errorMessage));

    verify(catService, times(1))
        .updateById(uuidArgumentCaptor.capture(), catDTOArgumentCaptor.capture());

    assertThat(catToUpdateDto.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    assertThat(catToUpdateDto).isEqualTo(catDTOArgumentCaptor.getValue());
  }

  @Test
  @DisplayName("PUT with empty blank name MethodArgumentNotValidException and Http 400 BAD REQUEST")
  void updateById_withBlankName_throws() throws Exception {
    CatDTO catDTO = CatDTO.builder().id(UUID.randomUUID()).name("").age(11).build();
    String catDTOJson = objectMapper.writeValueAsString(catDTO);

    given(catService.updateById(any(), any())).willReturn(Optional.of(catDTO));

    mockMvc
        .perform(
            put(CAT_PATH_ID, catDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(catDTOJson))
        .andExpectAll(status().isBadRequest(), jsonPath("$.length()").value(1));
  }

  @Test
  @DisplayName("PATCH with valid payload returns Http 200 OK")
  void patchById_validPayload_returnsPatched() throws Exception {
    CatDTO catToPatchDTO = CatDTO.builder().id(UUID.randomUUID()).name("Millefoglie").age(11).build();
    CatDTO patch = CatDTO.builder().name("Humus").build();
    String patchJson = objectMapper.writeValueAsString(patch);

    CatDTO patchedCatDTO = CatDTO.builder().id(catToPatchDTO.getId()).name(patch.getName()).build();
    when(catService.patchById(catToPatchDTO.getId(), patch)).thenReturn(Optional.of(patchedCatDTO));

    mockMvc
        .perform(
            patch(CAT_PATH_ID, catToPatchDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(patchJson))
        .andExpect(status().isOk());

    verify(catService, times(1))
        .patchById(uuidArgumentCaptor.capture(), catDTOArgumentCaptor.capture());

    assertThat(patch.getName()).isEqualTo(catDTOArgumentCaptor.getValue().getName());
    assertThat(catToPatchDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  @DisplayName("GET with valid id returns catDto and Http 201 Ok ")
  void getCatById_validPayload_returnCatDto() throws Exception {
    CatDTO catDTO = CatDTO.builder().id(UUID.randomUUID()).name("Couscous").age(10).build();
    String catDTOJson = objectMapper.writeValueAsString(catDTO);

    when(catService.findById(catDTO.getId())).thenReturn(Optional.of(catDTO));

    mockMvc
        .perform(get(CAT_PATH_ID, catDTO.getId()).contentType(MediaType.TEXT_PLAIN_VALUE))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(catDTOJson),
            jsonPath("$.id").value(catDTO.getId().toString()),
            jsonPath("$.name").value(catDTO.getName()),
            jsonPath("$.age").value(catDTO.getAge()));

    verify(catService, times(1)).findById(uuidArgumentCaptor.capture());

    assertThat(catDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  @DisplayName("GET with not found id throws NotFoundException and HTTP 404 Not Found")
  void getCatById_notFoundId_throwsNotFoundException() throws Exception {
    UUID uuid = UUID.randomUUID();

    when(catService.findById(uuid))
        .thenThrow(new NotFoundException("Cat with id = '" + uuid + "' is not found"));
    mockMvc
        .perform(get(CAT_PATH_ID, uuid))
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string("Cat with id = '" + uuid + "' is not found"));

    verify(catService, times(1)).findById(uuidArgumentCaptor.capture());

    assertThat(uuid).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  @DisplayName("GET with invalid id throws InvalidValueException and Http 400 Bad Request")
  void getCatById_invalidId_throwsInvalidValueException() throws Exception {
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
            Cat.builder().name("Couscous").age(0).build(),
            Cat.builder().name("Tiramisu").age(7).build());
    List<CatDTO> catDTOS = cats.stream().map(catMapper::catToCatDto).toList();
    String catDtosJson = objectMapper.writeValueAsString(catDTOS);

    when(catService.findAll()).thenReturn(catDTOS);

    mockMvc
        .perform(get(CAT_PATH))
        .andExpectAll(status().isOk(), content().json(catDtosJson), jsonPath("$.size()").value(2));
    verify(catService, times(1)).findAll();
  }

  @Test
  @DisplayName("GET topByField with valid Payload returns list of cat dtos and Http 200 Ok")
  void getTopByField_validPayload_returnValidListOfCatDtos() throws Exception {
    Integer top = 2;
    String fieldName = "age";
    List<CatDTO> catDTOs =
        List.of(
            CatDTO.builder().name("Couscous").age(0).build(),
            CatDTO.builder().name("Tiramisu").age(7).build());

    when(catService.findTopByField(top, fieldName)).thenReturn(catDTOs);

    mockMvc
        .perform(get(CAT_PATH + "/top?top={top}&fieldName={fieldName}", top, fieldName))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(objectMapper.writeValueAsString(catDTOs)));
    verify(catService, times(1))
        .findTopByField(integerArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertThat(top).isEqualTo(integerArgumentCaptor.getValue());
    assertThat(fieldName).isEqualTo(stringArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "GET topByField with invalid Top throws InvalidValueException and Http 400 Bad Request")
  void getTopByField_invalidTop_throwsInvalidValueException() throws Exception {
    Integer top = -2;
    String fieldName = "any";
    String errorMessage = "Invalid value \"" + top + "\" for parameter \"top\"";

    mockMvc
        .perform(get(CAT_PATH_TOP, top, fieldName))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string(errorMessage));

    verify(catService, times(0)).findTopByField(any(), any());
  }

  @Test
  @DisplayName(
      "GET topByField with invalid fieldName throws InvalidValueException and Http 400 Bad Request")
  void getTopByField_invalidFieldName_throwsInvalidValueException() throws Exception {
    Integer top = 2;
    String fieldName = "invalid";
    String errorMessage = String.format("No such field as '%s'", fieldName);

    mockMvc
        .perform(get(CAT_PATH_TOP, top, fieldName))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string(errorMessage));

    verify(catService, times(0)).findTopByField(any(), any());
  }

  @Test
  @DisplayName("GET top3ByField with valid fieldName returns list of cat dtos and Http 200 Ok")
  void getTopThree_validFieldName_returnValidListOfCatDtos() throws Exception {
    String fieldName = "age";
    List<CatDTO> catDTOs =
        List.of(
            CatDTO.builder().name("Couscous").age(0).build(),
            CatDTO.builder().name("Tiramisu").age(7).build(),
            CatDTO.builder().name("Humus").age(1).build());

    when(catService.findFirst3(fieldName)).thenReturn(catDTOs);

    mockMvc
        .perform(get(CAT_PATH_TOP3, fieldName))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(objectMapper.writeValueAsString(catDTOs)));
    verify(catService, times(1)).findFirst3(stringArgumentCaptor.capture());

    assertThat(fieldName).isEqualTo(stringArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "GET top3ByField with invalid fieldName throws InvalidValueException and Http 400 Bad Request")
  void getTopThree_invalidFieldName_throwsInvalidValueException() throws Exception {
    String fieldName = "diet";
    String errorMessage = String.format("No such field as '%s'", fieldName);

    mockMvc
        .perform(get(CAT_PATH + "/top3?fieldName={fieldName}", fieldName))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string(errorMessage));

    verify(catService, times(0)).findFirst3(any());
  }

  @Test
  @DisplayName("GET youngest returns valid cat and Http 200 Ok")
  void findTheYoungest_returnValidCat() throws Exception {
    var catDTO = CatDTO.builder().id(UUID.randomUUID()).name("Couscous").age(0).build();
    String catDtoJson = objectMapper.writeValueAsString(catDTO);
    when(catService.findFirstByOrderByAge()).thenReturn(Optional.of(catDTO));

    mockMvc
        .perform(get(CAT_PATH + "/youngest"))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(catDtoJson),
            jsonPath("$.id").value(catDTO.getId().toString()),
            jsonPath("$.name").value(catDTO.getName()),
            jsonPath("$.age").value(catDTO.getAge()));
    verify(catService, times(1)).findFirstByOrderByAge();
  }

  @Test
  @DisplayName("GET youngest throws InvalidValueException and Http 400 Bad Request")
  void findTheYoungest_throwsInvalidValueException() throws Exception {

    when(catService.findFirstByOrderByAge())
        .thenThrow(new NotFoundException("There are no records in data base"));

    mockMvc
        .perform(get(CAT_PATH_YOUNGEST))
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string("There are no records in data base"));

    verify(catService, times(1)).findFirstByOrderByAge();
  }

  @Test
  @DisplayName("GET totalByField with valid fieldName returns valid total and Http 200 Ok")
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
    String errorMessage = String.format("No such field as '%s'", fieldName);

    mockMvc
        .perform(get(CAT_PATH_TOTAL, fieldName))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"),
            content().string(errorMessage));

    verify(catService, times(0)).findTotalBy(any());
  }

  @Test
  @DisplayName("DELETE with valid id returns Http 204 No Content")
  void deleteById_validId_returnsNoContent() throws Exception {
    var catToDeleteDTO = CatDTO.builder().id(UUID.randomUUID()).build();

    when(catService.deleteById(catToDeleteDTO.getId())).thenReturn(true);

    mockMvc.perform(delete(CAT_PATH_ID, catToDeleteDTO.getId())).andExpect(status().isNoContent());

    verify(catService, times(1)).deleteById(uuidArgumentCaptor.capture());

    assertThat(catToDeleteDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
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
