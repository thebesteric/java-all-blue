package org.example.redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisSingleTest {
    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);

        try (JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 3000, null)) {
            // 获取一个连接对象
            Jedis jedis = jedisPool.getResource();

            // redis 普通操作
            System.out.println(jedis.set("single-1", System.currentTimeMillis() + ""));
            System.out.println(jedis.get("single-1"));
        }
    }
}
