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
     * @param collection 当前 SQL 所涉及的分片目标名称集合。
     *                   - 如果是分库：collection 是所有数据库名（如 ds_0, ds_1）
     *                   - 如果是分表：collection 是所有实际表名（如 link_mapping_0, link_mapping_1）
     * @param preciseShardingValue        分片属性，包括
     *                             logicTableName 为逻辑表，
     *                             columnName 分片健（字段），
     *                             value 为从 SQL 中解析出的分片健的值
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        //获取逻辑表
        String targetName = preciseShardingValue.getLogicTableName();
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
