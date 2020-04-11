package com.clf.cloud.nettyserver.service;

import com.clf.cloud.nettyserver.config.ChatMsgNio;

import java.util.List;

/**
 * @Author: clf
 * @Date: 2020-04-11
 * @Description: TODO
 */
public interface ChatMsgService {

    String saveMsg(ChatMsgNio chatMsgNio);

    void updateMsgSigned(List<String> msgIdList);
}
