package com.vigulear.restdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author : crme059, Constantin Vigulear
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
public class CatOrderShipment extends AbstractEntity<CatOrderShipment> {

  @OneToOne private CatOrder catOrder;
  private String trackingNumber;

  public CatOrderShipment setCatOrder(CatOrder catOrder) {
    this.catOrder = catOrder;
    return this;
  }

  public CatOrderShipment setTrackingNumber(String trackingNumber) {
    this.trackingNumber = trackingNumber;
    return this;
  }

  public static class Builder {
    private UUID id;
    private CatOrder catOrder;
    private String trackingNumber;
    private Integer version;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public Builder() {}

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder catOrder(CatOrder catOrder) {
      this.catOrder = catOrder;
      return this;
    }

    public Builder trackingNumber(String trackingNumber) {
      this.trackingNumber = trackingNumber;
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

    public CatOrderShipment build() {
      return new CatOrderShipment()
          .setId(this.id)
          .setCatOrder(this.catOrder)
          .setTrackingNumber(this.trackingNumber)
          .setVersion(this.version)
          .setCreatedOn(this.createdOn)
          .setUpdatedOn(this.updatedOn);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
