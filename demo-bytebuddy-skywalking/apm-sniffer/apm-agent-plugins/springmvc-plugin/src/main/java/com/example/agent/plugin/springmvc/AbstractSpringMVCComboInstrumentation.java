package com.example.agent.plugin.springmvc;

import com.example.agent.core.plugin.enhance.ClassEnhancePluginDefiner;
import com.example.agent.core.plugin.intercetor.ConstructorMethodsInterceptorPoint;
import com.example.agent.core.plugin.intercetor.InstanceMethodsInterceptorPoint;
import com.example.agent.core.plugin.intercetor.StaticMethodsInterceptorPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * SpringMVC 插件
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-19 00:21:39
 */
public abstract class AbstractSpringMVCComboInstrumentation extends ClassEnhancePluginDefiner {

    private static final String MAPPING_PKG_PREFIX = "org.springframework.web.bind.annotation";
    private static final String MAPPING_SUFFIX = "Mapping";
    private static final String INTERCEPTOR = "com.example.agent.plugin.springmvc.interceptor.SpringMVCInterceptor";

    @Override
    protected InstanceMethodsInterceptorPoint[] getInstanceMethodsInterceptorPoints() {
        return new InstanceMethodsInterceptorPoint[] {
                new InstanceMethodsInterceptorPoint() {

                    @Override
                    public ElementMatcher<MethodDescription> getMethodMatcher() {
                        return not(isStatic())
                                .and(isAnnotatedWith(nameStartsWith(MAPPING_PKG_PREFIX).and(nameEndsWith(MAPPING_SUFFIX))));
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
        return null;
    }

    @Override
    protected StaticMethodsInterceptorPoint[] getStaticMethodsInterceptorPoints() {
        return null;
    }
}
