package com.hutu.shortlinkcommon.util;
import org.apache.shardingsphere.sharding.algorithm.keygen.SnowflakeKeyGenerateAlgorithm;
import java.util.Properties;

/**
 * 雪花算法工具类
 */
public class IDUtil {

    private static final SnowflakeKeyGenerateAlgorithm keyGenerator;

    static {
        keyGenerator = new SnowflakeKeyGenerateAlgorithm();
        // TODO
        Properties props = new Properties();
        props.setProperty("worker-id", "1");

        keyGenerator.init(props);
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
