package com.clf.cloud.userserver.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * users
 * @author 
 */
@Data
public class Users implements Serializable {
    private String id;

    private String username;

    private String password;

    private String faceImage;

    private String faceImageBig;

    private String nickname;

    private String qrcode;

    private String cid;

    private String mobile;

    private static final long serialVersionUID = 1L;
}