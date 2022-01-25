package org.example.zookeeper;

import lombok.SneakyThrows;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class BaseOperations extends StandaloneBase {

    private final String testNodePath = "/test_node";

    @Test
    public void testCreate() throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = getZooKeeper();
        String result = zooKeeper.create(testNodePath, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(result);
    }

    @Test
    public void testGetData() throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = getZooKeeper();
        Watcher watcher = new Watcher() {
            @SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.NodeDataChanged
                        && event.getPath().equals(testNodePath)) {
                    byte[] data = zooKeeper.getData(testNodePath, this, null);
                    System.out.printf("%s 发生了数据变化 => %s\n", testNodePath, new String(data));
                }
            }
        };
        byte[] originalData = zooKeeper.getData(testNodePath, watcher, null);
        System.out.printf("%s 原始数据为 => %s\n", testNodePath, new String(originalData));

        TimeUnit.SECONDS.sleep(60);
    }

    @Test
    public void testSetData() throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = getZooKeeper();
        Stat stat = new Stat();
        zooKeeper.getData(testNodePath, false, stat);
        System.out.println("old version" + stat.getVersion());
        stat = zooKeeper.setData(testNodePath, "hello".getBytes(), stat.getVersion());
        System.out.println("new version" + stat.getVersion());
    }

    @Test
    public void testDelete() throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = getZooKeeper();
        // -1 代表匹配所有版本
        // 任意大于 1 的代表可以指定数据版本删除
        zooKeeper.delete(testNodePath, -1);
    }

    @Test
    public void testAsync() throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = getZooKeeper();
        zooKeeper.getData(testNodePath, false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.printf("rc: %d, path: %s, ctx: %s, data: %s, stat: %s", rc, path, ctx, new String(data), stat);
            }
        }, "test");
        System.out.println("== end ==");

        TimeUnit.SECONDS.sleep(10);
    }

}
