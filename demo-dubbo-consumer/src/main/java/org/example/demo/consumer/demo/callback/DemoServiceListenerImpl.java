package org.example.demo.consumer.demo.callback;

import org.example.dubbo.iface.DemoServiceListener;

public class DemoServiceListenerImpl implements DemoServiceListener {
    @Override
    public void changed(String msg) {
        System.out.println("被回调了：" + msg);
    }
}
