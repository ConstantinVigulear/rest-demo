package com.vigulear.restdemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
public class Cat extends AbstractEntity<Cat> implements Animal {

  @NotNull
  @NotBlank
  @Size(max = 50)
  @Column(length = 50)
  private String name;

  @NotNull @PositiveOrZero private Integer age;

  @OneToMany(mappedBy = "cat")
  private Set<CatOrderLine> catOrderLineSet;

  @ManyToMany
  @JoinTable(
      name = "cat_category",
      joinColumns = @JoinColumn(name = "cat_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();

  protected Cat() {}

  public Cat setName(String name) {
    this.name = name;
    return this;
  }

  public Cat setAge(Integer age) {
    this.age = age;
    return this;
  }

  public void addCategory(Category category) {
    this.categories.add(category);
    category.getCats().add(this);
  }

  public void removeCategory(Category category) {
    this.categories.remove(category);
    category.getCats().remove(this);
  }

  @Override
  public String sound() {
    return "Meow";
  }

  public static class Builder {
    private UUID id;
    private String name;
    private Integer age;
    private Integer version;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public Builder() {}

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder age(Integer age) {
      this.age = age;
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

    public Cat build() {
      return new Cat()
          .setId(this.id)
          .setName(this.name)
          .setAge(this.age)
          .setVersion(this.version)
          .setCreatedOn(this.createdOn)
          .setUpdatedOn(this.updatedOn);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
