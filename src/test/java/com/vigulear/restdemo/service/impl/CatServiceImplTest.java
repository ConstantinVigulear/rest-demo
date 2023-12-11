package com.vigulear.restdemo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.InvalidValueException;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.repository.CatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author : crme059
 * @created : 04-Dec-23, Monday Todo: test the rest of the class
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

  @Test
  void findById() {}

  @Test
  void createCat() {}

  @Test
  void createAllCats() {
    List<Cat> cats =
        List.of(
            Cat.builder().name("Humus").age(1).build(),
            Cat.builder().name("Couscous").age(0).build());
    List<CatDto> catDtos = cats.stream().map(CatMapper::mapToCatDto).toList();

    when(catRepository.saveAll(cats)).thenReturn(cats);

    List<CatDto> savedCatDtos = catService.createAllCats(cats);

    assertThat(catDtos).isEqualTo(savedCatDtos);
  }

  @Test
  void deleteById_validId_returnDeletedCatDto() throws InvalidValueException {
    Cat catToDelete = Cat.builder().id(1L).name("Humus").age(1).build();
    when(catRepository.findById(catToDelete.getId())).thenReturn(Optional.of(catToDelete));

    catService.deleteById(catToDelete.getId());

    verify(catRepository, times(1)).deleteById(catToDelete.getId());
  }

  @Test
  void deleteById_validId_throwsInvalidValueException() {
    Long id = 1001L;
    when(catRepository.findById(id)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> catService.deleteById(id))
        .isInstanceOf(InvalidValueException.class)
        .hasMessage("There is no cat with id = " + id);
  }
}
