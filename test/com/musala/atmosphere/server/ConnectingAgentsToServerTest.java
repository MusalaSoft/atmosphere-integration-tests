package com.musala.atmosphere.server;

import static com.musala.atmosphere.test.util.Constants.SERVER_IP;
import static com.musala.atmosphere.test.util.Constants.SERVER_PORT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.android.ddmlib.AndroidDebugBridge;
import com.musala.atmosphere.agent.Agent;
import com.musala.atmosphere.agent.AgentManager;

/**
 * Tests connect and register several agent to a server.
 *
 * @author dimcho.nedev
 *
 */
public class ConnectingAgentsToServerTest {
    private static final int NUMBER_OF_AGENTS = 5;

    private static final String AGENT_ID_FIELD_NAME = "agentId";

    private static final String AGENT_MENAGER_FIELD_NAME = "agentManager";

    private static final String SERVER_MANAGER_FIELD_NAME = "serverManager";

    private static Server testServer;

    private static List<Agent> testAgents;

    private static List<String> expectedAgentIds;

    private static ServerManager serverManager;

    @BeforeClass
    public static void setUp() throws Exception {
        AndroidDebugBridge.terminate();

        testServer = new Server(SERVER_IP, SERVER_PORT + 1);
        testServer.run();

        serverManager = AtmosphereReflectionUtils.getFieldObject(testServer, SERVER_MANAGER_FIELD_NAME);

        testAgents = new ArrayList<>();
        expectedAgentIds = new ArrayList<String>();
        for (int i = 0; i < NUMBER_OF_AGENTS; i++) {
            Agent currentAgent = new Agent();
            AndroidDebugBridge.terminate();

            // Assign an unique Id to the current Agent
            AgentManager agentManager = AtmosphereReflectionUtils.getFieldObject(currentAgent,
                                                                                 AGENT_MENAGER_FIELD_NAME);
            String newAgentId = AtmosphereReflectionUtils.getFieldObject(agentManager, AGENT_ID_FIELD_NAME);
            newAgentId += i;
            AtmosphereReflectionUtils.setFieldObject(agentManager, AGENT_ID_FIELD_NAME, newAgentId);
            AtmosphereReflectionUtils.setFieldObject(currentAgent, AGENT_MENAGER_FIELD_NAME, agentManager);

            testAgents.add(currentAgent);
            expectedAgentIds.add(newAgentId);

            currentAgent.connectToServer(SERVER_IP, SERVER_PORT + 1);
        }

        List<String> actualAgentIds = serverManager.getAllConnectedAgentIds();

        Assert.assertEquals(expectedAgentIds, actualAgentIds);
    }

    @AfterClass
    public static void tearDown() {
        testServer.stop();
        for (Agent agent : testAgents) {
            try {
                agent.stop();
            } catch (NullPointerException e) {
                // System.out.println("AAAAAAAAAA");
            }
        }
    }

    /**
     * TODO: The test case action is missing. Consider to remove/rewrite the test case.
     *
     * The server should have registered expected Agent.
     */
    /*
     * @Test public void registeredAgentIdTest() throws Exception { List<String> actualAgentIds =
     * serverManager.getAllConnectedAgentIds();
     *
     * Assert.assertEquals(expectedAgentIds, actualAgentIds); }
     */

    @Test
    public void stopAgentTest() throws Exception {
        int randomIndex = new Random().nextInt(NUMBER_OF_AGENTS);
        testAgents.get(randomIndex).stop();
        Thread.sleep(6000);

        List<String> actualAgentIds = serverManager.getAllConnectedAgentIds();

        Assert.assertEquals(NUMBER_OF_AGENTS - 1, actualAgentIds.size());

        expectedAgentIds.remove(randomIndex);
    }

}
