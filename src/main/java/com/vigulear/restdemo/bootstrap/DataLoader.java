package com.vigulear.restdemo.bootstrap;

import java.util.List;

import com.vigulear.restdemo.dto.CatDTO;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.service.CatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@Component
public class DataLoader implements CommandLineRunner {

  private final CatService service;
  private final CatMapper catMapper;

  public DataLoader(CatService service, CatMapper catMapper) {
    this.service = service;
      this.catMapper = catMapper;
  }

  @Override
  public void run(String... args) {
    List<Cat> cats =
        List.of(
            Cat.builder().name("Millefoglie").age(1).build(),
            Cat.builder().name("Couscous").age(0).build(),
            Cat.builder().name("Tiramisu").age(7).build(),
            Cat.builder().name("Object").age(3).build(),
            Cat.builder().name("Engin").age(6).build(),
            Cat.builder().name("Revision").age(8).build(),
            Cat.builder().name("Jacobson").age(10).build(),
            Cat.builder().name("Ivar").age(4).build(),
            Cat.builder().name("Enzo").age(6).build(),
            Cat.builder().name("Finland").age(15).build(),
            Cat.builder().name("Robert").age(13).build());

    List<CatDTO> catDTOs = cats.stream().map(catMapper::catToCatDto).toList();

    service.createAllCats(catDTOs);
  }
}
