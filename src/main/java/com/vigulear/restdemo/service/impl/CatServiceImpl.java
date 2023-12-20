package com.vigulear.restdemo.service.impl;

import com.vigulear.restdemo.entity.Cat;
import com.vigulear.restdemo.mapper.CatMapper;
import com.vigulear.restdemo.model.CatDTO;
import com.vigulear.restdemo.repository.CatRepository;
import com.vigulear.restdemo.service.CatService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author : crme059
 * @created : 30-Nov-23, Thursday
 */
@Slf4j
@Service
public class CatServiceImpl implements CatService {
  private final CatRepository catRepository;
  private final CatMapper catMapper;

  private static final int DEFAULT_PAGE = 0;
  private static final int DEFAULT_PAGE_SIZE = 25;

  public CatServiceImpl(CatRepository catRepository, CatMapper catMapper) {
    this.catRepository = catRepository;
    this.catMapper = catMapper;
  }

  @Override
  public Optional<CatDTO> findById(UUID id) {
    return Optional.ofNullable(catMapper.catToCatDto(catRepository.findById(id).orElse(null)));
  }

  @Override
  public Page<CatDTO> findAll(
      String catName, Integer catAge, Integer pageNumber, Integer pageSize) {

    Pageable pageRequest = buildPageRequest(pageNumber, pageSize);

    Page<Cat> catPage;

    if (StringUtils.hasText(catName) && catAge == null) {
      catPage = findCatsByName(catName, pageRequest);
    } else if (!StringUtils.hasText(catName) && catAge != null) {
      catPage = findCatsByAge(catAge, pageRequest);
    } else if (StringUtils.hasText(catName) && catAge != null) {
      catPage = findCatsByNameAndAge(catName, catAge, pageRequest);
    } else {
      catPage = catRepository.findAll(pageRequest);
    }

    return catPage.map(catMapper::catToCatDto);
  }

  public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
    int queryPageNumber;
    int queryPageSize;

    if (pageNumber != null && pageNumber > 0) {
      queryPageNumber = pageNumber - 1;
    } else {
      queryPageNumber = DEFAULT_PAGE;
    }

    if (pageSize == null) {
      queryPageSize = DEFAULT_PAGE_SIZE;
    } else {
      if (pageSize > 100000) {
        queryPageSize = 100000;
        log.warn("Attempt to get very large set of records: " + pageSize);
      } else {
        queryPageSize = pageSize;
      }
    }

    Sort sort = Sort.by(Sort.Order.asc("name"));


    return PageRequest.of(queryPageNumber, queryPageSize, sort);
  }

  private Page<Cat> findCatsByNameAndAge(String catName, Integer catAge, Pageable pageable) {
    return catRepository.findAllByNameIsLikeIgnoreCaseAndAge("%" + catName + "%", catAge, pageable);
  }

  private Page<Cat> findCatsByAge(Integer catAge, Pageable pageable) {
    return catRepository.findAllByAge(catAge, pageable);
  }

  public Page<Cat> findCatsByName(String catName, Pageable pageable) {
    return catRepository.findAllByNameIsLikeIgnoreCase("%" + catName + "%", pageable);
  }

  @Override
  public List<CatDTO> findTopByField(Integer quantity, String fieldName) {
    List<Cat> queriedCats = catRepository.findTopByField(quantity, fieldName);
    return queriedCats.stream().map(catMapper::catToCatDto).collect(Collectors.toList());
  }

  @Override
  public List<CatDTO> findTop3(String fieldName) {
    Sort sort = Sort.by(fieldName).ascending();
    List<Cat> cats = catRepository.findFirst3By(sort);

    return cats.stream().map(catMapper::catToCatDto).collect(Collectors.toList());
  }

  @Override
  public Optional<CatDTO> findTheYoungest() {
    AtomicReference<Optional<CatDTO>> atomicReference = new AtomicReference<>();

    catRepository
        .findFirstByOrderByAge()
        .ifPresentOrElse(
            foundCat -> atomicReference.set(Optional.of(catMapper.catToCatDto(foundCat))),
            () -> atomicReference.set(Optional.empty()));

    return atomicReference.get();
  }

  @Override
  public Integer findTotalBy(String fieldName) {
    return catRepository.findTotalBy(fieldName);
  }

  @Override
  public CatDTO createCat(CatDTO catDto) {
    var newCat = catRepository.save(catMapper.catDtoToCat(catDto));
    return catMapper.catToCatDto(newCat);
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
