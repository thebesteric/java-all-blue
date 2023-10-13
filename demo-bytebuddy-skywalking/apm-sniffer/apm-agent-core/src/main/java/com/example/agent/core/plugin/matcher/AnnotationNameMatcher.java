package com.example.agent.core.plugin.matcher;

import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * 注解匹配器
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-19 11:17:11
 */
public class AnnotationNameMatcher implements IndirectMatcher {

    private final List<String> annotationNames;

    private AnnotationNameMatcher(String[] annotationNames) {
        if (annotationNames == null) {
            throw new IllegalArgumentException("annotation names cannot be null");
        }
        this.annotationNames = Arrays.asList(annotationNames);
    }

    /**
     * 要求是 and 的关系
     * 比如：named("xxx").and(named("yyy"))
     *
     * @return Junction<TypeDescription>
     * @author wangweijun
     * @since 2023/9/19 11:39
     */
    @Override
    public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
        ElementMatcher.Junction<TypeDescription> junction = null;
        for (String annotationName : annotationNames) {
            if (junction == null) {
                junction = isAnnotatedWith(named(annotationName));
            } else {
                junction = junction.and(isAnnotatedWith(named(annotationName)));
            }
        }
        return junction;
    }

    @Override
    public boolean isMatch(TypeDescription typeDescription) {
        List<String> annotationList = new ArrayList<>(annotationNames);
        AnnotationList declaredAnnotations = typeDescription.getDeclaredAnnotations();
        for (AnnotationDescription declaredAnnotation : declaredAnnotations) {
            String actualName = declaredAnnotation.getAnnotationType().getActualName();
            annotationList.remove(actualName);
        }
        return annotationList.isEmpty();
    }

    public static IndirectMatcher byAnnotationNameMatcher(String... annotationNames) {
        return new AnnotationNameMatcher(annotationNames);
    }
}
