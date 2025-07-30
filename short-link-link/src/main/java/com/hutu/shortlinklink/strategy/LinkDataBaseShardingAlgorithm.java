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
        String code = preciseShardingValue.getValue();
        String dbSuffix = code.substring(0, 1); // 提取第一位库标识
        return collection.stream()
                .filter(target -> target.endsWith(dbSuffix))
                .findFirst()
                .orElseThrow(() -> new BizException(BizCodeEnum.DB_ROUTE_NOT_FOUND));
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
