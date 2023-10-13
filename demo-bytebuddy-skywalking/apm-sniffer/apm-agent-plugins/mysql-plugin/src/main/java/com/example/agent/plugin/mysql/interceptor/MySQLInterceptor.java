package com.example.agent.plugin.mysql.interceptor;

import com.example.agent.core.plugin.enhance.EnhancedInstance;
import com.example.agent.core.plugin.enhance.InstanceMethodsAroundInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * MySQLInterceptor
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-19 00:27:21
 */
@Slf4j
public class MySQLInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance instance, Method method, Object[] args, Class<?>[] argTypes) {
        // 添加扩展字段
        String sql = null;
        try {
            Field query = method.getDeclaringClass().getSuperclass().getDeclaredField("query");
            // query.setAccessible(true);
            Object obj = query.get(instance);
            Method asSql = obj.getClass().getMethod("asSql");
            sql = (String) asSql.invoke(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        instance.setApmTracingDynamicField(sql);
        log.info("before MySQL method: {}, args: {}", method.getName(), Arrays.toString(args));
    }

    @Override
    public Object afterMethod(EnhancedInstance instance, Method method, Object[] args, Class<?>[] argTypes, Object result) {
        // 获取扩展字段
        Object apmTracingDynamicField = instance.getApmTracingDynamicField();
        log.info("扩展字段为：{}", apmTracingDynamicField);
        log.info("after MySQL method: {}, args: {}, result: {}", method.getName(), Arrays.toString(args), result);
        return result;
    }

    @Override
    public void handleException(EnhancedInstance instance, Method method, Object[] args, Class<?>[] argTypes, Object result, Throwable throwable) {
        log.error("exception MySQL error", throwable);
    }

    public boolean isAlreadySegmentSQL(String sql) {
        return sql.replace('\r', ' ').replace('\n', ' ').replaceAll(" {2,}", " ").matches("(?i).+LIMIT [\\d+ *|\\d *, *\\d+].+");
    }
}
