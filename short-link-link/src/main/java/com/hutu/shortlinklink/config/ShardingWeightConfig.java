//package com.hutu.shortlinklink.config;
//
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.Collections;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
// TODO 待完善
//@Component
//@RefreshScope // 支持动态刷新配置
//public class ShardingWeightConfig {
//    // 数据库权重配置：库后缀 -> 权重值
//    private final Map<String, Integer> databaseWeights = new ConcurrentHashMap<>();
//
//    // 表权重配置：库后缀 -> (表后缀 -> 权重)
//    private final Map<String, Map<String, Integer>> tableWeights = new ConcurrentHashMap<>();
//
//    @PostConstruct
//    public void init() {
//        // 初始库配置（旧库权重=1）
//        databaseWeights.put("0", 1);
//        databaseWeights.put("1", 1);
//        databaseWeights.put("a", 1);
//
//        // 初始表配置（所有表权重=1）
//        Map<String, Integer> initTableWeights = Map.of(
//            "0", 1,
//            "a", 1
//        );
//        tableWeights.put("0", new ConcurrentHashMap<>(initTableWeights));
//        tableWeights.put("1", new ConcurrentHashMap<>(initTableWeights));
//        tableWeights.put("a", new ConcurrentHashMap<>(initTableWeights));
//    }
//
//    // 动态添加新库（权重=3）
//    public void addDatabase(String dbSuffix) {
//        databaseWeights.put(dbSuffix, 3);
//        tableWeights.put(dbSuffix, new ConcurrentHashMap<>());
//    }
//
//    // 动态添加新表（在指定库中添加表，权重=2）
//    public void addTable(String dbSuffix, String tableSuffix) {
//        tableWeights.getOrDefault(dbSuffix, new ConcurrentHashMap<>())
//                   .put(tableSuffix, 2);
//    }
//
//    // 获取库权重配置
//    public Map<String, Integer> getDatabaseWeights() {
//        return Collections.unmodifiableMap(databaseWeights);
//    }
//
//    // 获取指定库的表权重配置
//    public Map<String, Integer> getTableWeights(String dbSuffix) {
//        return Collections.unmodifiableMap(
//            tableWeights.getOrDefault(dbSuffix, Collections.emptyMap())
//        );
//    }
//}