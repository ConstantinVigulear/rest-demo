package com.vigulear.restdemo.entity;

import jakarta.persistence.*;
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
public class CatOrder extends AbstractEntity<CatOrder> {

  protected CatOrder() {}

  private String customerRef;

  @ManyToOne private Customer customer;

  @OneToMany(mappedBy = "catOrder")
  private Set<CatOrderLine> catOrderLines = new HashSet<>();

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private CatOrderShipment catOrderShipment;

  public CatOrder setCustomerRef(String customerRef) {
    this.customerRef = customerRef;
    return this;
  }

  public CatOrder setCustomer(Customer customer) {
    this.customer = customer;
    customer.getCatOrders().add(this);
    return this;
  }

  public CatOrder setCatOrderShipment(CatOrderShipment catOrderShipment) {
    this.catOrderShipment = catOrderShipment;
    catOrderShipment.setCatOrder(this);
    return this;
  }

  public boolean isNew() {
    return this.getId() == null;
  }

  public static class Builder {
    private UUID id;
    private String customerRef;
    private Customer customer;
    private Integer version;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private CatOrderShipment catOrderShipment;

    public Builder() {}

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder customerRef(String customerRef) {
      this.customerRef = customerRef;
      return this;
    }

    public Builder customer(Customer customer) {
      this.customer = customer;
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

    public Builder catOrderShipment(CatOrderShipment catOrderShipment) {
      this.catOrderShipment = catOrderShipment;
      return this;
    }

    public CatOrder build() {
      return new CatOrder()
          .setId(this.id)
          .setCustomer(this.customer)
          .setCustomerRef(this.customerRef)
          .setVersion(this.version)
          .setCreatedOn(this.createdOn)
          .setUpdatedOn(this.updatedOn)
          .setCatOrderShipment(this.catOrderShipment);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
