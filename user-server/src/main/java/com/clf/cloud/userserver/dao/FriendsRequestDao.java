package com.clf.cloud.userserver.dao;

import com.clf.cloud.userserver.domain.FriendsRequest;
import org.apache.ibatis.annotations.Param;

public interface FriendsRequestDao {
    int deleteByPrimaryKey(String id);

    int insert(FriendsRequest record);

    FriendsRequest selectByPrimaryKey(String id);

    int updateByPrimaryKey(FriendsRequest record);

    FriendsRequest selectBySendUserIdAndAcceptUserId(@Param("sendUserId") String sendUserId,
                                                     @Param("acceptUserId") String acceptUserId);

    int deleteBySendUserIdAndAcceptUserId(@Param("sendUserId") String sendUserId,
                                          @Param("acceptUserId") String acceptUserId);
}