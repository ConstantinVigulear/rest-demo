package com.vigulear.restdemo.entity;

import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author : crme059, Constantin Vigulear
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
public class CatOrderLine extends AbstractEntity<CatOrderLine> {

  private Integer orderQuantity = 0;

  private Integer quantityAllocated = 0;

  @ManyToOne
  private CatOrder catOrder;

  @ManyToOne
  private Cat cat;

  public CatOrderLine setOrderQuantity(Integer orderQuantity) {
    this.orderQuantity = orderQuantity;
    return this;
  }

  public CatOrderLine setQuantityAllocated(Integer quantityAllocated) {
    this.quantityAllocated = quantityAllocated;
    return this;
  }

  public boolean isNew() {
    return this.getId() == null;
  }

  public static class Builder {
    private UUID id;
    private Integer orderQuantity = 0;

    private Integer quantityAllocated = 0;
    private Integer version;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public Builder() {}

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder orderQuantity(Integer orderQuantity) {
      this.orderQuantity = orderQuantity;
      return this;
    }

    public Builder quantityAllocated(Integer quantityAllocated) {
      this.quantityAllocated = quantityAllocated;
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

    public CatOrderLine build() {
      return new CatOrderLine()
          .setId(this.id)
          .setOrderQuantity(this.orderQuantity)
          .setQuantityAllocated(this.quantityAllocated)
          .setVersion(this.version)
          .setCreatedOn(this.createdOn)
          .setUpdatedOn(this.updatedOn);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
