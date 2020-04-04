package com.clf.cloud.gatewayserver.dao;

import com.clf.cloud.gatewayserver.domain.AuthQuery;

import java.util.List;

public interface AuthQueryDao {
    int deleteByPrimaryKey(Long id);

    int insert(AuthQuery record);

    int insertSelective(AuthQuery record);

    AuthQuery selectByPrimaryKey(Long id);

    List<String> selectAnonUrl();

    int updateByPrimaryKeySelective(AuthQuery record);

    int updateByPrimaryKey(AuthQuery record);
}