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
 * 自定义分库算法
 */
public class LinkDataBaseShardingAlgorithm implements StandardShardingAlgorithm<String> {

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

        //获取短链码第一位，即库位
        String codePrefix = preciseShardingValue.getValue().substring(0, 1);
        for (String targetName : collection) {
            //获取库名的最后一位，真实配置的ds
            String targetNameSuffix = targetName.substring(targetName.length() - 1);
            //如果一致则返回
            if (codePrefix.equals(targetNameSuffix)) {
                return targetName;
            }
        }
        //抛异常
        throw new BizException(BizCodeEnum.DB_ROUTE_NOT_FOUND);
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
