package com.clf.cloud.nettyserver;

import com.clf.cloud.nettyserver.config.WSServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author: clf
 * @Date: 2020-02-08
 * @Description: TODO
 */
@Component
@Slf4j
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${service1.port}")
    private int port;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            WSServer.getInstance().start(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

