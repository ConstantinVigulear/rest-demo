package com.vigulear.restdemo.controller;

import java.lang.reflect.Field;
import java.util.List;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.InvalidValueException;
import com.vigulear.restdemo.service.CatService;
import com.vigulear.restdemo.util.FieldUtility;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@RestController
@RequestMapping("/cat")
public class CatController {

  private final CatService catService;

  public CatController(CatService catService) {
    this.catService = catService;
  }

  @PostMapping("/create")
  public ResponseEntity<CatDto> createCat(@RequestBody Cat cat) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<>(catService.createCat(cat), httpHeaders ,HttpStatus.CREATED);
  }

  @GetMapping("")
  public ResponseEntity<List<CatDto>> getAllCats() {
    List<CatDto> cats = catService.findAll();
    return new ResponseEntity<>(cats, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CatDto> getCatById(@PathVariable Long id) throws Exception {
    CatDto cat = catService.findById(id);

    if (cat == null) {
      throw new InvalidValueException("Cat with id = '" + id + "' is not found");
    }

    return new ResponseEntity<>(cat, HttpStatus.OK);
  }

  /*
  http://localhost:8080/cats/?top=2&fieldName=name
   */
  @GetMapping("/")
  public ResponseEntity<List<CatDto>> getTopByField(
          @RequestParam("top") Integer quantity, @RequestParam("fieldName") String fieldName)
      throws Exception {

    if (quantity <= 0)
      throw new InvalidValueException("Invalid value \"" + quantity + "\" for parameter \"top\"");

    Field field = FieldUtility.getCriteriaFieldForClass(fieldName, Cat.class);

    if (FieldUtility.isFieldValid(field)) {
      List<CatDto> queriedCats = catService.findTopByField(quantity, fieldName);
      return ResponseEntity.ok(queriedCats);
    } else {
      throw new InvalidValueException("No such field as '" + fieldName + "'");
    }
  }

  /*
  http://localhost:8080/cats/top3?fieldName=age
   */
  @GetMapping("/top3")
  public ResponseEntity<List<CatDto>> getTopThree(@RequestParam("fieldName") String fieldName)
      throws Exception {
    Field field = FieldUtility.getCriteriaFieldForClass(fieldName, Cat.class);
    if (FieldUtility.isFieldValid(field)) {
      Sort sort = Sort.by(fieldName).ascending();
      return ResponseEntity.ok(catService.findFirst3(sort));
    } else {
      throw new InvalidValueException("No such field as '" + fieldName + "'");
    }
  }

  @GetMapping("/youngest")
  public ResponseEntity<CatDto> findTheYoungest() throws Exception {
    CatDto youngestCat = catService.findFirstByOrderByAge();

    if (youngestCat != null) return ResponseEntity.ok(youngestCat);
    else {
      throw new InvalidValueException("There are no records in data base");
    }
  }

  @GetMapping("/total")
  public ResponseEntity<Integer> findTotalBy(@RequestParam("fieldName") String fieldName) throws Exception {
    Field field = FieldUtility.getCriteriaFieldForClass(fieldName, Cat.class);
    boolean isFieldValidForTotal = FieldUtility.isFieldValid(field) && FieldUtility.isFieldCountable(field);

    if (isFieldValidForTotal) {
      return ResponseEntity.ok(catService.findTotalBy(fieldName));
    } else {
      throw new InvalidValueException("No such field as '" + fieldName + "'");
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<CatDto> deleteById(@PathVariable Long id) throws InvalidValueException {
    CatDto deletedCatDto = catService.deleteById(id);
    return new ResponseEntity<>(deletedCatDto, HttpStatus.OK);
  }
}
