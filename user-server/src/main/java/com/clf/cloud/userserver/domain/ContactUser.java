package com.clf.cloud.userserver.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * contact_user
 * @author 
 */
@Data
public class ContactUser implements Serializable {
    private Long id;

    /**
     * 联系人为已注册用户则保存对应的userId
     */
    private String userId;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 电话号码
     */
    private String phone;

    private static final long serialVersionUID = 1L;
}