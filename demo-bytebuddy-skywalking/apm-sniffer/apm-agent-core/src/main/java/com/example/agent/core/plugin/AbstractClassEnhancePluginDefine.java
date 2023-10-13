package com.example.agent.core.plugin;

import com.example.agent.core.plugin.intercetor.ConstructorMethodsInterceptorPoint;
import com.example.agent.core.plugin.intercetor.InstanceMethodsInterceptorPoint;
import com.example.agent.core.plugin.intercetor.StaticMethodsInterceptorPoint;
import com.example.agent.core.plugin.matcher.ClassMatcher;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

/**
 * 类的增强插件定义类，是所有插件的顶级类
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-19 00:02:36
 */
@Slf4j
public abstract class AbstractClassEnhancePluginDefine {

    // 新属性名称
    public static final String CONTEXT_ATTR_NAME = "_$EnhancedClassField$_";

    /**
     * 获取当前插件要增强的类
     *
     * @return ClassMatcher
     * @author wangweijun
     * @since 2023/9/19 00:05
     */
    protected abstract ClassMatcher enhanceClass();

    /**
     * 实例方法的拦截点
     *
     * @return InstanceMethodsInterceptorPoint[]
     * @author wangweijun
     * @since 2023/9/19 00:10
     */
    protected abstract InstanceMethodsInterceptorPoint[] getInstanceMethodsInterceptorPoints();

    /**
     * 构造方法的拦截点
     *
     * @return ConstructorMethodsInterceptorPoint[]
     * @author wangweijun
     * @since 2023/9/19 00:16
     */
    protected abstract ConstructorMethodsInterceptorPoint[] getConstructorMethodsInterceptorPoints();

    /**
     * 静态方法的拦截点
     *
     * @return StaticMethodsInterceptorPoint[]
     * @author wangweijun
     * @since 2023/9/19 00:16
     */
    protected abstract StaticMethodsInterceptorPoint[] getStaticMethodsInterceptorPoints();

    /**
     * 增强类的主入口
     *
     * @param builder
     * @param typeDescription
     * @param classLoader
     * @param enhanceContext
     * @return Builder<?>
     * @author wangweijun
     * @since 2023/9/19 17:24
     */
    public DynamicType.Builder<?> define(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, EnhanceContext enhanceContext) {
        // com.example.agent.plugin.springmvc.RestControllerInstrumentation
        String pluginDefinerName = this.getClass().getName();
        // xxx.xxx.controller.UserController
        String typeName = typeDescription.getTypeName();

        log.debug("开始使用 {} 来增强 {}", pluginDefinerName, typeName);
        // 开发增强方法
        DynamicType.Builder<?> newBuilder = this.enhance(builder, typeDescription, classLoader, enhanceContext);
        // 设置为已经进行增强处理
        enhanceContext.initializationStageCompleted();
        log.debug("结束使用 {} 来增强 {}", pluginDefinerName, typeName);

        return newBuilder;
    }

    private DynamicType.Builder<?> enhance(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, EnhanceContext enhanceContext) {
        // 增强静态方法
        builder = this.enhanceClass(builder, typeDescription, classLoader);
        // 增强实例方法和构造方法
        builder = this.enhanceInstance(builder, typeDescription, classLoader, enhanceContext);
        return builder;
    }

    /**
     * 增强静态方法
     *
     * @param builder
     * @param typeDescription
     * @param classLoader
     * @return Builder<?>
     * @author wangweijun
     * @since 2023/9/20 00:36
     */
    protected abstract DynamicType.Builder<?> enhanceClass(DynamicType.Builder<?> builder,
                                                         TypeDescription typeDescription, ClassLoader classLoader);

    /**
     * 增强实例方法和构造方法
     *
     * @param builder
     * @param typeDescription
     * @param classLoader
     * @param enhanceContext
     * @return Builder<?>
     * @author wangweijun
     * @since 2023/9/20 00:37
     */
    protected abstract DynamicType.Builder<?> enhanceInstance(DynamicType.Builder<?> builder,
                                                              TypeDescription typeDescription, ClassLoader classLoader, EnhanceContext enhanceContext);
}
