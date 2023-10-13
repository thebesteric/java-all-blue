package com.example.agent.plugin.springmvc;

import com.example.agent.core.plugin.matcher.AnnotationNameMatcher;
import com.example.agent.core.plugin.matcher.ClassMatcher;

/**
 * 拦截带有 @RestController 注解的插件
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-19 11:53:51
 */
public class RestControllerInstrumentation extends AbstractSpringMVCComboInstrumentation {
    private static final String REST_CONTROLLER_NAME = "org.springframework.web.bind.annotation.RestController";

    @Override
    protected ClassMatcher enhanceClass() {
        return AnnotationNameMatcher.byAnnotationNameMatcher(REST_CONTROLLER_NAME);
    }
}
