package com.example.agent.plugin.mysql;

import com.example.agent.core.plugin.enhance.ClassEnhancePluginDefiner;
import com.example.agent.core.plugin.intercetor.ConstructorMethodsInterceptorPoint;
import com.example.agent.core.plugin.intercetor.InstanceMethodsInterceptorPoint;
import com.example.agent.core.plugin.intercetor.StaticMethodsInterceptorPoint;
import com.example.agent.core.plugin.matcher.ClassMatcher;
import com.example.agent.core.plugin.matcher.ClassNameMatcher;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * MySQL 插件
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-19 00:21:39
 */
public class MySQLInstrumentation extends ClassEnhancePluginDefiner {

    private static final String SERVER_PREPARED_STATEMENT = "com.mysql.cj.jdbc.ServerPreparedStatement";
    private static final String CLIENT_PREPARED_STATEMENT = "com.mysql.cj.jdbc.ClientPreparedStatement";
    private static final String INTERCEPTOR = "com.example.agent.plugin.mysql.interceptor.MySQLInterceptor";


    @Override
    protected ClassMatcher enhanceClass() {
        return ClassNameMatcher.byClassNameMatcher(SERVER_PREPARED_STATEMENT, CLIENT_PREPARED_STATEMENT);
    }

    @Override
    protected InstanceMethodsInterceptorPoint[] getInstanceMethodsInterceptorPoints() {
        return new InstanceMethodsInterceptorPoint[] {
                new InstanceMethodsInterceptorPoint() {

                    @Override
                    public ElementMatcher<MethodDescription> getMethodMatcher() {
                        return named("execute")
                                .or(named("executeQuery"))
                                .or(named("executeUpdate"));
                    }

                    @Override
                    public String getMethodInterceptor() {
                        return INTERCEPTOR;
                    }
                }
        };
    }

    @Override
    protected ConstructorMethodsInterceptorPoint[] getConstructorMethodsInterceptorPoints() {
        return new ConstructorMethodsInterceptorPoint[0];
    }

    @Override
    protected StaticMethodsInterceptorPoint[] getStaticMethodsInterceptorPoints() {
        return new StaticMethodsInterceptorPoint[0];
    }
}
