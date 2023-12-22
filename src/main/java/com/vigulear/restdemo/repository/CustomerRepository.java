package com.vigulear.restdemo.repository;

import com.vigulear.restdemo.entity.Customer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : crme059, Constantin Vigulear
 */
public interface CustomerRepository extends JpaRepository<Customer, UUID> {}
