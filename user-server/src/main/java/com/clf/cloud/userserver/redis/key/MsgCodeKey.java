package com.clf.cloud.userserver.redis.key;

/**
 * @Author: clf
 * @Date: 2020-02-18
 * @Description: TODO
 */
public class MsgCodeKey extends BasePrefix {
    public MsgCodeKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static MsgCodeKey getByMobile = new MsgCodeKey(1 * 60, "mobile");
    public static MsgCodeKey getByClientIP = new MsgCodeKey(1 * 60, "clientIp");
}
