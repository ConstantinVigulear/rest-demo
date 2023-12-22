package com.vigulear.restdemo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.vigulear.restdemo.bootstrap.BootstrapData;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.entity.CatOrder;
import com.vigulear.restdemo.entity.CatOrderShipment;
import com.vigulear.restdemo.entity.Customer;
import com.vigulear.restdemo.service.impl.CatCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * @author : crme059, Constantin Vigulear
 */
@DataJpaTest
@Import({BootstrapData.class, CatCsvServiceImpl.class})
class CatOrderRepositoryTest {

  @Autowired CatOrderRepository catOrderRepository;
  @Autowired CustomerRepository customerRepository;
  @Autowired CatRepository catRepository;

  Customer testCustomer;
  Cat testCat;

  @BeforeEach
  void setUp() {
    testCustomer = customerRepository.findAll().getFirst();
    testCat = catRepository.findAll().getFirst();
  }

  @Test
  void testCatOrders() {
    CatOrder catOrder =
        CatOrder.builder()
            .customerRef("Test order")
            .customer(testCustomer)
            .catOrderShipment(CatOrderShipment.builder().trackingNumber("12345r").build())
            .build();

    CatOrder savedCatOrder = catOrderRepository.save(catOrder);

    assertThat(savedCatOrder.getCustomer()).isNotNull();
  }
}
