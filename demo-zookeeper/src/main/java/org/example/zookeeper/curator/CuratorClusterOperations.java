package org.example.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CuratorClusterOperations extends CuratorClusterBase {

    private String testNodePath = "/test_node";


    @Test
    public void testCluster() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        while (true) {
            try {
                byte[] data = curatorFramework.getData().forPath("/test");
                System.out.println("data = " + new String(data));

                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("重新连接...");
                TimeUnit.SECONDS.sleep(3);
            }
        }
    }

}
