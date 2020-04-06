package com.clf.cloud.userserver.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsersBO {
    private String userId;
    private String faceData;
    @NotNull(message = "用户名不能为空")
    private String nickname;
}