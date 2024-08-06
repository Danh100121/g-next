package com.demo.service.impl;

import com.demo.dto.ExcelMetadataDto;
import com.demo.dto.ResourceDto;
import com.demo.dto.UserDto;
import com.demo.dto.request.RemoveUsersReq;
import com.demo.dto.request.UpsertUserReq;
import com.demo.dto.request.UserCriteria;
import com.demo.dto.utils.PagingReq;
import com.demo.entity.User;
import com.demo.exception.ServiceException;
import com.demo.repository.TeamRepository;
import com.demo.repository.UserRepository;
import com.demo.service.ExcelService;
import com.demo.service.MappingHelper;
import com.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MappingHelper mappingHelper;
    private final TeamRepository teamRepository;
    private final ExcelService excelService;

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
    public ResourceDto exportDataUsers(UserCriteria userCriteria) {
        var userData = userRepository
                .findAll(userCriteria == null ? null : userCriteria.toSpecification())
                .stream().map(e -> {
                    var res = mappingHelper.map(e, UserDto.class);
                    res.setTeam(e.getTeam().getName());
                    return res;
                })
                .collect(Collectors.toList());


        final var resourceDTO = excelService.exportExcel(prepareExcelData(userData));
        resourceDTO.setFileName("user-data-export");
        return resourceDTO;
    }

    public ExcelMetadataDto prepareExcelData(List<UserDto> userData) {
        final var excelMetadataDTO = new ExcelMetadataDto();
        excelMetadataDTO.setTableName("User data");
        excelMetadataDTO.setHeaders(List.of("ID", "Name", "Gender", "Team", "Phone", "Email", "Birth date", "Address", "Status"));
        List<Map<String, String>> metadata = new ArrayList<>();

        for (var user : userData) {
            Map<String, String> data = new HashMap<>();
            data.put("ID", user.getId().toString());
            data.put("Name", user.getName());
            data.put("Gender", user.getGender().toString());
            data.put("Team", user.getTeam());
            data.put("Phone", user.getPhone());
            data.put("Email", user.getEmail());
            data.put("Birth date", user.getBirthDate().toString());
            data.put("Address", user.getAddress());
            data.put("Status", user.getStatus().toString());
            metadata.add(data);
        }
        excelMetadataDTO.setDatas(metadata);
        return excelMetadataDTO;
    }
}
