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
    else return new CatDto().setId(cat.getId()).setName(cat.getName()).setAge(cat.getAge());
  }
}
