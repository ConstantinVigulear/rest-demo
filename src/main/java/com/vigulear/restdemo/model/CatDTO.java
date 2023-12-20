package com.vigulear.restdemo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/**
 * @author : crme059
 * @created : 05-Dec-23, Tuesday
 */
@Builder
@Data
public class CatDTO {
  private UUID id;

  @NotNull
  @NotBlank
  @Size(max = 50)
  private String name;

  @NotNull @PositiveOrZero private Integer age;
  private Integer version;
  private LocalDateTime createdOn;
  private LocalDateTime updatedOn;
}
