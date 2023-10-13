package com.example.agent.core.plugin.matcher;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.Arrays;
import java.util.List;

/**
 * 类名匹配器：同时匹配多个类名的情况
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-19 00:33:19
 */
public class ClassNameMatcher implements IndirectMatcher {

    private final List<String> classNames;

    private ClassNameMatcher(String[] classNames) {
        if (classNames == null) {
            throw new IllegalArgumentException("class names cannot be null");
        }
        this.classNames = Arrays.asList(classNames);
    }

    /**
     * 要求是 or 的关系
     * 比如：named("xxx").or(named("yyy"))
     *
     * @return Junction<TypeDescription>
     * @author wangweijun
     * @since 2023/9/19 11:39
     */
    @Override
    public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
        ElementMatcher.Junction<TypeDescription> junction = null;
        for (String className : classNames) {
            if (junction == null) {
                junction = ElementMatchers.named(className);
            } else {
                junction = junction.or(ElementMatchers.named(className));
            }
        }
        return junction;
    }

    @Override
    public boolean isMatch(TypeDescription typeDescription) {
        return classNames.contains(typeDescription.getTypeName());
    }

    public static IndirectMatcher byClassNameMatcher(String... classNames) {
        return new ClassNameMatcher(classNames);
    }
}
