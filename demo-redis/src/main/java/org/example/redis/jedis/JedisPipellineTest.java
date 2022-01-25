package org.example.redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class JedisPipellineTest {
    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(30 * 1000);

        try (JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 3000, null)) {
            // 获取一个连接对象
            Jedis jedis = jedisPool.getResource();

            Pipeline pl = jedis.pipelined();
            for (int i = 0; i < 10; i++) {
                pl.incr("pipelineKey");
                pl.set("eric" + i, i + "");
                // 模拟管道报错
                // pl.setbit("bitMapKey", -1, true);
            }
            List<Object> results = pl.syncAndReturnAll();
            // [1, OK, 2, OK, 3, OK, 4, OK, 5, OK, 6, OK, 7, OK, 8, OK, 9, OK, 10, OK]
            System.out.println(results);
        }
    }
}
