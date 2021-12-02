package org.example.sharding.jdbc.algorithm.standard;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.math.BigInteger;
import java.util.Collection;

public class MyTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    // select * from course where cid = ?
    // select * from course where cid in (?, ?)
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> preciseShardingValue) {
        // 逻辑表名：course
        String logicTableName = preciseShardingValue.getLogicTableName();
        // 列名：cid
        String columnName = preciseShardingValue.getColumnName();
        // 值：cid 对应的值，需要根据 cid 都值，计算取哪一个表
        Long value = preciseShardingValue.getValue();
        // course_$->{cid%2+1} => course_1 or course_2
        BigInteger shardingValue = BigInteger.valueOf(value);
        BigInteger targetKey = shardingValue.mod(new BigInteger("2")).add(new BigInteger("1"));
        // course_1 or course_2
        String actualTableName = logicTableName + "_" + targetKey;

        if (availableTargetNames.contains(actualTableName)) {
            return actualTableName;
        }

        throw new UnsupportedOperationException("route table " + actualTableName + " is not supported, please check your config");
    }
}
