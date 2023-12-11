package com.vigulear.restdemo.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@Entity
public class Cat extends AbstractEntity<Cat> implements Animal {

  @NotEmpty private String name = "";

  @NotEmpty private Integer age = 0;

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
    private Long id;
    private String name;
    private Integer age;
    private Integer version;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public Builder id(Long id) {
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

    public Cat build() {
      return new Cat()
          .setId(this.id)
          .setName(this.name)
          .setAge(this.age);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
