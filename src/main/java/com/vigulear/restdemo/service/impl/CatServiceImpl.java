package com.vigulear.restdemo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.InvalidValueException;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.repository.CatRepository;
import com.vigulear.restdemo.service.CatService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.vigulear.restdemo.mapper.CatMapper.mapToCatDto;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@Service
public class CatServiceImpl implements CatService {

  private final CatRepository catRepository;

  public CatServiceImpl(CatRepository catRepository) {
    this.catRepository = catRepository;
  }

  @Override
  public CatDto findById(Long id) {
    var cat = catRepository.findById(id).orElse(null);
    assert cat != null;
    return mapToCatDto(cat);
  }

  @Override
  public List<CatDto> findAll() {
    var cats = catRepository.findAll();
    return cats.stream().map(CatMapper::mapToCatDto).collect(Collectors.toList());
  }

  @Override
  public List<CatDto> findTopByField(Integer quantity, String fieldName) {
    var cats = catRepository.findTopByField(quantity, fieldName);
    return cats.stream().map(CatMapper::mapToCatDto).collect(Collectors.toList());
  }

  @Override
  public List<CatDto> findFirst3(Sort sort) {
    var cats = catRepository.findFirst3By(sort);
    return cats.stream().map(CatMapper::mapToCatDto).collect(Collectors.toList());
  }

  @Override
  public CatDto findFirstByOrderByAge() {
    var cat = catRepository.findFirstByOrderByAge();
    return mapToCatDto(cat);
  }

  @Override
  public Integer findTotalBy(String fieldName) {
    return catRepository.findTotalBy(fieldName);
  }

  @Override
  public CatDto createCat(Cat cat) {
    var newCat = catRepository.save(cat);
    return mapToCatDto(newCat);
  }

  @Override
  public List<CatDto> createAllCats(List<Cat> cats) {
    var newCats = catRepository.saveAll(cats);
    return newCats.stream().map(CatMapper::mapToCatDto).collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) throws InvalidValueException {
    Cat catToDelete = catRepository.findById(id).orElse(null);

    if (catToDelete == null) {
      throw new InvalidValueException("There is no cat with id = " + id);
    }
    catRepository.deleteById(catToDelete.getId());
  }

  @Override
  public CatDto updateById(Long id, Cat cat) throws InvalidValueException {
    Cat catById = catRepository.findById(id).orElse(null);

    if (catById != null ) {
      catById
              .setId(cat.getId() != null ? cat.getId() : catById.getId())
              .setName(cat.getName() != null ? cat.getName() : catById.getName())
              .setAge(cat.getAge() != null ? cat.getAge() : catById.getAge())
              .setVersion(cat.getVersion() != null ? cat.getVersion() + 1 : catById.getVersion() + 1)
              .setCreatedOn(cat.getCreatedOn() != null ? cat.getCreatedOn() : catById.getCreatedOn())
              .setUpdatedOn(LocalDateTime.now());
    }

    else throw new InvalidValueException("There is no cat with id = " + id);

    return CatMapper.mapToCatDto(catRepository.save(catById));
  }
}
