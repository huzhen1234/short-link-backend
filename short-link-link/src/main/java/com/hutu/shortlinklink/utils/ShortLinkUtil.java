package com.hutu.shortlinklink.utils;

import com.google.common.hash.Hashing;
import com.hutu.shortlinklink.config.ShardingWeightConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class ShortLinkUtil {
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final ShardingWeightConfig weightConfig;

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
    public String createShortLinkCode(String originalUrl) {
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
