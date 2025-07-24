package com.hutu.shortlinkcommon.util;
import org.apache.shardingsphere.sharding.algorithm.keygen.SnowflakeKeyGenerateAlgorithm;

/**
 * 雪花算法工具类
 */
public class IDUtil {

    private static final SnowflakeKeyGenerateAlgorithm keyGenerator;

    static {
        keyGenerator = new SnowflakeKeyGenerateAlgorithm();
    }

    /**
     * 生成雪花ID
     */
    public static Comparable<?> generateSnowflakeId() {
        return keyGenerator.generateKey();
    }

    public static void main(String[] args) {
        System.out.println(generateSnowflakeId());
    }
}
