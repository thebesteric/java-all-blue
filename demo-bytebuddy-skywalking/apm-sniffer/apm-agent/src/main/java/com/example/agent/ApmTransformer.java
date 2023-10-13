package com.example.agent;

import com.example.agent.core.plugin.AbstractClassEnhancePluginDefine;
import com.example.agent.core.plugin.EnhanceContext;
import com.example.agent.core.plugin.PluginFinder;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;
import net.bytebuddy.utility.nullability.MaybeNull;

import java.security.ProtectionDomain;
import java.util.List;

/**
 * ApmTransformer
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-18 23:51:45
 */
@Slf4j
public class ApmTransformer implements AgentBuilder.Transformer {

    private PluginFinder pluginFinder;

    public ApmTransformer(PluginFinder pluginFinder) {
        this.pluginFinder = pluginFinder;
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, @MaybeNull ProtectionDomain protectionDomain) {
        System.out.println("========== transform ==========");
        return transform(builder, typeDescription, classLoader, javaModule);
    }

    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
        String actualClassName = typeDescription.getActualName();
        log.info("========== actualClassName to transform: {} ==========", actualClassName);

        List<AbstractClassEnhancePluginDefine> pluginDefiners = pluginFinder.find(typeDescription);

        if (!pluginDefiners.isEmpty()) {
            DynamicType.Builder<?> newBuilder = builder;
            EnhanceContext enhanceContext = new EnhanceContext();
            for (AbstractClassEnhancePluginDefine pluginDefiner : pluginDefiners) {
                DynamicType.Builder<?> possibleBuilder = pluginDefiner.define(newBuilder, typeDescription, classLoader, enhanceContext);
                if (possibleBuilder != null) {
                    newBuilder = possibleBuilder;
                }
            }

            if (enhanceContext.isEnhanced()) {
                log.debug("Finished enhance for {}", typeDescription.getTypeName());
            }
            return newBuilder;
        }
        log.debug("匹配到了类: {}, 但是未找到相关插件", actualClassName);
        // return builder.method(
        //                 null
        //         )
        //         /*
        //          // springmvc
        //          new SpringmvcInterceptor()
        //          // mysql
        //          new MySQLInterceptor()
        //          */
        //         .intercept(MethodDelegation.to(null));
        return builder;

    }
}
