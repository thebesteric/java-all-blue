package org.example.zookeeper;

public abstract class ClusterBase extends StandaloneBase {

    private final static String CONNECT_STR = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    private final static int CLUSTER_SESSION_TIMEOUT = 60 * 1000;

    @Override
    public String getConnectStr() {
        return CONNECT_STR;
    }

    @Override
    protected int getSessionTimeout() {
        return CLUSTER_SESSION_TIMEOUT;
    }
}
