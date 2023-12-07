package com.vigulear.restdemo.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@Entity
public class Cat extends AbstractEntity implements Animal {

  @NotEmpty
  private String name = "";

  @NotEmpty private Integer age = 0;

  protected Cat() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
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
  public void sound() {
    System.out.println("Meow");
  }

  public static class Builder {
    private Long id;
    private String name;
    private Integer age;

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
      var cat = new Cat();
      cat.setId(this.id);
      cat.setName(this.name);
      cat.setAge(this.age);
      return cat;
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
