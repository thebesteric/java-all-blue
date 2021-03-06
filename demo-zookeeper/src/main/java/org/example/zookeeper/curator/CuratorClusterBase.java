package org.example.zookeeper.curator;

public class CuratorClusterBase extends CuratorStandaloneBase {
    private final static String CONNECT_STR = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    @Override
    protected String getConnectStr() {
        return CONNECT_STR;
    }
}
