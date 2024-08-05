package com.demo.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RemoveUsersReq {
    private List<Integer> userIds;
}
