package com.vigulear.restdemo.repository;

import com.vigulear.restdemo.entity.Cat;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
public interface CatRepository extends JpaRepository<Cat, UUID> {

  @Query("""
          SELECT c
          FROM Cat c
          ORDER BY CASE when :fieldName = 'name' THEN c.name END,
                   CASE when :fieldName = 'id' THEN c.id END,
                   CASE when :fieldName = 'version' THEN c.version END,
                   CASE when :fieldName = 'age' THEN c.age END LIMIT :quantity""")
  List<Cat> findTopByField(
      @Param("quantity") Integer quantity, @Param("fieldName") String fieldName);

  List<Cat> findFirst3By(Sort sort);

  Optional<Cat> findFirstByOrderByAge();

  @Query(
      "SELECT SUM(CASE WHEN :fieldName = 'age' THEN c.age "
          + "WHEN :fieldName = 'id' THEN c.id "
          + "ELSE 0 END) "
          + "FROM Cat c")
  Integer findTotalBy(@Param("fieldName") String fieldName);
}
