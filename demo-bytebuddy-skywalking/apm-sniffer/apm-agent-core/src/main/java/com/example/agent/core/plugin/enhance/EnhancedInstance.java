package com.example.agent.core.plugin.enhance;

/**
 * 所有需要增强构造或实例方法的字节码都会实现这个接口
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-21 01:01:33
 */
public interface EnhancedInstance {

    Object getApmTracingDynamicField();

    void setApmTracingDynamicField(Object obj);

}
