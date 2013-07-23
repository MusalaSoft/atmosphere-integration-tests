package com.musala.atmosphere.server;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.agent.AgentManager;

public class ServerAgentConnectionTest
{
	private final static int POOLMANAGER_RMI_PORT = 2099;

	private final static int AGENTMANAGER_RMI_PORT = 2000;

	private AgentIntegrationEnvironmentCreator agentEnvironment;

	private ServerIntegrationEnvironmentCreator serverEnvironment;

	@Before
	public void setUp() throws Exception
	{
		agentEnvironment = new AgentIntegrationEnvironmentCreator(AGENTMANAGER_RMI_PORT);
	}

	@After
	public void tearDown() throws Exception
	{
		serverEnvironment.close();
		agentEnvironment.close();
	}

	@Test
	public void testSuccessfulConnection()
		throws RemoteException,
			NotBoundException,
			NoSuchFieldException,
			SecurityException,
			IllegalArgumentException,
			IllegalAccessException
	{
		serverEnvironment = new ServerIntegrationEnvironmentCreator(POOLMANAGER_RMI_PORT);
		agentEnvironment.connectToLocalhostServer(POOLMANAGER_RMI_PORT);
		String agentId = agentEnvironment.getUnderlyingAgentId();
		serverEnvironment.waitForAgentConnection(agentId);

		AgentManager underlyingAgentManager = agentEnvironment.getAgentManagerInstance();
		Field deviceChangeListenerField = underlyingAgentManager.getClass()
																.getDeclaredField("currentDeviceChangeListener");
		deviceChangeListenerField.setAccessible(true);
		Object deviceChangeListener = deviceChangeListenerField.get(underlyingAgentManager);
		Field listenerServerPort = deviceChangeListener.getClass().getDeclaredField("serverRmiPort");
		listenerServerPort.setAccessible(true);
		int setPort = listenerServerPort.getInt(deviceChangeListener);

		assertEquals(	"PoolManager registration on the Agent failed. (the agent event sender has an incorrect server port set)",
						POOLMANAGER_RMI_PORT,
						setPort);
	}
}
