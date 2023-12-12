package com.vigulear.restdemo.service.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import com.vigulear.restdemo.dto.CatDto;
import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.exceptions.InvalidValueException;
import com.vigulear.restdemo.exceptions.NotFoundException;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.repository.CatRepository;
import com.vigulear.restdemo.service.CatService;
import com.vigulear.restdemo.util.FieldUtility;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
    var cat = catRepository.findById(id).orElseThrow(NotFoundException::new);
    return CatMapper.mapToCatDto(cat);
  }

  @Override
  public List<CatDto> findAll() {
    var cats = catRepository.findAll();
    return cats.stream().map(CatMapper::mapToCatDto).collect(Collectors.toList());
  }

  @Override
  public List<CatDto> findTopByField(Integer quantity, String fieldName)
      throws InvalidValueException {

    if (quantity <= 0)
      throw new InvalidValueException("Invalid value \"" + quantity + "\" for parameter \"top\"");

    Field field = FieldUtility.getCriteriaFieldForClass(fieldName, Cat.class);

    if (FieldUtility.isFieldValid(field)) {
      List<Cat> queriedCats = catRepository.findTopByField(quantity, fieldName);
      return queriedCats.stream().map(CatMapper::mapToCatDto).collect(Collectors.toList());
    } else {
      throw new InvalidValueException("No such field as '" + fieldName + "'");
    }
  }

  @Override
  public List<CatDto> findFirst3(String fieldName) throws InvalidValueException {
    Field field = FieldUtility.getCriteriaFieldForClass(fieldName, Cat.class);
    if (FieldUtility.isFieldValid(field)) {
      Sort sort = Sort.by(fieldName).ascending();
      List<Cat> cats = catRepository.findFirst3By(sort);
      List<CatDto> catDtos = cats.stream().map(CatMapper::mapToCatDto).collect(Collectors.toList());

      return catDtos;
    } else {
      throw new InvalidValueException("No such field as '" + fieldName + "'");
    }
  }

  @Override
  public CatDto findFirstByOrderByAge() {
    var cat = catRepository.findFirstByOrderByAge().orElseThrow(NotFoundException::new);
    return mapToCatDto(cat);
  }

  @Override
  public Integer findTotalBy(String fieldName) throws InvalidValueException {
    Field field = FieldUtility.getCriteriaFieldForClass(fieldName, Cat.class);
    boolean isFieldValidForTotal =
        FieldUtility.isFieldValid(field) && FieldUtility.isFieldCountable(field);

    if (isFieldValidForTotal) {
      return catRepository.findTotalBy(fieldName);
    } else {
      throw new InvalidValueException("No such field as '" + fieldName + "'");
    }
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
  public void deleteById(Long id) {
    catRepository
        .findById(id)
        .ifPresent(catToDelete -> catRepository.deleteById(catToDelete.getId()));
  }

  @Override
  public CatDto updateById(Long id, Cat cat) {
    Cat catById = catRepository.findById(id).orElseThrow(NotFoundException::new);

    catById.setName(cat.getName());
    catById.setAge(cat.getAge());
    return CatMapper.mapToCatDto(catRepository.save(catById));
  }

  @Override
  public void patchById(Long id, Cat cat) {
    Cat catById = catRepository.findById(id).orElse(null);

    if (catById != null) {

      if (cat.getName() != null) {
        catById.setName(cat.getName());
      }

      if (cat.getAge() != null) {
        catById.setAge(cat.getAge());
      }
      catRepository.save(catById);
    }
  }
}
