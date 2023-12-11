package com.vigulear.restdemo.mapper;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;

/**
 * @author : crme059
 * @created : 05-Dec-23, Tuesday
 */
public class CatMapper {
  public static CatDto mapToCatDto(Cat cat) {
    if (cat == null) return null;
    else
      return CatDto.builder()
          .id(cat.getId())
          .name(cat.getName())
          .age(cat.getAge())
          .version(cat.getVersion())
          .createdOn(cat.getCreatedOn())
          .updateOn(cat.getUpdatedOn())
          .build();
  }
}
