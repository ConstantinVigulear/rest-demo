package com.vigulear.restdemo.bootstrap;

import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.entity.Customer;
import com.vigulear.restdemo.model.CatCSVRecord;
import com.vigulear.restdemo.repository.CatRepository;
import com.vigulear.restdemo.repository.CustomerRepository;
import com.vigulear.restdemo.service.CatCsvService;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

  private final CatRepository catRepository;
  private final CatCsvService catCsvService;
  private final CustomerRepository customerRepository;

  @Transactional
  @Override
  public void run(String... args) throws FileNotFoundException {
    loadCatData();
    loadCsvData();
  }

  private void loadCatData() {
    if (catRepository.count() == 0) {
      Set<Cat> cats =
          Set.of(
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

      catRepository.saveAll(cats);
    }

    if (customerRepository.count() == 0) {
      Set<Customer> customers =
          Set.of(
              Customer.builder().name("John").email("john.asdf@gmail.com").build(),
              Customer.builder().name("Luna").email("luna.rert@gmail.com").build(),
              Customer.builder().name("Boris").email("boris.facz@gmail.com").build());
      customerRepository.saveAll(customers);
    }
  }

  private void loadCsvData() throws FileNotFoundException {
    if (catRepository.count() < 100) {
      File file = ResourceUtils.getFile("classpath:csvdata/cats.csv");

      List<CatCSVRecord> records = catCsvService.convertCSV(file);

      records.forEach(
          catCSVRecord ->
              catRepository.save(
                  Cat.builder().name(catCSVRecord.getName()).age(catCSVRecord.getAge()).build()));
    }
  }
}
