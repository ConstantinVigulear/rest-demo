package com.vigulear.restdemo.mapper;

import com.vigulear.restdemo.model.CatDTO;
import com.vigulear.restdemo.entity.Cat;
import org.mapstruct.Mapper;

/**
 * @author : crme059
 * @created : 05-Dec-23, Tuesday
 */
@Mapper
public interface CatMapper {

  Cat catDtoToCat(CatDTO dto);

  CatDTO catToCatDto(Cat cat);
}
