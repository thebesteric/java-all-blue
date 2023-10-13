package com.example.agent;

import com.example.agent.core.plugin.PluginBootstrap;
import com.example.agent.core.plugin.PluginFinder;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.scaffold.TypeValidation;

import java.lang.instrument.Instrumentation;

/**
 * ApmAgent
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-18 23:30:01
 */
@Slf4j
public class ApmAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        log.info("进入到 premain， args = {}", args);

        PluginFinder pluginFinder;

        try {
            pluginFinder = new PluginFinder(new PluginBootstrap().loadPlugins());
        }catch (Exception e) {
            log.error("初始化失败", e);
            return;
        }

        ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.ENABLED);
        AgentBuilder builder = new AgentBuilder.Default(byteBuddy);

        builder.type(pluginFinder.buildTypeMatcher())
                .transform(new ApmTransformer(pluginFinder))
                .with(new AgentListener())
                .installOn(instrumentation);
    }

}
