package org.example.redis.jedis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class JedisSentinelTest {

    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(30 * 1000);

        String masterName = "mymaster";
        // 这里是 sentinel 的访问额地址
        Set<String> sentinels = new HashSet<>();
        sentinels.add(new HostAndPort("127.0.0.1", 26379).toString());
        sentinels.add(new HostAndPort("127.0.0.1", 26380).toString());
        sentinels.add(new HostAndPort("127.0.0.1", 26381).toString());

        // JedisSentinelPool 并不是与 sentinel 建立的连接池，而是通过 sentinel 发现 redis 主节点并与其建立连接
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig, 3000, null, 0);

        try (Jedis jedis = jedisSentinelPool.getResource()) {
            System.out.println(jedis.set("sentinel-1", System.currentTimeMillis() + ""));
            System.out.println(jedis.get("sentinel-1"));
        }


    }
}
