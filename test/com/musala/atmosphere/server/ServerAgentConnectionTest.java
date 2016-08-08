//package com.musala.atmosphere.server;
//
//import static org.junit.Assert.assertEquals;
//
//import java.lang.reflect.Field;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
//import com.musala.atmosphere.agent.AgentManager;
//import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;
//
////FIXME It should be decided whther this test is adequate.
//public class ServerAgentConnectionTest
//{
//	private final static int POOLMANAGER_RMI_PORT = 2099;
//
//	private final static int AGENTMANAGER_RMI_PORT = 2000;
//
//	private AgentIntegrationEnvironmentCreator agentEnvironment;
//
//	private ServerIntegrationEnvironmentCreator serverEnvironment;
//
//	@Before
//	public void setUp() throws Exception
//	{
//		agentEnvironment = AtmosphereIntegrationTestsSuite.getAgentIntegrationEnvironmentCreator();
//	}
//
//	@Test
//	public void testSuccessfulConnection() throws Exception
//	{
//		serverEnvironment = new ServerIntegrationEnvironmentCreator(POOLMANAGER_RMI_PORT);
//		agentEnvironment.connectToLocalhostServer(POOLMANAGER_RMI_PORT);
//		String agentId = agentEnvironment.getUnderlyingAgentId();
//		serverEnvironment.waitForAgentConnection(agentId);
//
//		AgentManager underlyingAgentManager = agentEnvironment.getAgentManagerInstance();
//		Field deviceChangeListenerField = underlyingAgentManager.getClass()
//																.getDeclaredField("currentDeviceChangeListener");
//		deviceChangeListenerField.setAccessible(true);
//		Object deviceChangeListener = deviceChangeListenerField.get(underlyingAgentManager);
//		Field listenerServerPort = deviceChangeListener.getClass().getDeclaredField("serverRmiPort");
//		listenerServerPort.setAccessible(true);
//		int setPort = listenerServerPort.getInt(deviceChangeListener);
//
//		assertEquals(	"PoolManager registration on the Agent failed. (the agent event sender has an incorrect server port set)",
//						POOLMANAGER_RMI_PORT,
//						setPort);
//	}
// }
