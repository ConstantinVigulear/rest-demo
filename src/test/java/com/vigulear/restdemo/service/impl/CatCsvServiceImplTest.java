package com.vigulear.restdemo.service.impl;

import com.vigulear.restdemo.model.CatCSVRecord;
import com.vigulear.restdemo.service.CatCsvService;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : crme059, Constantin Vigulear
 */
public class CatCsvServiceImplTest {
    CatCsvService catCsvService = new CatCsvServiceImpl();

    @Test
    void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/cats.csv");

        List<CatCSVRecord> records = catCsvService.convertCSV(file);

        assertThat(records.size()).isGreaterThan(0);

    }
}
