package org.example.dubbo.simulate.framework.register;

import org.example.dubbo.simulate.framework.URL;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class RemoteRegistryCenter {

    private static Map<String, Set<URL>> REGISTRY = new ConcurrentHashMap<>();

    public static void registry(String interfaceName, URL url) {
        Set<URL> urls = REGISTRY.get(interfaceName);
        if (urls == null) {
            urls = new HashSet<>();
        }
        urls.add(url);

        REGISTRY.put(interfaceName, urls);
        // 模拟存储到数据库
        saveFile();
    }

    public static Set<URL> get(String interfaceName) {
        // 从数据库获取
        REGISTRY = getFile();
        return REGISTRY.get(interfaceName);
    }

    public static void saveFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./temp.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(REGISTRY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Set<URL>> getFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("./temp.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (Map<String, Set<URL>>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("cannot found the REGISTRY");
    }


}
