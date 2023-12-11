package com.vigulear.restdemo.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : crme059
 * @created : 05-Dec-23, Tuesday
 */
@Builder
@Data
public class CatDto {
  private Long id;
  private String name;
  private Integer age;
  private Integer version;
  private LocalDateTime createdOn;
  private LocalDateTime updateOn;
}
