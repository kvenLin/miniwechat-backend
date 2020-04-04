package com.clf.cloud.userserver.dao;

import com.clf.cloud.userserver.domain.UserContactRel;

public interface UserContactRelDao {
    int deleteByPrimaryKey(Long id);

    int insert(UserContactRel record);

    int insertSelective(UserContactRel record);

    UserContactRel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserContactRel record);

    int updateByPrimaryKey(UserContactRel record);
}