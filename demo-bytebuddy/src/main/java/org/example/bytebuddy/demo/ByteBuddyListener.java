package org.example.bytebuddy.demo;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

/**
 * ByteListener
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-15 14:51:01
 */
@Slf4j
public class ByteBuddyListener implements AgentBuilder.Listener {
    /**
     * 某个类将要被加载的时候，会调用此方法
     */
    @Override
    public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule javaModule, boolean b) {
        // log.info("onDiscovery typeName: {}", typeName);
    }

    /**
     * 对某个类完成类 transform，会调用此方法
     */
    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {
        log.info("onTransformation typeName: {}", typeDescription.getActualName());
    }

    /**
     * 当某个类完成类将要被加载，并且配置类被忽略或者没有匹配上要拦截的类，会调用此方法
     */
    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {
        // log.info("onIgnored typeName: {}", typeDescription.getActualName());
    }

    /**
     * 当某个类在 transform 中发生类异常时，会调用此方法
     */
    @Override
    public void onError(String typeName, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {
        log.info("onError typeName: {}", typeName);
    }

    /**
     * 当某个类处理完成时（transform，ignore，error），会调用此方法
     */
    @Override
    public void onComplete(String typeName, ClassLoader classLoader, JavaModule javaModule, boolean b) {
        // log.info("onComplete typeName: {}", typeName);
    }
}
