package com.clf.cloud.userserver.service;


import com.clf.cloud.userserver.bo.ContactsBO;

/**
 * @Author: clf
 * @Date: 2020-02-19
 * @Description: TODO
 */
public interface ContactService {
    /**
     * 保存通讯录信息
     * @param contactsBO
     */
    void saveContact(ContactsBO contactsBO);
}
