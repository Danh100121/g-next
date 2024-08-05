package com.demo.service;

import com.demo.dto.UserDto;
import com.demo.dto.request.RemoveUsersReq;
import com.demo.dto.request.UpsertUserReq;
import com.demo.dto.request.UserCriteria;
import com.demo.dto.utils.PagingReq;
import com.demo.entity.User;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserService {
    Page<?> getUsers(UserCriteria userCriteria, PagingReq pagingReq);

    void upsertUser(UpsertUserReq upsertUserReq);

    void removeUsers(RemoveUsersReq removeUsersReq);

    List<?> exportDataUsers(UserCriteria userCriteria);
}
