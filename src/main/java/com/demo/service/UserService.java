package com.demo.service;

import com.demo.dto.ResourceDto;
import com.demo.dto.request.RemoveUsersReq;
import com.demo.dto.request.UpsertUserReq;
import com.demo.dto.request.UserCriteria;
import com.demo.dto.utils.PagingReq;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<?> getUsers(UserCriteria userCriteria, PagingReq pagingReq);

    void upsertUser(UpsertUserReq upsertUserReq);

    void removeUsers(RemoveUsersReq removeUsersReq);

    ResourceDto exportDataUsers(UserCriteria userCriteria);
}
