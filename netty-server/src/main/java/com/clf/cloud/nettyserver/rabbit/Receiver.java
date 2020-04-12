package com.clf.cloud.nettyserver.rabbit;

import com.clf.cloud.common.enums.MsgActionEnum;
import com.clf.cloud.common.utils.JsonUtils;
import com.clf.cloud.nettyserver.config.ChatHandler;
import com.clf.cloud.nettyserver.config.DataContent;
import com.clf.cloud.nettyserver.config.UserChannelRel;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: clf
 * @Date: 2020-04-12
 * @Description: TODO
 */
@Component
@Slf4j
public class Receiver {

    @RabbitHandler
    @RabbitListener(queues = "immediate_queue_test1")
    public void immediateProcess(String message) {
        DataContent dataContent = JsonUtils.jsonToPojo(message, DataContent.class);
        Channel receiverChannel = UserChannelRel.get(dataContent.getChatMsgNio().getReceiverId());
        if(dataContent.getAction() == MsgActionEnum.CHAT.type &&  receiverChannel != null) {
            Channel findChannel = ChatHandler.users.find(receiverChannel.id());
            log.info("接收消息: " + dataContent.toString());
            if(findChannel != null) {
                log.info("channelId: " + receiverChannel.id() + " 用户在线");
                //用户在线
                findChannel.writeAndFlush(
                        new TextWebSocketFrame(
                                JsonUtils.objectToJson(dataContent)));
            } else {
                log.info("channelId: " + receiverChannel.id() + " 用户离线");
                //用户离线 TODO 推送消息
            }
        }
    }
}
