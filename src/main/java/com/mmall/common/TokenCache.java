package com.mmall.common;/*
 *  cteate by tao on 2018/2/26.
 */


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TokenCache {
    //声明日志
    private static Logger log = LoggerFactory.getLogger(TokenCache.class);
    public static final String Token_Profix = "token_";
    //静态内存块
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                @Override
                //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setCache(String key, String value) {
        localCache.put(key,value);
    }

    public static String getCache(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            log.error("localCache get error",e);
            return null;
        }

    }
}
