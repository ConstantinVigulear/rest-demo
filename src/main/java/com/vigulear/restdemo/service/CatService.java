package com.vigulear.restdemo.service;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.InvalidValueException;

import java.util.List;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
public interface CatService {
  CatDto findById(Long id);

  List<CatDto> findAll();

  List<CatDto> findTopByField(Integer quantity, String fieldName) throws InvalidValueException;

  List<CatDto> findFirst3(String fieldName) throws InvalidValueException;

  CatDto findFirstByOrderByAge();

  Integer findTotalBy(String fieldName) throws InvalidValueException;

  CatDto createCat(Cat cat);

  List<CatDto> createAllCats(List<Cat> cats);

  void deleteById(Long id) throws InvalidValueException;

  CatDto updateById(Long id, Cat cat) throws InvalidValueException;

  void patchById(Long id, Cat cat) throws InvalidValueException;
}
