package com.example.agent.core.plugin.enhance;

import com.example.agent.core.plugin.AbstractClassEnhancePluginDefine;
import com.example.agent.core.plugin.EnhanceContext;
import com.example.agent.core.plugin.intercetor.ConstructorMethodsInterceptorPoint;
import com.example.agent.core.plugin.intercetor.InstanceMethodsInterceptorPoint;
import com.example.agent.core.plugin.intercetor.StaticMethodsInterceptorPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatcher;

import java.text.MessageFormat;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.not;

/**
 * 所有的插件都必须继承直接或间接继承此类，此类完成类 transform 中指定的 method 和 intercept
 * ex: DynamicType.Builder<?> builder = builder.method(xx).intercept(MethodDelegation.to(yy))
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-20 00:39:52
 */
public abstract class ClassEnhancePluginDefiner extends AbstractClassEnhancePluginDefine {

    @Override
    protected DynamicType.Builder<?> enhanceClass(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader) {
        StaticMethodsInterceptorPoint[] staticMethodsInterceptorPoints = getStaticMethodsInterceptorPoints();
        if (staticMethodsInterceptorPoints == null || staticMethodsInterceptorPoints.length == 0) {
            return builder;
        }
        // 增强构造方法
        String typeName = typeDescription.getTypeName();
        for (StaticMethodsInterceptorPoint staticMethodsInterceptorPoint : staticMethodsInterceptorPoints) {
            String methodInterceptor = staticMethodsInterceptorPoint.getMethodInterceptor();
            if (methodInterceptor == null || methodInterceptor.isEmpty()) {
                String message = MessageFormat.format("{0} 没有提供拦截器", typeName);
                throw new IllegalArgumentException(message);
            }
            ElementMatcher<MethodDescription> methodMatcher = staticMethodsInterceptorPoint.getMethodMatcher();
            builder = builder.method(isStatic().and(methodMatcher))
                    .intercept(MethodDelegation.withDefaultConfiguration()
                            .to(new StaticMethodsInterceptor(methodInterceptor, classLoader)));

        }
        return builder;
    }

    @Override
    protected DynamicType.Builder<?> enhanceInstance(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, EnhanceContext enhanceContext) {
        ConstructorMethodsInterceptorPoint[] constructorMethodsInterceptorPoints = getConstructorMethodsInterceptorPoints();
        InstanceMethodsInterceptorPoint[] instanceMethodsInterceptorPoints = getInstanceMethodsInterceptorPoints();
        boolean existConstructorMethodsInterceptorPoints = constructorMethodsInterceptorPoints != null && constructorMethodsInterceptorPoints.length > 0;
        boolean existInstanceMethodsInterceptorPoints = instanceMethodsInterceptorPoints != null && instanceMethodsInterceptorPoints.length > 0;
        if (!existConstructorMethodsInterceptorPoints && !existInstanceMethodsInterceptorPoints) {
            return builder;
        }

        String typeName = typeDescription.getTypeName();

        // 为字节码增加属性，对于同一个类，只需要执行一次
        // typeDescription 不是 EnhancedInstance 的子类或实现类，并且没有设置扩展字段
        if (!typeDescription.isAssignableTo(EnhancedInstance.class) && !enhanceContext.isObjectExtended()) {
            builder = builder.defineField(CONTEXT_ATTR_NAME, Object.class, Opcodes.ACC_PRIVATE | Opcodes.ACC_VOLATILE)
                    // 成为 EnhancedInstance 的实现类
                    .implement(EnhancedInstance.class)
                    .intercept(FieldAccessor.ofField(CONTEXT_ATTR_NAME));
            // 设置为已添加扩展字段
            enhanceContext.objectExtendedCompleted();
        }


        // 增强构造方法
        if (existConstructorMethodsInterceptorPoints) {
            for (ConstructorMethodsInterceptorPoint constructorMethodsInterceptorPoint : constructorMethodsInterceptorPoints) {
                String constructorInterceptor = constructorMethodsInterceptorPoint.getConstructorInterceptor();
                if (constructorInterceptor == null || constructorInterceptor.isEmpty()) {
                    String message = MessageFormat.format("{0} 没有提供拦截器", typeName);
                    throw new IllegalArgumentException(message);
                }
                ElementMatcher<MethodDescription> constructorMatcher = constructorMethodsInterceptorPoint.getConstructorMatcher();
                builder = builder.constructor(constructorMatcher)
                        .intercept(SuperMethodCall.INSTANCE.andThen(
                                MethodDelegation.withDefaultConfiguration()
                                        .to(new ConstructorInterceptor(constructorInterceptor, classLoader)))
                        );
            }
        }

        // 增强实例方法
        if (existInstanceMethodsInterceptorPoints) {
            for (InstanceMethodsInterceptorPoint instanceMethodsInterceptorPoint : instanceMethodsInterceptorPoints) {
                String methodInterceptor = instanceMethodsInterceptorPoint.getMethodInterceptor();
                if (methodInterceptor == null || methodInterceptor.isEmpty()) {
                    String message = MessageFormat.format("{0} 没有提供拦截器", typeName);
                    throw new IllegalArgumentException(message);
                }
                ElementMatcher<MethodDescription> methodMatcher = instanceMethodsInterceptorPoint.getMethodMatcher();
                builder = builder.method(not(isStatic()).and(methodMatcher))
                        .intercept(MethodDelegation.withDefaultConfiguration()
                                .to(new InstanceMethodsInterceptor(methodInterceptor, classLoader)));
            }
        }


        return builder;
    }
}
