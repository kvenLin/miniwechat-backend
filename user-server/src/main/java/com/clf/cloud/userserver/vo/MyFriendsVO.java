package com.clf.cloud.userserver.vo;

import lombok.Data;

@Data
public class MyFriendsVO {
    private String friendUserId;
    private String friendUsername;
    private String friendFaceImage;
    private String friendNickname;
}