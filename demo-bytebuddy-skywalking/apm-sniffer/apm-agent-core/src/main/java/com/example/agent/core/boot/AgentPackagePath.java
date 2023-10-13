package com.example.agent.core.boot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;

/**
 * AgentPackagePath
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-21 13:45:46
 */
@Slf4j
public class AgentPackagePath {

    // apm-agent.jar 所在的目录
    private static File AGENT_PACKAGE_PATH;

    public static File getPath() {
        if (AGENT_PACKAGE_PATH == null) {
            AGENT_PACKAGE_PATH = doGetPath();
        }
        return AGENT_PACKAGE_PATH;
    }

    private static File doGetPath() {
        // com/example/agent/AgentPackagePath
        String classResourcePath = AgentPackagePath.class.getName().replaceAll("\\.", "/") + ".class";
        // file:/IdeaProjects/apm-agent-dist/target/classes/com/example/agent/core/AgentPackagePath.class
        // jar:file:/IdeaProjects/apm-agent-dist/apm-agent-1.0-SNAPSHOT-jar-with-dependencies.jar!/com/example/agent/core/AgentPackagePath.class
        URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
        if (resource != null) {
            String location = resource.toString();
            log.info("The beacon class location is {}", location);
            boolean isInJar = location.indexOf('!') > -1;
            if (isInJar) {
                // /IdeaProjects/apm-agent-dist/apm-agent-1.0-SNAPSHOT-jar-with-dependencies.jar
                location = StringUtils.substringBetween(location, "file:", "!");
                File agentJarFile = null;
                try {
                    agentJarFile = new File(location);
                } catch (Exception ex) {
                    log.error("Cannot locate agent jar file by url: {}", location, ex);
                }
                if (agentJarFile != null && agentJarFile.exists()) {
                    // /IdeaProjects/apm-agent-dist
                    return agentJarFile.getParentFile();
                }
            }
        }
        log.error("Cannot locate agent jar file");
        throw new RuntimeException("Cannot locate agent jar file");
    }
}
