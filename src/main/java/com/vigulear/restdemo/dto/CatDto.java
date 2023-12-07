package com.vigulear.restdemo.dto;


import java.util.Objects;

/**
 * @author : crme059
 * @created : 05-Dec-23, Tuesday
 */
public class CatDto {
  private Long id;
  private String name;
  private Integer age;

  public Long getId() {
    return id;
  }

  public CatDto setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public CatDto setName(String name) {
    this.name = name;
    return this;
  }

  public Integer getAge() {
    return age;
  }

  public CatDto setAge(Integer age) {
    this.age = age;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CatDto catDto = (CatDto) o;
    return Objects.equals(getId(), catDto.getId())
        && Objects.equals(getName(), catDto.getName())
        && Objects.equals(getAge(), catDto.getAge());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getAge());
  }
}
