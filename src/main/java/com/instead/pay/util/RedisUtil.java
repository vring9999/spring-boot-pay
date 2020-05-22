package com.instead.pay.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis操作工具类.
 * @author Alex
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void not_Refresh_time(String key,String value){
        try {
            redisTemplate.opsForValue().set(key, value, 0);
        }catch (Exception E){
            E.printStackTrace();
        }
    }


//    public boolean insMap(String key,Map<Object,Object> map,int time,TimeUnit unit){
//        boolean flag = false;
//        try {
//            redisTemplate.opsForHash().putAll(key,map);
//            redisTemplate.expire(key,time,unit);
//            flag = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return flag;
//    }
//
//    public Map<Object, Object> getMap(final String key){
//        Map<Object, Object> resultMap = redisTemplate.opsForHash().entries(key);
//        return resultMap;
//    }



    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 写入缓存
     */
    public boolean ins(final String key, String value,int time,TimeUnit unit) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value,time, unit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     */
    public boolean ins(final String key, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新缓存
     */
    public boolean set(final String key, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().getAndSet(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




    /**
     * 删除缓存
     */
    public boolean delete(final String key) {
        boolean result = false;
        try {
            redisTemplate.delete(key);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}