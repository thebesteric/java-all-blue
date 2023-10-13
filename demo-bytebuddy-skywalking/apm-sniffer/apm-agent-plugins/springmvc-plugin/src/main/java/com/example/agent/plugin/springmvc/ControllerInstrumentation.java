package com.example.agent.plugin.springmvc;

import com.example.agent.core.plugin.matcher.AnnotationNameMatcher;
import com.example.agent.core.plugin.matcher.ClassMatcher;

/**
 * 拦截带有 @Controller 注解的插件
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-19 11:53:51
 */
public class ControllerInstrumentation extends AbstractSpringMVCComboInstrumentation {
    private static final String CONTROLLER_NAME = "org.springframework.stereotype.Controller";

    @Override
    protected ClassMatcher enhanceClass() {
        return AnnotationNameMatcher.byAnnotationNameMatcher(CONTROLLER_NAME);
    }
}
