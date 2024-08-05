package com.demo.service.impl;

import com.demo.dto.UserDto;
import com.demo.dto.request.RemoveUsersReq;
import com.demo.dto.request.UpsertUserReq;
import com.demo.dto.request.UserCriteria;
import com.demo.dto.utils.PagingReq;
import com.demo.entity.User;
import com.demo.exception.ServiceException;
import com.demo.repository.TeamRepository;
import com.demo.repository.UserRepository;
import com.demo.service.MappingHelper;
import com.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MappingHelper mappingHelper;
    private final TeamRepository teamRepository;

    @Override
    public Page<?> getUsers(UserCriteria userCriteria, PagingReq pagingReq) {
        return userRepository
                .findAll(userCriteria == null ? null : userCriteria.toSpecification(), pagingReq.makePageable())
                .map(e -> {
                    var res = mappingHelper.map(e, UserDto.class);
                    res.setTeam(e.getTeam().getName());
                    return res;
                });
    }

    @Override
    public void upsertUser(UpsertUserReq upsertUserReq) {
        var user = userRepository
                .findById(upsertUserReq.getId())
                .orElse(new User());
        mappingHelper.mapIfSourceNotNullAndStringNotBlank(upsertUserReq, user);

        userRepository
                .findByEmail(upsertUserReq.getEmail())
                .ifPresent(e -> {
                    if (!e.getId().equals(user.getId()))
                        throw new ServiceException("Email is existed in system", "err.duplicate.email");
                });

        userRepository
                .findByPhone(upsertUserReq.getPhone())
                .ifPresent(e -> {
                    if (!e.getId().equals(user.getId()))
                        throw new ServiceException("Phone number is existed in system", "err.duplicate.phone");
                });

        var team = teamRepository
                .findById(upsertUserReq.getTeam())
                .orElseThrow(() -> new ServiceException(
                        MessageFormat.format("Team with id {0} n", upsertUserReq.getTeam()),
                        "err.entity-not-found")
                );
        user.setTeam(team);
        userRepository.save(user);
    }

    @Override
    public void removeUsers(RemoveUsersReq removeUsersReq) {
        userRepository.deleteAllById(removeUsersReq.getUserIds());
    }

    @Override
    public List<?> exportDataUsers(UserCriteria userCriteria) {
        return userRepository
                .findAll(userCriteria == null ? null : userCriteria.toSpecification())
                .stream().map(e -> {
                    var res = mappingHelper.map(e, UserDto.class);
                    res.setTeam(e.getTeam().getName());
                    return res;
                })
                .collect(Collectors.toList());
    }
}
