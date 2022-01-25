package org.example.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CuratorBaseOperations extends CuratorStandaloneBase {

    private String testNodePath = "/test_node";


    /**
     * 递归创建节点
     */
    @Test
    public void testCreateParents() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        String path = curatorFramework
                .create()
                .creatingParentsIfNeeded().forPath("/parent/child");
        System.out.println(path);
    }

    /**
     * protection 模式，防止由于异常原因，导致僵尸节点，或者重复创建
     */
    @Test
    public void testCreate() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        String path = curatorFramework
                .create()
                .withProtection()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/protection_node", "hello".getBytes());
        System.out.println(path);
    }

    @Test
    public void testGetData() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        byte[] bytes = curatorFramework.getData().forPath(testNodePath);
        System.out.println(new String(bytes));
    }

    @Test
    public void testSetData() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        curatorFramework.setData().forPath(testNodePath, "change".getBytes());
        byte[] bytes = curatorFramework.getData().forPath(testNodePath);
        System.out.println(new String(bytes));
    }

    @Test
    public void testDelete() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        // guaranteed: 如果删除失败会进行重试，保证数据删除成功
        curatorFramework.delete().guaranteed().forPath(testNodePath);
    }

    @Test
    public void testListChildren() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        // guaranteed: 如果删除失败会进行重试，保证数据删除成功
        List<String> children = curatorFramework.getChildren().forPath("/");
        children.forEach(System.out::println);
    }

    @Test
    public void testThreadPool() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 使用线程池提供异步
        byte[] bytes = curatorFramework.getData().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework curatorFramework, CuratorEvent event) throws Exception {
                System.out.println(event);
            }
        }, executorService).forPath(testNodePath);
    }

}
