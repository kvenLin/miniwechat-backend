package com.clf.cloud.nettyserver;

import com.clf.cloud.nettyserver.dao.ChatMsgDao;
import com.clf.cloud.nettyserver.domain.ChatMsg;
import org.junit.jupiter.api.Test;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class NettyServerApplicationTests {
    @Autowired
    private ChatMsgDao chatMsgDao;
    @Autowired
    private Sid sid;

    @Test
    void contextLoads() {
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setId(sid.nextShort());
        chatMsg.setCreateTime(new Date());
        chatMsgDao.insert(chatMsg);
    }

}
