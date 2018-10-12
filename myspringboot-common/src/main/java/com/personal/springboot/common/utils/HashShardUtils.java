package com.personal.springboot.common.utils;

import org.apache.commons.lang3.StringUtils;

import redis.clients.util.MurmurHash;

/**
 * DESCRIPTION : 通过hash在同一个db中分片（命名空间）
 */
public class HashShardUtils {
    /**
     * 每个命名空间只能放1000左右的数据
     */
    private static final int maxHashSize = 1000;

    /**
     * Hash算法
     */
    private static MurmurHash murmurHash = new MurmurHash();

    private static Long getShardValue(String shardkey) {
        long hashValue = murmurHash.hash(shardkey.getBytes());
        return hashValue / maxHashSize;
    }

    /**
     * 命名空间格式
     *
     * @param shardkey
     * @param namespace
     * @return
     */
    public static String getShardNamespace(String shardkey, String namespace) {
        if(StringUtils.isBlank(namespace)){
            return getShardValue(shardkey).toString();
        }
        return namespace + "-" + getShardValue(shardkey);
    }
    
    /**
     * 命名空间格式
     * @param namespace
     */
    public static String getShardNamespace(String namespace) {
        return namespace.toLowerCase();
    }

}
