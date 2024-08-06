package com.demo.service;

import com.demo.dto.request.UserCriteria;

import java.io.ByteArrayInputStream;

public interface ExcelService {
    ByteArrayInputStream load(UserCriteria userCriteria);
}
