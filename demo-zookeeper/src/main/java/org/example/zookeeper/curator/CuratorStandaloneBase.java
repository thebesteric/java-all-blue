package org.example.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

public class CuratorStandaloneBase {
    private final static String CONNECT_STR = "127.0.0.1:2181";
    private final static Integer SESSION_TIMEOUT = 30 * 1000;
    private final static Integer CONNECTION_TIMEOUT = 5 * 1000;
    private final CuratorFramework curatorFramework;

    public CuratorStandaloneBase() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(5000, 30);
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(getConnectStr())
                .sessionTimeoutMs(getSessionTimeout())
                .connectionTimeoutMs(getConnectionTimeout())
                .retryPolicy(retryPolicy)
                .canBeReadOnly(true)
                .build();
        curatorFramework.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState state) {
                if (ConnectionState.CONNECTED == state) {
                    System.out.println("连接成功");
                }
            }
        });
        System.out.println("连接中...");
        curatorFramework.start();
    }

    public void createIfAbsent(String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat == null) {
            String s = curatorFramework.create().forPath(path);
            System.out.println("path is created: " + s);
        }
    }

    protected String getConnectStr() {
        return CONNECT_STR;
    }

    protected int getSessionTimeout() {
        return SESSION_TIMEOUT;
    }

    protected int getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }

    public CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }
}
