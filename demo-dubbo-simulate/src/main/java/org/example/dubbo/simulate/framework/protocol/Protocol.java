package org.example.dubbo.simulate.framework.protocol;

import org.example.dubbo.simulate.framework.Invocation;
import org.example.dubbo.simulate.framework.URL;

public interface Protocol {
    void startup(URL url);
    String send(URL url, Invocation invocation);
}
