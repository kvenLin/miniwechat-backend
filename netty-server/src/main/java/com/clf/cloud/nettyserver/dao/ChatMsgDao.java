package com.clf.cloud.nettyserver.dao;

import com.clf.cloud.nettyserver.domain.ChatMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChatMsgDao {
    int deleteByPrimaryKey(String id);

    int insert(ChatMsg record);

    ChatMsg selectByPrimaryKey(String id);

    int updateByPrimaryKey(ChatMsg record);

    void batchUpdateMsgSigned(List<String> msgIdList);

    List<ChatMsg> selectByAcceptUserIdAndSignType(@Param("acceptUserId") String acceptUserId, @Param("type") Integer type);
}