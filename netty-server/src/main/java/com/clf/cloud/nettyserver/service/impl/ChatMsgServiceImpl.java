package com.clf.cloud.nettyserver.service.impl;

import com.clf.cloud.common.enums.MsgSignFlagEnum;
import com.clf.cloud.nettyserver.config.ChatMsgNio;
import com.clf.cloud.nettyserver.dao.ChatMsgDao;
import com.clf.cloud.nettyserver.domain.ChatMsg;
import com.clf.cloud.nettyserver.service.ChatMsgService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: clf
 * @Date: 2020-04-11
 * @Description: TODO
 */
@Service
public class ChatMsgServiceImpl implements ChatMsgService {
    @Autowired
    private ChatMsgDao chatMsgDao;
    @Autowired
    private Sid sid;
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveMsg(ChatMsgNio chatMsgNio) {
        ChatMsg msg = new ChatMsg();
        msg.setId(sid.nextShort());
        msg.setSendUserId(chatMsgNio.getSenderId());
        msg.setAcceptUserId(chatMsgNio.getReceiverId());
        msg.setMsg(chatMsgNio.getMsg());
        msg.setCreateTime(new Date());
        msg.setSignFlag(MsgSignFlagEnum.unsign.type);
        chatMsgDao.insert(msg);
        return msg.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        chatMsgDao.batchUpdateMsgSigned(msgIdList);
    }

}
