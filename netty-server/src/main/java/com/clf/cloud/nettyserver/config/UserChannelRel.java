package com.clf.cloud.nettyserver.config;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: clf
 * @Date: 2020-02-13
 * @Description: 用户id和channel的关联关系处理
 */
public class UserChannelRel {
    private static Map<String, Channel> manager = new ConcurrentHashMap<>();

    public static void put(String senderId, Channel channel) {
        manager.put(senderId, channel);
    }

    public static Channel get(String senderId) {
        return manager.get(senderId);
    }

    public static void outPut() {
        for (Map.Entry<String, Channel> entry : manager.entrySet()) {
            System.out.println("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }
}
