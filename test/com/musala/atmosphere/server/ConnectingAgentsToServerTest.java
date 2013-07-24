package com.musala.atmosphere.server;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.agent.Agent;
import com.musala.atmosphere.commons.Pair;

public class ConnectingAgentsToServerTest
{

	private static final int SERVER_PORT = 1980;

	private static Server testedServer;

	@BeforeClass
	public static void setUp() throws RemoteException
	{
		testedServer = new Server(SERVER_PORT);
		testedServer.run();
	}

	@AfterClass
	public static void tearDown() throws InterruptedException
	{
		testedServer.stop();
	}

	@Test
	public void addingAgentToServerTest() throws InterruptedException, RemoteException
	{
		final int AGENT_PORT = 12345;
		Agent localAgent = new Agent(AGENT_PORT);
		localAgent.run();
		testedServer.addAgentToServer("localhost", AGENT_PORT);

		List<Pair<String, Integer>> expectedAgents = new ArrayList<Pair<String, Integer>>();
		expectedAgents.add(new Pair<String, Integer>("localhost", AGENT_PORT));

		assertEquals("Agent adress list is not as expected", expectedAgents, testedServer.getAgentAdressesList());
		localAgent.stop();
		testedServer.stop();

	}

	@Test
	public void addingFalseAgentToServerTest()
	{
		final String agentIp = "123.123.123.123";
		final int agentPort = 12345;

		List<Pair<String, Integer>> initialAgentAdressesOnServer = testedServer.getAgentAdressesList();
		testedServer.addAgentToServer(agentIp, agentPort);
		List<Pair<String, Integer>> agentAdressesOnServer = testedServer.getAgentAdressesList();

		assertEquals(	"Lists of agent adresses on the Server before adding the fake Agent is not the same as after adding it.",
						initialAgentAdressesOnServer,
						agentAdressesOnServer);
	}
}
