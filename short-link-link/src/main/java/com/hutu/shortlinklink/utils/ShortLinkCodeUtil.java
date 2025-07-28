package com.hutu.shortlinklink.utils;

import com.google.common.hash.Hashing;
import com.hutu.shortlinklink.strategy.ShardingDBConfig;
import com.hutu.shortlinklink.strategy.ShardingTableConfig;

public class ShortLinkCodeUtil {

    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * murmurhash算法
     *
     * @param param
     * @return
     */
    public static long murmurHash32(String param) {
        return Hashing.murmur3_32().hashUnencodedChars(param).padToLong();
    }


    /**
     * 创建短链
     * 设置 库 + 短链 + 表
     * @param originalUrl 原始长链
     * @return db编码+6位短链编码
     */
    public static String createShortLinkCode(String originalUrl) {
        return ShardingDBConfig.getRandomDBPrefix() + encodeToBase62(murmurHash32(originalUrl)) + ShardingTableConfig.getRandomTableSuffix();
    }


    /**
     10进制转62进制
     */
    private static String encodeToBase62(long num) {
        StringBuilder sb = new StringBuilder();
        do {
            int i = (int) (num % 62);
            sb.append(CHARS.charAt(i));
            num /= 62;
        } while (num > 0);
        return sb.reverse().toString();
    }
}
