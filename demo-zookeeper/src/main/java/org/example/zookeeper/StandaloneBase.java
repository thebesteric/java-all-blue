package org.example.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public abstract class StandaloneBase {
    private final static String CONNECT_STR = "127.0.0.1:2181";
    private final static int SESSION_TIMEOUT = 30 * 1000;
    private ZooKeeper zooKeeper = null;

    public StandaloneBase() {
        try {
            zooKeeper = new ZooKeeper(getConnectStr(), getSessionTimeout(), new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.None
                            && event.getState() == Event.KeeperState.SyncConnected) {
                        System.out.println("连接已经建立");
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getConnectStr() {
        return CONNECT_STR;
    }

    protected int getSessionTimeout() {
        return SESSION_TIMEOUT;
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
}
