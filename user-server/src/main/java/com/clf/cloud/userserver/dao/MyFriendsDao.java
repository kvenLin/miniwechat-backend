package com.clf.cloud.userserver.dao;

import com.clf.cloud.userserver.domain.MyFriends;
import org.apache.ibatis.annotations.Param;

public interface MyFriendsDao {
    int deleteByPrimaryKey(String id);

    int insert(MyFriends record);

    MyFriends selectByPrimaryKey(String id);

    int updateByPrimaryKey(MyFriends record);

    MyFriends selectByMyUserIdAndFriendId(@Param("myUserId") String myUserId,
                                          @Param("friendUserId") String friendUserId);
}