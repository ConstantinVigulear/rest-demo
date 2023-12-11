package com.vigulear.restdemo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
@MappedSuperclass
public abstract class AbstractEntity <T>{
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idGenerator")
  @SequenceGenerator(name = "idGenerator", initialValue = 1001)
  private Long id;
  @Version()
  private Integer version = 1;
  @CreationTimestamp
  private LocalDateTime createdOn;
  @CreationTimestamp
  private LocalDateTime updatedOn;

  public Long getId() {
    return id;
  }

  public T setId(Long id) {
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
