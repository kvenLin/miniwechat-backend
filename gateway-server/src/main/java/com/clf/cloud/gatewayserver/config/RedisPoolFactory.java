package com.clf.cloud.gatewayserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description: 配置jedis链接池
 */
@Service
public class RedisPoolFactory {
    @Autowired
    private RedisConfig redisConfig;

    @Bean
    public JedisPool jedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
        JedisPool jedisPool = new JedisPool(poolConfig
                , redisConfig.getHost()
                , redisConfig.getPort()
                , redisConfig.getTimeout() * 1000
                , null
                , 1);
        return jedisPool;
    }
}
