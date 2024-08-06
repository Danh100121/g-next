package com.demo.service;

import com.demo.dto.ExcelMetadataDto;
import com.demo.dto.ResourceDto;

public interface ExcelService {
    ResourceDto exportExcel(ExcelMetadataDto excelMetadataDTO);
}
