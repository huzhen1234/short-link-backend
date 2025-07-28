package com.hutu.shortlinklink.strategy;

import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.exception.BizException;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * 短链分片算法
 * 自定义分表算法
 */
public class LinkTableShardingAlgorithm implements StandardShardingAlgorithm<String> {

    /**
     * 精准分片
     * @param collection 数据源集合
     *                             在分库时值为所有分片库的集合 databaseNames
     *                             分表时为对应分片库中所有分片表的集合 tablesNames
     * @param preciseShardingValue        分片属性，包括
     *                             logicTableName 为逻辑表，
     *                             columnName 分片健（字段），
     *                             value 为从 SQL 中解析出的分片健的值
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        //获取逻辑表
        String targetName = collection.iterator().next();
        //短链码  A23Ad1
        String value = preciseShardingValue.getValue();
        //获取短链码最后一位
        String codeSuffix =  value.substring(value.length()-1);
        //拼接 Actual table
        return targetName+"_"+codeSuffix;
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<String> rangeShardingValue) {
        return List.of();
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties properties) {

    }
}
