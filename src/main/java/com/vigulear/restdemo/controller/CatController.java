package com.vigulear.restdemo.controller;

import com.vigulear.restdemo.dto.CatDTO;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.InvalidValueException;
import com.vigulear.restdemo.exceptions.NotFoundException;
import com.vigulear.restdemo.service.CatService;
import com.vigulear.restdemo.util.FieldUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@RestController
public class CatController {

  @Value("${cat.error.not-found}")
  private String NOT_FOUND;

  @Value("${cat.error.invalid-value-field}")
  private String INVALID_VALUE_FIELD;

  @Value("${cat.property.cat-path}")
  private String CAT_PATH;

  private final CatService catService;

  public CatController(CatService catService) {
    this.catService = catService;
  }

  @PostMapping("${cat.property.cat-path}")
  public ResponseEntity<CatDTO> createCat(@Validated @RequestBody CatDTO catDto) {
    CatDTO savedCatDTO = catService.createCat(catDto);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.add("Location", CAT_PATH + savedCatDTO.getId().toString());

    return new ResponseEntity<>(savedCatDTO, httpHeaders, HttpStatus.CREATED);
  }

  @GetMapping("${cat.property.cat-path-id}")
  public ResponseEntity<CatDTO> findById(@PathVariable("catId") UUID id) {
    CatDTO cat = catService.findById(id).orElse(null);

    if (cat == null) {
      throw new NotFoundException(String.format(NOT_FOUND, id));
    }

    return new ResponseEntity<>(cat, HttpStatus.OK);
  }

  @PutMapping("${cat.property.cat-path-id}")
  public ResponseEntity<CatDTO> updateById(
      @PathVariable("catId") UUID id, @Validated @RequestBody CatDTO catDto) {
    Optional<CatDTO> updatedCat = catService.updateById(id, catDto);

    if (updatedCat.isEmpty()) {
      throw new NotFoundException(String.format(NOT_FOUND, id));
    }

    return new ResponseEntity<>(updatedCat.get(), HttpStatus.OK);
  }

  @PatchMapping("${cat.property.cat-path-id}")
  public ResponseEntity<CatDTO> patchById(
      @PathVariable("catId") UUID id, @RequestBody CatDTO catDto) {
    Optional<CatDTO> patchedCat = catService.patchById(id, catDto);

    if (patchedCat.isEmpty()) {
      throw new NotFoundException(String.format(NOT_FOUND, id));
    }

    return new ResponseEntity<>(patchedCat.get(), HttpStatus.OK);
  }

  @GetMapping("${cat.property.cat-path}")
  public ResponseEntity<List<CatDTO>> findAllCats() {
    List<CatDTO> cats = catService.findAll();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<>(cats, httpHeaders, HttpStatus.OK);
  }

  @GetMapping("${cat.property.cat-path}" + "/top")
  public ResponseEntity<List<CatDTO>> findTopByField(
      @RequestParam("top") Integer quantity, @RequestParam("fieldName") String fieldName)
      throws InvalidValueException {

    if (quantity <= 0)
      throw new InvalidValueException("Invalid value \"" + quantity + "\" for parameter \"top\"");

    Field field = FieldUtility.getCriteriaFieldForClass(fieldName, Cat.class);

    if (FieldUtility.isFieldValid(field)) {
      List<CatDTO> queriedCats = catService.findTopByField(quantity, fieldName);
      return ResponseEntity.ok(queriedCats);
    } else {
      throw new InvalidValueException(String.format(INVALID_VALUE_FIELD, fieldName));
    }
  }

  @GetMapping("${cat.property.cat-path}" + "/top3")
  public ResponseEntity<List<CatDTO>> findTopThree(@RequestParam("fieldName") String fieldName)
      throws InvalidValueException {

    Field field = FieldUtility.getCriteriaFieldForClass(fieldName, Cat.class);

    if (FieldUtility.isFieldValid(field)) {
      return ResponseEntity.ok(catService.findFirst3(fieldName));
    } else {
      throw new InvalidValueException(String.format(INVALID_VALUE_FIELD, fieldName));
    }
  }

  @GetMapping("${cat.property.cat-path}" + "/youngest")
  public ResponseEntity<CatDTO> findTheYoungest() {
    Optional<CatDTO> youngestCat = catService.findFirstByOrderByAge();

    if (youngestCat.isEmpty()) {
      throw new NotFoundException("There are no records in database");
    }

    return new ResponseEntity<>(youngestCat.get(), HttpStatus.OK);
  }

  @GetMapping("${cat.property.cat-path}" + "/total")
  public ResponseEntity<Integer> findTotalBy(@RequestParam String fieldName)
      throws InvalidValueException {

    Field field = FieldUtility.getCriteriaFieldForClass(fieldName, Cat.class);
    boolean isFieldValidForTotal =
        FieldUtility.isFieldValid(field) && FieldUtility.isFieldCountable(field);

    if (isFieldValidForTotal) {
      return ResponseEntity.ok(catService.findTotalBy(fieldName));
    } else {
      throw new InvalidValueException(String.format(INVALID_VALUE_FIELD, fieldName));
    }
  }

  @DeleteMapping("${cat.property.cat-path-id}")
  public ResponseEntity<?> deleteById(@PathVariable("catId") UUID id) {
    if (!catService.deleteById(id)) {
      throw new NotFoundException(String.format(NOT_FOUND, id));
    }
    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
  }
}
