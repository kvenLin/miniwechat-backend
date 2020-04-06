package com.clf.cloud.userserver.dao;

import com.clf.cloud.userserver.domain.ContactUser;

public interface ContactUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(ContactUser record);

    int insertSelective(ContactUser record);

    ContactUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ContactUser record);

    int updateByPrimaryKey(ContactUser record);

    ContactUser selectByPhone(String phone);
}