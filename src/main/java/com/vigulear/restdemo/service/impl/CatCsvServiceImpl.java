package com.vigulear.restdemo.service.impl;

import com.opencsv.bean.CsvToBeanBuilder;
import com.vigulear.restdemo.model.CatCSVRecord;
import com.vigulear.restdemo.service.CatCsvService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author : crme059, Constantin Vigulear
 */
@Service
public class CatCsvServiceImpl implements CatCsvService {
  @Override
  public List<CatCSVRecord> convertCSV(File csvFile) {
    try {
      return new CsvToBeanBuilder<CatCSVRecord>(new FileReader(csvFile))
          .withType(CatCSVRecord.class)
          .build()
          .parse();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
