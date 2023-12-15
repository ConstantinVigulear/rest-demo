package com.vigulear.restdemo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.vigulear.restdemo.dto.CatDTO;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.repository.CatRepository;
import com.vigulear.restdemo.service.CatService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@Service
public class CatServiceImpl implements CatService {
  private final CatRepository catRepository;
  private final CatMapper catMapper;

  public CatServiceImpl(CatRepository catRepository, CatMapper catMapper) {
    this.catRepository = catRepository;
    this.catMapper = catMapper;
  }

  @Override
  public Optional<CatDTO> findById(UUID id) {
    return Optional.ofNullable(catMapper.catToCatDto(catRepository.findById(id).orElse(null)));
  }

  @Override
  public List<CatDTO> findAll() {
    return catRepository.findAll().stream()
        .map(catMapper::catToCatDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<CatDTO> findTopByField(Integer quantity, String fieldName) {
    List<Cat> queriedCats = catRepository.findTopByField(quantity, fieldName);
    return queriedCats.stream().map(catMapper::catToCatDto).collect(Collectors.toList());
  }

  @Override
  public List<CatDTO> findFirst3(String fieldName) {
    Sort sort = Sort.by(fieldName).ascending();
    List<Cat> cats = catRepository.findFirst3By(sort);

    return cats.stream().map(catMapper::catToCatDto).collect(Collectors.toList());
  }

  @Override
  public Optional<CatDTO> findFirstByOrderByAge() {
    AtomicReference<Optional<CatDTO>> atomicReference = new AtomicReference<>();

    catRepository
        .findFirstByOrderByAge()
        .ifPresentOrElse(
            foundCat -> {
              atomicReference.set(Optional.of(catMapper.catToCatDto(foundCat)));
            },
            () -> atomicReference.set(Optional.empty()));

    return atomicReference.get();
  }

  @Override
  public Integer findTotalBy(String fieldName) {
    return catRepository.findTotalBy(fieldName);
  }

  @Override
  public CatDTO createCat(CatDTO catDto) {
    var newCatDto = catRepository.save(catMapper.catDtoToCat(catDto));
    return catMapper.catToCatDto(newCatDto);
  }

  @Override
  public List<CatDTO> createAllCats(List<CatDTO> catDTOS) {
    List<Cat> catsToSave = catDTOS.stream().map(catMapper::catDtoToCat).toList();
    var newCats = catRepository.saveAll(catsToSave);

    return newCats.stream().map(catMapper::catToCatDto).collect(Collectors.toList());
  }

  @Override
  public Boolean deleteById(UUID id) {
    if (catRepository.existsById(id)) {
      catRepository.deleteById(id);
      return true;
    }
    return false;
  }

  @Override
  public Optional<CatDTO> updateById(UUID id, CatDTO catDto) {
    AtomicReference<Optional<CatDTO>> atomicReference = new AtomicReference<>();

    catRepository
        .findById(id)
        .ifPresentOrElse(
            foundCat -> {
              foundCat.setName(catDto.getName());
              foundCat.setAge(catDto.getAge());
              atomicReference.set(Optional.of(catMapper.catToCatDto(catRepository.save(foundCat))));
            },
            () -> atomicReference.set(Optional.empty()));

    return atomicReference.get();
  }

  @Override
  public Optional<CatDTO> patchById(UUID id, CatDTO catDto) {
    AtomicReference<Optional<CatDTO>> atomicReference = new AtomicReference<>();

    catRepository
        .findById(id)
        .ifPresentOrElse(
            found -> {
              if (catDto.getName() != null) {
                found.setName(catDto.getName());
              }

              if (catDto.getAge() != null) {
                found.setAge(catDto.getAge());
              }

              atomicReference.set(Optional.of(catMapper.catToCatDto(catRepository.save(found))));
            },
            () -> atomicReference.set(Optional.empty()));

    return atomicReference.get();
  }
}
