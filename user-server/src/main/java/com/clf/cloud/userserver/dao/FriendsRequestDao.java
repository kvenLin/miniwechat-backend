package com.clf.cloud.userserver.dao;

import com.clf.cloud.userserver.domain.FriendsRequest;
import org.apache.ibatis.annotations.Param;

public interface FriendsRequestDao {
    int deleteByPrimaryKey(String id);

    int insert(FriendsRequest record);

    int insertSelective(FriendsRequest record);

    FriendsRequest selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FriendsRequest record);

    int updateByPrimaryKey(FriendsRequest record);

    FriendsRequest selectBySendUserIdAndAcceptUserId(@Param("sendUserId") String sendUserId,
                                                     @Param("acceptUserId") String acceptUserId);

    int deleteBySendUserIdAndAcceptUserId(@Param("sendUserId") String sendUserId,
                                          @Param("acceptUserId") String acceptUserId);
}