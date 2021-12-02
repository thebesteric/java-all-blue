package org.example.sharding.jdbc.algorithm.standard;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.math.BigInteger;
import java.util.Collection;

public class MyDatabasePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> preciseShardingValue) {
        // 值：cid 对应的值，需要根据 cid 都值，计算取哪一个数据库
        Long value = preciseShardingValue.getValue();
        // course_$->{cid%2+1} => course_1 or course_2
        BigInteger shardingValue = BigInteger.valueOf(value);
        BigInteger targetKey = shardingValue.mod(new BigInteger("2")).add(new BigInteger("1"));
        // course_1 or course_2
        String actualDatabaseName = "m" + targetKey;

        if (availableTargetNames.contains(actualDatabaseName)) {
            return actualDatabaseName;
        }

        throw new UnsupportedOperationException("route database " + actualDatabaseName + " is not supported, please check your config");
    }
}
