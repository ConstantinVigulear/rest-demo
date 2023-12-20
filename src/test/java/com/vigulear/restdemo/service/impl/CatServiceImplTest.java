package com.vigulear.restdemo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.mapper.CatMapperImpl;
import com.vigulear.restdemo.model.CatDTO;
import com.vigulear.restdemo.repository.CatRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

/**
 * @author : crme059
 * @created : 04-Dec-23, Monday Todo: test the rest of the class
 */
@ExtendWith(MockitoExtension.class)
class CatServiceImplTest {

  @Mock CatRepository catRepository;
  @Mock CatMapper catMapperStub;
  CatMapper catMapper = new CatMapperImpl();
  @InjectMocks CatServiceImpl catService;
  @Captor ArgumentCaptor<UUID> uuidArgumentCaptor;
  @Captor ArgumentCaptor<Cat> catArgumentCaptor;
  @Captor ArgumentCaptor<String> stringArgumentCaptor;
  @Captor ArgumentCaptor<Integer> integerArgumentCaptor;

  @Test
  void createCat() {
    Cat cat = Cat.builder().id(UUID.randomUUID()).name("Couscous").age(8).build();

    when(catMapperStub.catDtoToCat(catMapper.catToCatDto(cat))).thenReturn(cat);
    when(catMapperStub.catToCatDto(cat)).thenReturn(catMapper.catToCatDto(cat));
    when(catRepository.save(cat)).thenReturn(cat);

    CatDTO catDTO = catService.createCat(catMapper.catToCatDto(cat));

    verify(catRepository, times(1)).save(catArgumentCaptor.capture());
    assertThat(cat).isEqualTo(catArgumentCaptor.getValue());
    assertThat(catDTO).isNotNull().isEqualTo(catMapper.catToCatDto(cat));
  }

  @Test
  void createAllCats() {
    List<Cat> cats =
        List.of(
            Cat.builder().id(UUID.randomUUID()).name("Couscous").age(8).build(),
            Cat.builder().id(UUID.randomUUID()).name("Couscous").age(8).build());
    List<CatDTO> catDTOs = cats.stream().map(catMapper::catToCatDto).toList();

    when(catRepository.saveAll(any())).thenReturn(cats);

    List<CatDTO> foundCatDTOS = catService.createAllCats(catDTOs);

    assertThat(foundCatDTOS).isNotNull().isNotEmpty();
  }

  @Test
  void findById_validId_returnValidCatDto() {
    Cat cat = Cat.builder().id(UUID.randomUUID()).name("Couscous").age(8).build();
    CatDTO catDto = catMapper.catToCatDto(cat);

    when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));
    when(catMapperStub.catToCatDto(cat)).thenReturn(catDto);

    var returnedCat = catService.findById(cat.getId()).orElse(null);

    assertThat(returnedCat).isNotNull().isEqualTo(catDto);
  }

  @Test
  void findById_invalidId_returnsOptionalOfNull() {
    Cat cat = Cat.builder().id(UUID.randomUUID()).name("Couscous").age(8).build();

    when(catRepository.findById(cat.getId())).thenReturn(Optional.empty());

    Optional<CatDTO> optional = catService.findById(cat.getId());

    verify(catRepository, times(1)).findById(uuidArgumentCaptor.capture());
    assertThat(cat.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    assertThat(optional).isEmpty();
  }

  @Test
  void findAll() {
    List<Cat> cats =
        List.of(
            Cat.builder().id(UUID.randomUUID()).name("Couscous").age(8).build(),
            Cat.builder().id(UUID.randomUUID()).name("Tiramisu").age(18).build());
    List<CatDTO> catDTOS = cats.stream().map(catMapper::catToCatDto).toList();

    when(catRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(cats));
    cats.forEach(
        cat -> when(catMapperStub.catToCatDto(cat)).thenReturn(catMapper.catToCatDto(cat)));

    Page<CatDTO> foundCatDtoPage = catService.findAll(null, null, 1, 25);

    assertThat(foundCatDtoPage.getContent()).isNotNull().isNotEmpty().isEqualTo(catDTOS);
  }

  @Test
  void findAll_returnEmptyList() {

    when(catRepository.findAll(any(PageRequest.class)))
        .thenReturn(new PageImpl<>(new ArrayList<>()) {});

    Page<CatDTO> foundCatDTOS = catService.findAll(null, null, 1, 25);

    assertThat(foundCatDTOS).isNotNull().isEmpty();
  }

  @Test
  void findTopByField() {
    Integer quantity = 2;
    String fieldName = "age";
    List<Cat> cats =
        List.of(
            Cat.builder().name("Couscous").age(0).build(),
            Cat.builder().name("Tiramisu").age(7).build());

    when(catRepository.findTopByField(quantity, fieldName)).thenReturn(cats);

    List<CatDTO> foundCatDTOS = catService.findTopByField(quantity, fieldName);

    assertThat(foundCatDTOS).isNotNull().isNotEmpty();

    verify(catRepository, times(1))
        .findTopByField(integerArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertThat(quantity).isEqualTo(integerArgumentCaptor.getValue());
    assertThat(fieldName).isEqualTo(stringArgumentCaptor.getValue());
  }

  @Test
  void deleteById_validId_returnTrue() {
    Cat catToDelete = Cat.builder().name("Humus").age(1).build();

    when(catRepository.existsById(catToDelete.getId())).thenReturn(true);

    boolean deleted = catService.deleteById(catToDelete.getId());

    verify(catRepository, times(1)).deleteById(uuidArgumentCaptor.capture());
    assertThat(catToDelete.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    verify(catRepository, times(1)).existsById(uuidArgumentCaptor.capture());
    assertThat(catToDelete.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    assertThat(deleted).isEqualTo(true);
  }

  @Test
  void deleteById_validId_returnFalse() {
    UUID id = UUID.randomUUID();

    when(catRepository.existsById(id)).thenReturn(false);

    boolean deleted = catService.deleteById(id);

    verify(catRepository, times(1)).existsById(uuidArgumentCaptor.capture());
    assertThat(id).isEqualTo(uuidArgumentCaptor.getValue());

    verify(catRepository, times(0)).deleteById(any());

    assertThat(deleted).isEqualTo(false);
  }
}
