package com.musala.atmosphere.testsuites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.musala.atmosphere.agent.Agent;
import com.musala.atmosphere.server.ServerIntegrationEnvironment;

/**
 * JUnit test suite with all running integration tests.
 * 
 * @author valyo.yolovski
 * 
 */
@RunWith(Suite.class)
public class AtmosphereIntegrationTestsSuite {
    private final static int SERVER_MANAGER_RMI_PORT = 2099;

    private final static int AGENTMANAGER_RMI_PORT = 2000;

    private static Agent agent;

    private static ServerIntegrationEnvironment serverEnvironment;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Start agent and server.
        agent = new Agent(AGENTMANAGER_RMI_PORT);
        serverEnvironment = new ServerIntegrationEnvironment(SERVER_MANAGER_RMI_PORT);

        // Connect agent to the server
        agent.connectToServer("localhost", SERVER_MANAGER_RMI_PORT);
        String agentId = agent.getId();
        serverEnvironment.waitForAgentConnection(agentId);

        Thread.sleep(10000);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Master tear down.
        serverEnvironment.close();
        agent.stop();
    }

    public static ServerIntegrationEnvironment getServerEnvironment() {
        return serverEnvironment;
    }

    public static Agent getAgent() {
        return agent;
    }
}
