package com.vigulear.restdemo.dto;


import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

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
