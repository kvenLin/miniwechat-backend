package com.clf.cloud.userserver.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: clf
 * @Date: 2020-02-19
 * @Description: TODO
 */
@Data
public class ContactsBO {
    @NotNull(message = "用户Id不能为空")
    private String userId;
    private List<ContactBO> contactList;
}
