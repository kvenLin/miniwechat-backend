package com.clf.cloud.userserver.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * user_contact_rel
 * @author 
 */
@Data
public class UserContactRel implements Serializable {
    private Long id;

    private String userId;

    /**
     * 用户与该手机号之间的关系名备注
     */
    private String relationNick;

    /**
     * 电话号码
     */
    private String phone;

    private static final long serialVersionUID = 1L;
}