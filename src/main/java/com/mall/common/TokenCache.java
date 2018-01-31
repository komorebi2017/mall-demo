package com.mall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/1/12 11:40
 * @ Description:
 */
@Slf4j
public class TokenCache {



    public static final String TOKEN_PREFIX = "token_";

    //LRU算法
    /* 这个本地缓存初始化最大值是10000，如果超过10000就使用LRU算法（最少使用算法）进行清除
    * 有效期是12小时*/
    private static LoadingCache<String,String> localCache =
            CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                // 默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });
    public static void setKey(String key, String value){
        localCache.put(key, value);
    }

    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("localCache get error",e);
        }
        return null;
    }

}
