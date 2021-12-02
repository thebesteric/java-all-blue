package org.example.sharding.jdbc.algorithm.standard;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Arrays;
import java.util.Collection;

public class MyTableRangeShardingAlgorithm implements RangeShardingAlgorithm<Long> {
    // select * from course where cid between 1 and 100;
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> rangeShardingValue) {
        // 范围上限 => 100
        Long upperVal = rangeShardingValue.getValueRange().upperEndpoint();
        // 范围下限 => 1
        Long lowerVal = rangeShardingValue.getValueRange().lowerEndpoint();
        // 逻辑表名
        String logicTableName = rangeShardingValue.getLogicTableName();
        // 因为是范围查询，所以所有表都要查
        return Arrays.asList(logicTableName + "_1", logicTableName + "_2");
    }
}
