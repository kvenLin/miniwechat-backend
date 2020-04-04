package com.clf.cloud.nettyserver.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: clf
 * @Date: 19-12-30
 * @Description:
 */
@Slf4j
public class WSServer {

    private static class SingletonWSServer{
        static final WSServer instance = new WSServer();
    }
    public static WSServer getInstance() {
        return SingletonWSServer.instance;
    }
    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    public WSServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new WSServerInitializer());
        log.warn("netty websocket server 启动完毕。。。。");
    }
    public void  start(int port) {
        this.future = server.bind(port);
    }
}
