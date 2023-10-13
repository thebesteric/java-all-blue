package com.example.agent.core.plugin.load;

import com.example.agent.core.boot.AgentPackagePath;
import com.example.agent.core.plugin.PluginBootstrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 用于加载插件和插件的拦截器
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-21 13:34:43
 */
@Slf4j
public class ApmAgentClassLoader extends ClassLoader {

    // 用于加载插件的定义相关的类，除了插件的 interceptor，如：MySQLInstrumentation
    private static ApmAgentClassLoader DEFAULT_LOADER;

    // 自定义加载器加载的路径
    private List<File> classpath;

    // 所有的 Jar 文件
    private List<Jar> allJars;

    private final ReentrantLock jarScanLock = new ReentrantLock();

    public ApmAgentClassLoader(ClassLoader parent) {
        super(parent);
        // 获取 apm-agent.jar 的目录
        File agentJarDir = AgentPackagePath.getPath();
        // 获取到完整的 plugins 的目录
        classpath = new LinkedList<>();
        classpath.add(new File(agentJarDir, "plugins"));
    }

    public static ApmAgentClassLoader getDefault() {
        initDefaultLoader();
        return DEFAULT_LOADER;
    }

    public static void initDefaultLoader() {
        if (DEFAULT_LOADER == null) {
            DEFAULT_LOADER = new ApmAgentClassLoader(PluginBootstrap.class.getClassLoader());
        }
    }

    /**
     * loadClass -> findClass -> defineClass
     *
     * @param name aa.cc.cc.Xxx
     * @return Class<?>
     * @author wangweijun
     * @since 2023/9/22 00:22
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        List<Jar> allJars = getAllJars();
        String path = name.replace(".", "/").concat(".class");
        for (Jar jar : allJars) {
            JarEntry jarEntry = jar.jarFile.getJarEntry(path);
            if (jarEntry == null) {
                continue;
            }
            try {
                URL url = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + path);
                byte[] bytes = IOUtils.toByteArray(url);
                return defineClass(name, bytes, 0, bytes.length);
            } catch (Exception e) {
                log.error("find class {} error", name, e);
            }
        }
        throw new ClassNotFoundException("Cannot find class: " + path);
    }

    @Override
    public URL getResource(String name) {
        List<Jar> allJars = getAllJars();
        for (Jar jar : allJars) {
            JarEntry jarEntry = jar.jarFile.getJarEntry(name);
            if (jarEntry != null) {
                try {
                    return new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name);
                } catch (MalformedURLException e) {
                    log.error("get resource error: {}", name, e);
                }
            }
        }
        return null;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        List<URL> resources = new ArrayList<>();
        List<Jar> allJars = getAllJars();
        for (Jar jar : allJars) {
            JarEntry jarEntry = jar.jarFile.getJarEntry(name);
            if (jarEntry != null) {
                URL url = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name);
                resources.add(url);
            }
        }
        Iterator<URL> iterator = resources.iterator();
        return new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public URL nextElement() {
                return iterator.next();
            }
        };
    }

    private List<Jar> getAllJars() {
        if (allJars == null) {
            jarScanLock.lock();
            try {
                if (allJars == null) {
                    allJars = doGetAllJars();
                }
            } catch (Exception ex) {
                log.error("Find jar files failed", ex);
            } finally {
                jarScanLock.unlock();
            }
        }
        return allJars;
    }

    private List<Jar> doGetAllJars() {
        List<Jar> jarList = new ArrayList<>();
        for (File path : classpath) {
            if (path.exists() && path.isDirectory()) {
                String[] jarFileNames = path.list((dir, name) -> name.endsWith(".jar"));
                if (jarFileNames == null || ArrayUtils.isEmpty(jarFileNames)) {
                    continue;
                }
                for (String jarFileName : jarFileNames) {
                    try {
                        File jarSourceFile = new File(path, jarFileName);
                        Jar jar = new Jar(new JarFile(jarSourceFile), jarSourceFile);
                        jarList.add(jar);
                        log.info("Jar {} loaded", jarFileName);
                    } catch (IOException e) {
                        log.error("Jar {} load failed", jarFileName, e);
                    }
                }
            }
        }
        return jarList;
    }


    @RequiredArgsConstructor
    private static class Jar {
        // Jar 文件对于的 JarFile 对象
        private final JarFile jarFile;
        // Jar 文件对象
        private final File sourceFile;

    }
}
