package org.example.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class BloomFilterTest {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");

        RedissonClient redisson = Redisson.create(config);

        RBloomFilter<Object> bloomNameListFilter = redisson.getBloomFilter("bloom:name:list");
        // 设置预计元素个数，误差率
        bloomNameListFilter.tryInit(100000L, 0.03);

        bloomNameListFilter.add("zhangsan");

        System.out.println(bloomNameListFilter.contains("zhangsan"));
        System.out.println(bloomNameListFilter.contains("lisi"));
    }
}
