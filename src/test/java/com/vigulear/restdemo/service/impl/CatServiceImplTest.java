package com.vigulear.restdemo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.repository.CatRepository;
import com.vigulear.restdemo.service.impl.CatServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author : crme059
 * @created : 04-Dec-23, Monday
 * Todo: test the rest of the class
 */

@ExtendWith(MockitoExtension.class)
class CatServiceImplTest {

  @Mock private CatRepository catRepository;

  @InjectMocks private CatServiceImpl catService;

  @Test
  void findById_validPayload_returnValidCatDto() {
    Cat cat = Cat.builder().id(1001L).name("Couscous").age(8).build();
    CatDto catDto = CatMapper.mapToCatDto(cat);
    when(catRepository.findById(1001L)).thenReturn(Optional.of(cat));

    var returnedCat = catService.findById(1001L);

    assertThat(catDto).isEqualTo(returnedCat);
  }

  @Test
  void findAll() {}

  @Test
  void findTopByField() {}

  @Test
  void saveAll() {}

  @Test
  void findFirst3() {}

  @Test
  void findFirstByOrderByAge() {}

  @Test
  void findTotalBy() {}
}