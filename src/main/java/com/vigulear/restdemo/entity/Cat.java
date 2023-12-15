package com.vigulear.restdemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@SuppressWarnings("LombokGetterMayBeUsed")
@Entity
public class Cat extends AbstractEntity<Cat> implements Animal {

  @NotNull
  @NotBlank
  @Size(max = 50)
  @Column(length = 50)
  private String name;

  @NotNull @PositiveOrZero private Integer age;

  protected Cat() {}

  public String getName() {
    return name;
  }

  public Cat setName(String name) {
    this.name = name;
    return this;
  }

  public Integer getAge() {
    return age;
  }

  public Cat setAge(Integer age) {
    this.age = age;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Cat cat = (Cat) o;
    return Objects.equals(getName(), cat.getName()) && Objects.equals(getAge(), cat.getAge());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getAge());
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
