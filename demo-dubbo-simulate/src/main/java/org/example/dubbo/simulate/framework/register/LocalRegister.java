package org.example.dubbo.simulate.framework.register;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {

    private static final Map<String, Class<?>> map = new HashMap<>();

    public static void registry(String interfaceName, Class<?> implClass) {
        map.put(interfaceName, implClass);
    }

    public static Class<?> get(String interfaceName) {
        return map.get(interfaceName);
    }
}
