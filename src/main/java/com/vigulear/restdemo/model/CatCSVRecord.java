package com.vigulear.restdemo.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : crme059, Constantin Vigulear
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatCSVRecord {

  @CsvBindByName private String name;
  @CsvBindByName private Integer age;
}
