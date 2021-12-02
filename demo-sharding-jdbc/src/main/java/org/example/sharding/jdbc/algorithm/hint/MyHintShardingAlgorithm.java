package org.example.sharding.jdbc.algorithm.hint;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.Collection;
import java.util.Collections;

public class MyHintShardingAlgorithm implements HintShardingAlgorithm<Integer> {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, HintShardingValue<Integer> hintShardingValue) {
        String actualTableName = hintShardingValue.getLogicTableName() + "_" + hintShardingValue.getValues().toArray()[0];
        if(availableTargetNames.contains(actualTableName)) {
            return Collections.singletonList(actualTableName);
        }
        throw new UnsupportedOperationException("route table " + actualTableName + " is not supported, please check your config");
    }
}
