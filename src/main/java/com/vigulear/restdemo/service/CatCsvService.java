package com.vigulear.restdemo.service;

import com.vigulear.restdemo.model.CatCSVRecord;

import java.io.File;
import java.util.List;

/**
 * @author : crme059, Constantin Vigulear
 */
public interface CatCsvService {
    List<CatCSVRecord> convertCSV(File file);
}
