package org.example.sharding.jdbc.algorithm.complex;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyDatabaseComplexShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long>  {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        Range<Long> cidRange = complexKeysShardingValue.getColumnNameAndRangeValuesMap().get("cid");
        Collection<Long> userIds = complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("user_id");

        Long upperVal = cidRange.upperEndpoint();
        Long lowerVal = cidRange.lowerEndpoint();

        List<String> list = new ArrayList<>();
        for (Long userId : userIds) {
            BigInteger key = BigInteger.valueOf(userId).mod(new BigInteger("2")).add(new BigInteger("1"));
            list.add("m" + key);
        }

        return list;
    }
}
