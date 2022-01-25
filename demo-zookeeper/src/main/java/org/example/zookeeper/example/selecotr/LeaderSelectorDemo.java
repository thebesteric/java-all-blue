package org.example.zookeeper.example.selecotr;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LeaderSelectorDemo {

    public static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    public static CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {

        client.start();

        String appName = System.getProperty("appName");

        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("i am leader now: " + appName);
                // 模拟成为 leader 的时间为 15 秒，结束后，会让出 leader，从新进行选举
                // 如果这里不结束，则他永远会是 leader
                TimeUnit.SECONDS.sleep(15);
            }
        };
        LeaderSelector selector = new LeaderSelector(client, "/cache_preheat_leader", listener);
        selector.autoRequeue();
        selector.start();

        countDownLatch.await();
    }

}
