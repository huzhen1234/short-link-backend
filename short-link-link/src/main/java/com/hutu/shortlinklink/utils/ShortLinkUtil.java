package com.hutu.shortlinklink.utils;

import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class ShortLinkUtil {
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

//    private final ShardingWeightConfig weightConfig;

    /**
     * 存储数据表位置编号
     */
    private static final List<String> tableSuffixList = new ArrayList<>();

    /**
     * 存储数据库位置编号
     */
    private static final List<String> dbPrefixList = new ArrayList<>();

    static {
        //配置启用那些表的后缀
        tableSuffixList.add("0");
        tableSuffixList.add("a");

        //配置启用那些库的前缀
        dbPrefixList.add("0");
        dbPrefixList.add("1");
        dbPrefixList.add("a");
    }


    /**
     * murmurhash算法
     */
    public long murmurHash32(String param) {
        return Hashing.murmur3_32().hashUnencodedChars(param).padToLong();
    }


    /**
     * 创建短链
     * 设置 库 + 短链 + 表
     * @param originalUrl 原始长链
     * @return db编码+6位短链编码
     */
/*    public String createShortLinkCode(String originalUrl) {
        // 2. 按权重选择库后缀
        String dbSuffix = selectByWeight(
                weightConfig.getDatabaseWeights()
        );
        // 3. 按权重选择表后缀
        Map<String, Integer> tableWeights = weightConfig.getTableWeights(dbSuffix);

        String tableSuffix = tableWeights.isEmpty() ?
                // 新库无权重配置时随机选择（例如刚扩容还未初始化表权重）
                String.valueOf(ThreadLocalRandom.current().nextInt(3)) :
                selectByWeight(tableWeights);
        // 4. 组合8位码：库位 + 短码 + 表位
        return dbSuffix + encodeToBase62(murmurHash32(originalUrl)) + tableSuffix;
//        return ShardingDBConfig.getRandomDBPrefix() + encodeToBase62(murmurHash32(originalUrl)) + ShardingTableConfig.getRandomTableSuffix();
    }*/


    public String createShortLinkCode(String originalUrl) {
        // 同样的originalUrl生成的murmurhash时一致的
        long murmurhash = murmurHash32(originalUrl);
        //进制转换
        String code = encodeToBase62(murmurhash);
        // 保证不同平台下短链编码唯一
        return getRandomDBPrefix(code) + code + getRandomTableSuffix(code);
    }


    /**
     * 获取随机的前缀
     * @return
     */
    public static String getRandomDBPrefix(String code){

        int hashCode = code.hashCode();

        int index = Math.abs(hashCode) % dbPrefixList.size();

        return dbPrefixList.get(index);
    }

    public static String getRandomTableSuffix(String code ){

        int hashCode = code.hashCode();

        int index = Math.abs(hashCode) % tableSuffixList.size();

        return tableSuffixList.get(index);
    }



    /**
     10进制转62进制
     */
    private String encodeToBase62(long num) {
        StringBuilder sb = new StringBuilder();
        do {
            int i = (int) (num % 62);
            sb.append(CHARS.charAt(i));
            num /= 62;
        } while (num > 0);
        return sb.reverse().toString();
    }

    public String selectByWeight(Map<String, Integer> weightMap) {
        if (weightMap.isEmpty()) {
            throw new IllegalArgumentException("Weight map is empty");
        }

        // 计算总权重
        int totalWeight = weightMap.values().stream().mapToInt(Integer::intValue).sum();

        // 生成随机权重值
        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);

        // 遍历选择
        int currentWeight = 0;
        for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {
            currentWeight += entry.getValue();
            if (randomWeight < currentWeight) {
                return entry.getKey();
            }
        }
        // 理论上不会执行到此处
        return weightMap.keySet().iterator().next();
    }
}
