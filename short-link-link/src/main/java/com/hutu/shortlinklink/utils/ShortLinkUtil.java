package com.hutu.shortlinklink.utils;

import com.google.common.hash.Hashing;
import com.hutu.shortlinkcommon.util.IDUtil;
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
        // 上面生成的code是不变的，然后前缀和后缀是变化的，就会导致不一致。通过不变的来生成变化的。使用hash方式
        return getRandomDBPrefix(code) + code + getRandomTableSuffix(code);
    }


    /**
     * code是不变的，
     */
    public static String getRandomDBPrefix(String code){
        // hash方式，相同的hashcode一致
        int hashCode = code.hashCode();
        int index = Math.abs(hashCode) % dbPrefixList.size();
        return dbPrefixList.get(index);
    }

    public static String getRandomTableSuffix(String code ){
        // hash方式，相同的hashcode一致
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

    /**
     * URL增加前缀
     * 目的：避免短链码重复，相同的原始URL可以生成不同的短链code
     * 可以投递到不同的平台，比如投递到：抖音，快手，微博等等
     */
    public String addUrlPrefix(String url){
        return IDUtil.generateSnowflakeId()+"&"+url;

    }

    /**
     * 移除URL前缀
     * 使用前移除掉
     */
    public String removeUrlPrefix(String url){
        return url.substring(url.indexOf("&")+1);
    }

    /**
     * 如果短链码重复，则调用这个方法
     * url前缀的编号递增1
     * 如果还是用雪花算法，TODO 则容易C端和B端不一致，所以采用编号递增1的方式
     * 123132432212&https://xxx.com
     */
    public String addUrlPrefixVersion(String url){
        //随机id
        String version = url.substring(0,url.indexOf("&"));
        //原始地址
        String originalUrl = url.substring(url.indexOf("&")+1);
        //新id
        Long newVersion = Long.parseLong(version)+1;
        return newVersion + "&"+originalUrl;
    }
}
