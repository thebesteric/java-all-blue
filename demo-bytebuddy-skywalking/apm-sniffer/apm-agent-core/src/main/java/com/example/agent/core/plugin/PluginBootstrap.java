package com.example.agent.core.plugin;

import com.example.agent.core.plugin.load.ApmAgentClassLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * PluginBootstrap
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-21 13:29:16
 */
@Slf4j
public class PluginBootstrap {

    /**
     * 加载所有生效的插件，因为是自定义路径下的 jar 包
     * 1. 因为 plugins 目录与 apm-agent.jar 平级，所以先获取到 apm-agent.jar 的路径
     * 2. 再使用自定义类加载器进行加载 plugins 目录下的插件
     *
     * @return List<AbstractClassEnhancePluginDefiner>
     * @author wangweijun
     * @since 2023/9/21 13:30
     */
    public List<AbstractClassEnhancePluginDefine> loadPlugins() {
        ApmAgentClassLoader.initDefaultLoader();

        List<AbstractClassEnhancePluginDefine> plugins = new ArrayList<>();

        // 解析并获取每个 Jar 包内的 apm-plugin.def 文件
        PluginResourceResolver pluginResourceResolver = new PluginResourceResolver();
        List<URL> resources = pluginResourceResolver.getResources();

        if (resources == null || resources.isEmpty()) {
            log.info("Not fount apm-plugin.def file");
            return plugins;
        }

        for (URL resource : resources) {
            try {
                PluginCfg.INSTANCE.load(resource.openStream());
            } catch (IOException e) {
                log.error("Plugin define file {} init failed", resource, e);
            }
        }

        List<PluginDefine> pluginDefines = PluginCfg.INSTANCE.getPluginDefines();
        // 拿到全类名通过反射获取到对象，这个对象是插件的定义对象，如：MySQLInstrumentation
        // 并且都是 AbstractClassEnhancePluginDefine 的子类
        for (PluginDefine pluginDefine : pluginDefines) {
            try {
                Class<?> aClass = Class.forName(pluginDefine.getDefineClass(), true, ApmAgentClassLoader.getDefault());
                AbstractClassEnhancePluginDefine plugin = (AbstractClassEnhancePluginDefine) aClass.getDeclaredConstructor().newInstance();
                plugins.add(plugin);
            } catch (Exception e) {
                log.error("Load class error: {}", pluginDefine.getDefineClass(), e);
            }
        }

        return plugins;
    }
}
