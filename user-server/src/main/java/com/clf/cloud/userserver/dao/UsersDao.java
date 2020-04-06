package com.clf.cloud.userserver.dao;

import com.clf.cloud.userserver.domain.Users;
import com.clf.cloud.userserver.vo.FriendRequestVO;
import com.clf.cloud.userserver.vo.MyFriendsVO;

import java.util.List;

public interface UsersDao {
    int deleteByPrimaryKey(String id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    List<MyFriendsVO> queryMyFriends(String userId);

    List<Users> selectAll();

    Users selectOne(String username);

    Users selectByMobile(String mobile);
}