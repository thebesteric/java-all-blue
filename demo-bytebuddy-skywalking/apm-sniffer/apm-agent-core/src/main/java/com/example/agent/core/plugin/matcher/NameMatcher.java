package com.example.agent.core.plugin.matcher;

import lombok.Getter;

/**
 * 用于类名称匹配的匹配器，仅适用于 named(xxx) 的清空
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-19 00:30:16
 */
@Getter
public class NameMatcher implements ClassMatcher {

    private final String className;

    private NameMatcher(String className) {
        if (className == null || className.isEmpty()) {
            throw new IllegalArgumentException("class name cannot be null");
        }
        this.className = className;
    }

    public static NameMatcher byNameMatcher(String className) {
        return new NameMatcher(className);
    }
}
