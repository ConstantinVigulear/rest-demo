package com.vigulear.restdemo.service;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
public interface CatService {
    CatDto findById(Long id);
    List<CatDto> findAll();
    List<CatDto> findTopByField(Integer quantity, String fieldName);
    List<CatDto> findFirst3(Sort sort);
    CatDto findFirstByOrderByAge();
    Integer findTotalBy(String fieldName);
    CatDto createCat(Cat cat);
    List<CatDto> createAllCats(List<Cat> cats);
}
