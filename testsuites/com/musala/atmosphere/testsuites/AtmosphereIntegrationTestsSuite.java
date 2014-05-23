package com.musala.atmosphere.testsuites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.musala.atmosphere.agent.Agent;
import com.musala.atmosphere.server.Server;

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

    private static Server server;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Start and run server.
        server = new Server(SERVER_MANAGER_RMI_PORT);
        server.run();

        // Start and connect agent to the server
        agent = new Agent(AGENTMANAGER_RMI_PORT);
        agent.connectToServer("localhost", SERVER_MANAGER_RMI_PORT);
        String agentId = agent.getId();
        server.waitForGivenAgentToConnect(agentId);

        Thread.sleep(10000);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Master tear down.
        server.exit();
        agent.stop();
    }

    public static Agent getAgent() {
        return agent;
    }

    public static Server getServer() {
        return server;
    }
}
