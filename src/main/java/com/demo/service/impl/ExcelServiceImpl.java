package com.demo.service.impl;

import com.demo.dto.request.UserCriteria;
import com.demo.dto.utils.ExcelHelper;
import com.demo.entity.User;
import com.demo.repository.UserRepository;
import com.demo.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private final UserRepository repository;
    @Override
    public ByteArrayInputStream load(UserCriteria userCriteria) {
        List<User> users = repository.findAll(userCriteria == null ? null : userCriteria.toSpecification());

        ByteArrayInputStream in = ExcelHelper.usersToExcel(users);
        return in;
    }
}
