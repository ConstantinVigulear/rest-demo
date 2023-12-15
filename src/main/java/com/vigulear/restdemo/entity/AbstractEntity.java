package com.vigulear.restdemo.entity;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.Generator;
import org.hibernate.id.UUIDGenerator;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
@MappedSuperclass
public abstract class AbstractEntity <T>{
  @Id
  @GeneratedValue(generator = "idGenerator")
  @GenericGenerator(name = "idGenerator", type = UUIDGenerator.class)
  @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
  private UUID id;

  @Version
  private Integer version;
  @CreationTimestamp
  private LocalDateTime createdOn;
  @UpdateTimestamp
  private LocalDateTime updatedOn;

  public UUID getId() {
    return id;
  }

  public T setId(UUID id) {
    this.id = id;
    return (T)this;
  }

  public Integer getVersion() {
    return version;
  }

  public T setVersion(Integer version) {
    this.version = version;
    return (T)this;
  }

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public T setCreatedOn(LocalDateTime createdOn) {
    this.createdOn = createdOn;
    return (T)this;
  }

  public LocalDateTime getUpdatedOn() {
    return updatedOn;
  }

  public T setUpdatedOn(LocalDateTime updateOn) {
    this.updatedOn = updateOn;
    return (T)this;
  }
}
