package com.vigulear.restdemo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.id.UUIDGenerator;
import org.hibernate.id.uuid.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@Getter
@SuppressWarnings({"unused", "UnusedReturnValue"})
@MappedSuperclass
public abstract class AbstractEntity<T> {
  @Id
  @GeneratedValue(generator = "idGenerator")
  @GenericGenerator(name = "idGenerator", type = UuidGenerator.class)
  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  private UUID id;

  @Version private Integer version;
  @CreationTimestamp private LocalDateTime createdOn;
  @UpdateTimestamp private LocalDateTime updatedOn;

  public T setId(UUID id) {
    this.id = id;
    return (T) this;
  }

  public T setVersion(Integer version) {
    this.version = version;
    return (T) this;
  }

  public T setCreatedOn(LocalDateTime createdOn) {
    this.createdOn = createdOn;
    return (T) this;
  }

  public T setUpdatedOn(LocalDateTime updateOn) {
    this.updatedOn = updateOn;
    return (T) this;
  }
}
