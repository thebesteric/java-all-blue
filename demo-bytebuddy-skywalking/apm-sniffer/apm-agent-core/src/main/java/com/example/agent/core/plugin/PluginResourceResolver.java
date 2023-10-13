package com.example.agent.core.plugin;

import com.example.agent.core.plugin.load.ApmAgentClassLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * PluginResourceResolver
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-22 00:41:42
 */
@Slf4j
public class PluginResourceResolver {

    /**
     * 获取插件目录 plugins 下所有 jar 文件内的 apm-plugin.def 文件的 URL
     *
     * @return List<URL>
     * @author wangweijun
     * @since 2023/9/22 00:43
     */
    public List<URL> getResources() {
        List<URL> cfgUrlPath = new ArrayList<>();
        ApmAgentClassLoader apmAgentClassLoader = ApmAgentClassLoader.getDefault();
        try {
            Enumeration<URL> resources = apmAgentClassLoader.getResources("apm-plugin.def");
            while (resources.hasMoreElements()) {
                URL pluginDefUrl = resources.nextElement();
                cfgUrlPath.add(pluginDefUrl);
                log.info("Find apm plugin define url: {}", pluginDefUrl);
            }
            return cfgUrlPath;
        } catch (IOException e) {
            log.error("Read resource error", e);
        }
        return null;
    }
}
