package com.vigulear.restdemo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@SuppressWarnings("unused")
@MappedSuperclass
public abstract class AbstractEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idGenerator")
  @SequenceGenerator(name = "idGenerator", initialValue = 1001)
  private Long id;
  @Version()
  private Integer version = 1;
  @CreationTimestamp
  private LocalDateTime createdOn;
  @CreationTimestamp
  private LocalDateTime updateOn;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(LocalDateTime createdOn) {
    this.createdOn = createdOn;
  }

  public LocalDateTime getUpdateOn() {
    return updateOn;
  }

  public void setUpdateOn(LocalDateTime updateOn) {
    this.updateOn = updateOn;
  }
}
