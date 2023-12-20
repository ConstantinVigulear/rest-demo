package com.vigulear.restdemo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.InvalidValueException;
import com.vigulear.restdemo.exceptions.NotFoundException;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.model.CatDTO;
import com.vigulear.restdemo.repository.CatRepository;
import com.vigulear.restdemo.service.CatService;
import jakarta.transaction.Transactional;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author : crme059
 * @created : 06-Dec-23, Wednesday
 */
@SpringBootTest
class CatControllerIT {

  @Autowired CatController catController;
  @Autowired CatService catService;
  @Autowired CatRepository catRepository;
  @Autowired CatMapper catMapper;
  @Autowired ObjectMapper objectMapper;
  @Autowired WebApplicationContext wac;

  @Value("${cat.property.cat-path-id}")
  String CAT_PATH_ID;

  @Value("${cat.property.cat-path}")
  String CAT_PATH;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  void testFindAllCats() {
    ResponseEntity<Page<CatDTO>> responseEntity = catController.findAllCats(null, null, 1, 25);

    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isNotEmpty();
    assertThat(responseEntity.getBody().getContent().size()).isEqualTo(25);
    assertThat(responseEntity.getBody().getTotalElements()).isEqualTo(100011);
  }

  @Test
  void testFindAllCatsMaximumPageSize() {
    Page<CatDTO> catDTOPage = catController.findAllCats(null, null, null, 1500000).getBody();

    assertThat(catDTOPage).isNotNull();
    assertThat(catDTOPage.getContent()).isNotNull();
    assertThat(catDTOPage.getContent().size()).isEqualTo(100000);
  }

  @Test
  void testFindAllCatsByNameAndAgePage1() throws Exception {
    mockMvc
            .perform(
                    get(CAT_PATH)
                            .queryParam("catName", "m")
                            .queryParam("catAge", "11")
                            .queryParam("pageNumber", "1")
                            .queryParam("pageSize", "50"))
            .andExpectAll(
                    status().isOk(),
                    jsonPath("$.content.size()").value(50),
                    jsonPath("$.totalPages").value(16),
                    jsonPath("$.totalElements").value(775));
  }

  @Test
  void testFindAllCatsByNameAndAgePage16() throws Exception {
    mockMvc
        .perform(
            get(CAT_PATH)
                .queryParam("catName", "m")
                .queryParam("catAge", "11")
                .queryParam("pageNumber", "16")
                .queryParam("pageSize", "50"))
        .andExpectAll(
                status().isOk(),
                jsonPath("$.content.size()").value(25),
                jsonPath("$.totalPages").value(16),
                jsonPath("$.totalElements").value(775));
  }

  @Test
  void testFindAllCatsByName() throws Exception {
    mockMvc
        .perform(
            get(CAT_PATH)
                .queryParam("catName", "kel")
                .queryParam("pageSize", "1000000"))
        .andExpectAll(status().isOk(), jsonPath("$.content.size()").value(278));
  }

  @Test
  void testFindAllCatsByAge() throws Exception {
    mockMvc
        .perform(get(CAT_PATH).queryParam("catAge", String.valueOf(7)))
        .andExpectAll(status().isOk(), jsonPath("$.totalElements").value(5058));
  }

  @Test
  void testFindAllCatsByNameAndAge() throws Exception {
    mockMvc
        .perform(get(CAT_PATH).queryParam("catAge", String.valueOf(8)).queryParam("catName", "mi"))
        .andExpectAll(status().isOk(), jsonPath("$.totalElements").value(154));
  }

  @Rollback
  @Transactional
  @Test
  void createCat() {
    CatDTO catDto = CatDTO.builder().name("Napoleon").age(11).build();
    long initialDatabaseSize = catRepository.count();

    ResponseEntity<CatDTO> responseEntity = catController.createCat(catDto);
    long postDatabaseSize = catRepository.count();

    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isNotNull().isEqualTo(HttpStatus.CREATED);
    assertThat(initialDatabaseSize + 1).isEqualTo(postDatabaseSize);

    UUID savedUUID = Objects.requireNonNull(responseEntity.getBody()).getId();

    Cat catFound = catRepository.findById(savedUUID).orElse(null);
    assertThat(catFound).isNotNull();
  }

  @Rollback
  @Transactional
  @Test
  void findAllCats_returnsEmptyList() {
    catRepository.deleteAll();
    Page<CatDTO> catDTOS = catController.findAllCats(null, null, 1, 25).getBody();

    assertThat(catDTOS).isNotNull().isEmpty();
  }

  @Rollback
  @Transactional
  @Test
  void findCatById() {
    var cat = catRepository.save(Cat.builder().build());

    ResponseEntity<CatDTO> responseEntity = catController.findById(cat.getId());

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isNotNull().isEqualTo(catMapper.catToCatDto(cat));
  }

  @Test
  void findCatById_throwsNotFound() {
    assertThatThrownBy(() -> catController.findById(UUID.randomUUID()))
        .isInstanceOf(NotFoundException.class);
  }

