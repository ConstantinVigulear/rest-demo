package com.vigulear.restdemo.service;

import com.vigulear.restdemo.dto.CatDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
public interface CatService {
  Optional<CatDTO> findById(UUID id);

  List<CatDTO> findAll();

  List<CatDTO> findTopByField(Integer quantity, String fieldName);

  List<CatDTO> findFirst3(String fieldName);

  Optional<CatDTO> findFirstByOrderByAge();

  Integer findTotalBy(String fieldName);

  CatDTO createCat(CatDTO catDto);

  List<CatDTO> createAllCats(List<CatDTO> catDTOS);

  Boolean deleteById(UUID id) ;

  Optional<CatDTO> updateById(UUID id, CatDTO catDto) ;

  Optional<CatDTO> patchById(UUID id, CatDTO catDto);
}
