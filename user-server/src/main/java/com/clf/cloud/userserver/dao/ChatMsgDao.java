package com.clf.cloud.userserver.dao;

import com.clf.cloud.userserver.domain.ChatMsg;

public interface ChatMsgDao {
    int deleteByPrimaryKey(String id);

    int insert(ChatMsg record);

    int insertSelective(ChatMsg record);

    ChatMsg selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChatMsg record);

    int updateByPrimaryKey(ChatMsg record);
}