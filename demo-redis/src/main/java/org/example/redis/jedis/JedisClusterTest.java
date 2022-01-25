package org.example.redis.jedis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class JedisClusterTest {

    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);

        Set<HostAndPort> clusterNodes = new HashSet<>();
        clusterNodes.add(new HostAndPort("127.0.0.1", 8001));
        clusterNodes.add(new HostAndPort("127.0.0.1", 8002));
        clusterNodes.add(new HostAndPort("127.0.0.1", 8003));
        clusterNodes.add(new HostAndPort("127.0.0.1", 8004));
        clusterNodes.add(new HostAndPort("127.0.0.1", 8005));
        clusterNodes.add(new HostAndPort("127.0.0.1", 8006));

        try (JedisCluster jedisCluster = new JedisCluster(clusterNodes, jedisPoolConfig)) {
            System.out.println(jedisCluster.set("cluster", "202201132302"));
            System.out.println(jedisCluster.get("cluster"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
