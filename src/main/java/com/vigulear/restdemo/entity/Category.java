package com.vigulear.restdemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author : crme059, Constantin Vigulear
 */
@Entity
@Getter
@EqualsAndHashCode(callSuper = false)
public class Category extends AbstractEntity<Category> {
  @Size(max = 50)
  @Column(length = 50)
  private String description;

  @ManyToMany
  @JoinTable(
      name = "cat_category",
      joinColumns = @JoinColumn(name = "category_id"),
      inverseJoinColumns = @JoinColumn(name = "cat_id"))
  private Set<Cat> cats = new HashSet<>();

  protected Category() {}

  public Category setDescription(String description) {
    this.description = description;
    return this;
  }

  public Category setCats(Set<Cat> cats) {
    this.cats = cats;
    return this;
  }

  public static class Builder {
    private UUID id;
    private String description;
    private Set<Cat> cats = new HashSet<>();
    private Integer version;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public Builder() {}

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder cats(Set<Cat> cats) {
      this.cats = cats;
      return this;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder createdOn(LocalDateTime createdOn) {
      this.createdOn = createdOn;
      return this;
    }

    public Builder updatedOn(LocalDateTime updatedOn) {
      this.updatedOn = updatedOn;
      return this;
    }

    public Category build() {
      return new Category()
          .setId(this.id)
          .setVersion(this.version)
          .setCreatedOn(this.createdOn)
          .setUpdatedOn(this.updatedOn)
          .setDescription(this.description)
          .setCats(this.cats);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
