package com.clf.cloud.userserver.dao;

import com.clf.cloud.userserver.domain.MyFriends;

public interface MyFriendsDao {
    int deleteByPrimaryKey(String id);

    int insert(MyFriends record);

    int insertSelective(MyFriends record);

    MyFriends selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MyFriends record);

    int updateByPrimaryKey(MyFriends record);
}