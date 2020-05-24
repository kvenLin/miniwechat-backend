package com.clf.cloud.api.vo;

import lombok.Data;

@Data
public class UsersVO {
    private String id;
    private String token;
    private String username;
    private String faceImage;
    private String faceImageBig;
    private String nickname;
    private String qrcode;
    private String mobile;
}