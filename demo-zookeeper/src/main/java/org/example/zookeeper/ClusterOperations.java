package org.example.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ClusterOperations extends ClusterBase {

    @Test
    public void testReconnect() throws InterruptedException, KeeperException {
        while (true) {
            try {
                Stat stat = new Stat();
                byte[] data = getZooKeeper().getData("/test", false, stat);
                System.out.println("data = " + new String(data));
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("重新连接...");
                while (true) {
                    String state = getZooKeeper().getState().name();
                    System.out.println("state = " + state);
                    if (getZooKeeper().getState().isConnected()) {
                        break;
                    }
                    TimeUnit.SECONDS.sleep(3);
                }
            }
        }
    }

}
