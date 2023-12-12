package com.vigulear.restdemo.controller;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.InvalidValueException;
import com.vigulear.restdemo.service.CatService;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@Data
@RestController
public class CatController {

  private final CatService catService;

  public CatController(CatService catService) {
    this.catService = catService;
  }

  @PostMapping("${cat.property.cat-path}")
  public ResponseEntity<CatDto> createCat(@RequestBody Cat cat) {
    CatDto savedCatDto = catService.createCat(cat);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.add("Location", "${cat.property.cat-path}" + savedCatDto.getId().toString());

    return new ResponseEntity<>(savedCatDto, httpHeaders, HttpStatus.CREATED);
  }

  @PutMapping("${cat.property.cat-path-id}")
  public ResponseEntity<CatDto> updateById(@PathVariable("catId") Long id, @RequestBody Cat cat)
      throws InvalidValueException {
    CatDto updatedCatDto = catService.updateById(id, cat);

    return new ResponseEntity<>(updatedCatDto, HttpStatus.OK);
  }

  // In summary, PATCH is more suitable for partial updates, while PUT (or UPDATE) is used for
  // complete replacements or creations of a resource.
  @PatchMapping("${cat.property.cat-path-id}")
  public ResponseEntity<?> patchById(@PathVariable("catId") Long id, @RequestBody Cat cat)
      throws InvalidValueException {
    catService.patchById(id, cat);
    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
  }

  @GetMapping("${cat.property.cat-path}")
  public ResponseEntity<List<CatDto>> getAllCats() {
    List<CatDto> cats = catService.findAll();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<>(cats, httpHeaders, HttpStatus.OK);
  }

  @GetMapping("${cat.property.cat-path-id}")
  public ResponseEntity<CatDto> getCatById(@PathVariable("catId") Long id) {
    CatDto cat = catService.findById(id);

    return new ResponseEntity<>(cat, HttpStatus.OK);
  }

  @GetMapping("${cat.property.cat-path}" + "/top")
  public ResponseEntity<List<CatDto>> getTopByField(
      @RequestParam("top") Integer quantity, @RequestParam("fieldName") String fieldName)
      throws InvalidValueException {

    List<CatDto> queriedCats = catService.findTopByField(quantity, fieldName);
    return ResponseEntity.ok(queriedCats);
  }

  @GetMapping("${cat.property.cat-path}" + "/top3")
  public ResponseEntity<List<CatDto>> getTopThree(@RequestParam("fieldName") String fieldName)
      throws Exception {
    return ResponseEntity.ok(catService.findFirst3(fieldName));
  }

  @GetMapping("${cat.property.cat-path}" + "/youngest")
  public ResponseEntity<CatDto> findTheYoungest() {
    CatDto youngestCat = catService.findFirstByOrderByAge();
    return ResponseEntity.ok(youngestCat);
  }

  @GetMapping("${cat.property.cat-path}" + "/total")
  public ResponseEntity<Integer> findTotalBy(@RequestParam String fieldName) throws Exception {

    return ResponseEntity.ok(catService.findTotalBy(fieldName));
  }

  @DeleteMapping("${cat.property.cat-path-id}")
  public ResponseEntity<?> deleteById(@PathVariable("catId") Long id) throws InvalidValueException {
    catService.deleteById(id);
    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
  }
}
