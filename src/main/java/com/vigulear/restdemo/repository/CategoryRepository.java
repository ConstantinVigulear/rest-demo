package com.vigulear.restdemo.repository;

import com.vigulear.restdemo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author : crme059, Constantin Vigulear
 */
public interface CategoryRepository extends JpaRepository<Category, UUID> {}
