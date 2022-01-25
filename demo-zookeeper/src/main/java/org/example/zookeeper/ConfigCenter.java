package org.example.zookeeper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

@Slf4j
public class ConfigCenter {
    private final static String URL = "127.0.0.1:2181";
    private final static Integer SESSION_TIMEOUT = 30 * 1000;

    private static ZooKeeper zooKeeper = null;

    private static Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) throws Exception {
        semaphore.acquire();

        zooKeeper = new ZooKeeper(URL, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.None
                        && event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("连接已经建立");
                    semaphore.release();
                }
            }
        });

        semaphore.acquire();

        ConfigFile configFile = new ConfigFile();
        ConfigFile.Item item1 = new ConfigFile.Item("key1", "value1");
        ConfigFile.Item item2 = new ConfigFile.Item("key2", 100);
        configFile.setConfigFiles(Arrays.asList(item1, item2));

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = objectMapper.writeValueAsBytes(configFile);

        String configPath = "/myconfig";

        String result = zooKeeper.create(configPath, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.printf("result => %s\n",result);

        Watcher watcher = new Watcher() {
            @SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.NodeDataChanged
                        && event.getPath().equals(configPath)) {
                    byte[] data = zooKeeper.getData(configPath, this, null);
                    ConfigFile newConfigFile = objectMapper.readValue(new String(data), ConfigFile.class);
                    System.out.printf("%s 发生了数据变化 => %s\n", configPath, newConfigFile);
                    semaphore.release();
                }
            }
        };
        byte[] data = zooKeeper.getData(configPath, watcher, null);
        ConfigFile originalConfigFile = objectMapper.readValue(new String(data), ConfigFile.class);
        System.out.printf("%s 原始数据为 => %s\n", configPath, originalConfigFile);

        semaphore.acquire();

    }
}
