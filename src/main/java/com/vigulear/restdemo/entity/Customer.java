package com.vigulear.restdemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
public class Customer extends AbstractEntity<Customer> {
  @NotNull
  @NotBlank
  @Size(max = 50)
  @Column(length = 50)
  private String name;

  @Email String email;


  @OneToMany(mappedBy = "customer")
  private Set<CatOrder> catOrders = new HashSet<>();

  protected Customer() {}

  public Customer setName(String name) {
    this.name = name;
    return this;
  }

  public Customer setEmail(String email) {
    this.email = email;
    return this;
  }

  public static class Builder {
    private UUID id;
    private String name;
    private String email;
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

    public Builder email(String email) {
      this.email = email;
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

    public Customer build() {
      return new Customer()
          .setId(this.id)
          .setName(this.name)
          .setEmail(this.email)
          .setVersion(this.version)
          .setCreatedOn(this.createdOn)
          .setUpdatedOn(this.updatedOn);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