  @Test
  void findTopByField() throws InvalidValueException {
    int quantity = 6;
    String fieldName = "age";

    List<CatDTO> catDTOS = catController.findTopByField(quantity, fieldName).getBody();

    assertThat(catDTOS).isNotNull().isNotEmpty().size().isEqualTo(quantity);
    for (int i = 0; i < catDTOS.size() - 1; i++) {
      assertThat(catDTOS.get(i).getAge()).isLessThanOrEqualTo(catDTOS.get(i + 1).getAge());
    }
  }

  @Test
  void findTopByField_invalidFieldName_throwsInvalidValueException() {
    int quantity = 6;
    String fieldName = "ag";
    String errorMessage = String.format("No such field as '%s'", fieldName);

    assertThatThrownBy(() -> catController.findTopByField(quantity, fieldName))
        .isInstanceOf(InvalidValueException.class)
        .hasMessage(errorMessage);
  }

  @Test
  void findTopByField_invalidQuantity_throwsInvalidValueException() {
    int quantity = -6;
    String fieldName = "ag";
    String errorMessage = "Invalid value \"" + quantity + "\" for parameter \"top\"";

    assertThatThrownBy(() -> catController.findTopByField(quantity, fieldName))
        .isInstanceOf(InvalidValueException.class)
        .hasMessage(errorMessage);
  }

  @Test
  void findTopThree() throws InvalidValueException {
    String fieldName = "name";

    List<CatDTO> catDtosResponse = catController.findTop3(fieldName).getBody();

    assertThat(catDtosResponse).isNotNull().isNotEmpty().size().isEqualTo(3);
    for (int i = 0; i < catDtosResponse.size() - 1; i++) {
      assertThat(catDtosResponse.get(i).getName())
          .isLessThanOrEqualTo(catDtosResponse.get(i + 1).getName());
    }
  }

  @Test
  void findTopThree_invalidFieldName_throwsInvalidValueException() {
    String fieldName = "111";
    String errorMessage = String.format("No such field as '%s'", fieldName);

    assertThatThrownBy(() -> catController.findTop3(fieldName))
        .isInstanceOf(InvalidValueException.class)
        .hasMessage(errorMessage);
  }

  @Test
  void findTheYoungest() {
    CatDTO catDto = catController.findTheYoungest().getBody();
    Page<CatDTO> catDTOS = catService.findAll(null, null, 1, 25);

    assertThat(catDto).isNotNull();
    catDTOS.forEach(cat -> assertThat(cat.getAge()).isGreaterThanOrEqualTo(catDto.getAge()));
  }

  @Rollback
  @Transactional
  @Test
  void findTheYoungest_emptyDataBase_throwsNotFoundException() {
    String errorMessage = "There are no records in database";

    catRepository.deleteAll();

    assertThatThrownBy(() -> catController.findTheYoungest())
        .isInstanceOf(NotFoundException.class)
        .hasMessage(errorMessage);
  }

  @Test
  void findTotalBy() throws InvalidValueException {
    String fieldName = "age";

    Integer total = catController.findTotalBy(fieldName).getBody();

    assertThat(total).isNotNull().isGreaterThan(0);
  }

  @Test
  void findTotalBy_invalidFieldName_throwsInvalidValueException() {
    String fieldName = "ag";
    String errorMessage = String.format("No such field as '%s'", fieldName);

    assertThatThrownBy(() -> catController.findTotalBy(fieldName))
        .isInstanceOf(InvalidValueException.class)
        .hasMessage(errorMessage);
  }

  @Rollback
  @Transactional
  @Test
  void updateById() {
    Cat cat = catRepository.findAll().getFirst();
    CatDTO catDTO = catMapper.catToCatDto(cat);
    catDTO.setAge(33);

    ResponseEntity<CatDTO> responseEntity = catController.updateById(cat.getId(), catDTO);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(responseEntity.getBody()).getAge())
        .isEqualTo(catDTO.getAge());
  }

  @Test
  void updateById_throwsNotFoundException() {
    UUID id = UUID.randomUUID();
    assertThatThrownBy(() -> catController.updateById(id, CatDTO.builder().build()))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("There is no cat with id = " + id);
  }

  @Rollback
  @Transactional
  @Test
  void deleteById() {
    Cat cat = catRepository.findAll().getFirst();

    ResponseEntity<?> responseEntity = catController.deleteById(cat.getId());

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(catRepository.findById(cat.getId())).isEmpty();
  }

  @Test
  void deleteById_throwsNotFoundException() {
    UUID id = UUID.randomUUID();
    assertThatThrownBy(() -> catController.deleteById(id))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("There is no cat with id = " + id);
  }

  @Test
  @DisplayName("PATCH with invalid payload returns Http 400 Bad Request")
  void patchById_badName() throws Exception {
    Cat cat = catRepository.findAll().getFirst();

    Map<String, Object> catMap = new HashMap<>();
    catMap.put("name", "catName012345678901234567890123456789012345678901234567890123456789");
    mockMvc
        .perform(
            patch(CAT_PATH_ID, cat.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(catMap)))
        .andExpectAll(status().isBadRequest(), jsonPath("$.length()").value(1));
  }
}
