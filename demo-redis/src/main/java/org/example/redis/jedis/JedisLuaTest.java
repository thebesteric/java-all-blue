package org.example.redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;

public class JedisLuaTest {
    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(30 * 1000);

        try (JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 3000, null)) {
            // 获取一个连接对象
            Jedis jedis = jedisPool.getResource();

            String productNo = "product_stock_10001";
            String productStockCount = "15";
            // 初始化商品库存
            jedis.set(productNo, productStockCount);

            String script = " local count = redis.call('get', KEYS[1]) " +
                    " local count = tonumber(count) " +
                    " local sale = tonumber(ARGV[1]) " +
                    " if count >= sale then " +
                    "   redis.call('set', KEYS[1], count - sale) " +
                    // 模拟语法报错，触发回滚
                    // "   error == 0 " +
                    "   return 1 " +
                    " end " +
                    " return 0 ";
            Object result = jedis.eval(script, Arrays.asList(productNo), Arrays.asList("10"));
            System.out.println(result);
        }
    }
}
