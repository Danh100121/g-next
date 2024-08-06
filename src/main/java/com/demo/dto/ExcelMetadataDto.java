package com.demo.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExcelMetadataDto {
    private String tableName;
    private List<String> headers;
    private List<Map<String, String>> datas;
}
