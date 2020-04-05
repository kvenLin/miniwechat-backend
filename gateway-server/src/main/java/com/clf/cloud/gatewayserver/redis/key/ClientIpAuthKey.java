package com.clf.cloud.gatewayserver.redis.key;

/**
 * @Author: clf
 * @Date: 2020-02-18
 * @Description: TODO
 */
public class ClientIpAuthKey extends BasePrefix {
    public ClientIpAuthKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static ClientIpAuthKey getByClientIP = new ClientIpAuthKey(3 * 60, "clientIp");
}
