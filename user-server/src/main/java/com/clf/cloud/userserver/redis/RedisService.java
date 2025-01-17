package com.clf.cloud.userserver.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.clf.cloud.common.utils.SerializeUtils;
import com.clf.cloud.userserver.redis.key.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 对jedis部分方法进行封装
 */
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;


    /**
     * 清空缓存数据
     */
    public void flush(){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.flushDB();
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * 获取单个对象
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 获取List集合
     * @param key
     * @return
     */
    public List<?> getList(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            if(!jedis.exists(realKey)){
                return null;
            }
            byte[] data = jedis.get(realKey.getBytes());
            return SerializeUtils.unSerializeList(data);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置List集合
     * @param key
     * @param list
     */
    public void setList(KeyPrefix prefix, String key, List<?> list){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            if(list != null && list.size() != 0){
                if (seconds <= 0){
                    jedis.set(realKey.getBytes(), SerializeUtils.serializeList(list));
                }else {
                    jedis.setex(realKey.getBytes(), seconds, SerializeUtils.serializeList(list));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置单个对象
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix,String key,T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str==null||str.length()<=0){
                return false;
            }
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            //判断是否是永不过期的从而调用不同的jedis方法
            if (seconds <= 0){
                jedis.set(realKey,str);
            }else {
                jedis.setex(realKey,seconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断是否存在
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exist(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean delete(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * 模糊删除
     * @param prefix
     */
    public void  deleteFuzzy(KeyPrefix prefix){
        deleteFuzzyKey(prefix, "");
    }

    /**
     * 删除前缀指定前缀的模糊key
     * @param prefix
     * @param fuzzyPrefix
     */
    public void deleteFuzzyKey(KeyPrefix prefix, String fuzzyPrefix){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String fuzzyKey = prefix.getPrefix() + fuzzyPrefix + "*";
            Set<String> keys = jedis.keys(fuzzyKey);
            for (String key : keys) {
                jedis.del(key);
            }
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr( realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr( realKey);
        }finally {
            returnToPool(jedis);
        }
    }


    public static <T> String beanToString(T value) {
        if (value==null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz==int.class||clazz==Integer.class){
            return ""+value;
        }else if (clazz==String.class){
            return (String) value;
        }else if (clazz==long.class||clazz==Long.class){
            return ""+value;
        }else if (clazz==List.class){
            return ""+ JSONArray.toJSONString(value);
        }else {
            return JSON.toJSONString(value);
        }

    }

    public static  <T> T stringToBean(String str,Class<T> clazz) {
        if (str==null||str.length()<=0){
            return null;
        }
        if (clazz==int.class||clazz==Integer.class){
            return (T) Integer.valueOf(str);
        }else if (clazz==String.class){
            return (T) str;
        }else if (clazz==long.class||clazz==Long.class) {
            return (T) Long.valueOf(str);
        }else if (clazz==List.class){
            return JSONArray.parseObject(str,clazz);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis!=null){
            jedis.close();
        }
    }


}
