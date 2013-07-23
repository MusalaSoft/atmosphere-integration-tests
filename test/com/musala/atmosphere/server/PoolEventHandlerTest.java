package com.musala.atmosphere.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.android.ddmlib.IDevice;
import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.agent.AgentManager;

/**
 * 
 * @author georgi.gaydarov
 * 
 */
public class PoolEventHandlerTest
{
	private final static int AGENT_RMI_PORT = 1999;

	private final static int SERVER_RMI_PORT = 1998;

	private AgentIntegrationEnvironmentCreator agentEnvironment;

	private ServerIntegrationEnvironmentCreator serverEnvironment;

	private Object deviceChangeListener;

	private Method deviceConnectedMethod;

	private Method deviceDisconnectedMethod;

	private List<PoolItem> poolItemsList;

	@Before
	public void setUp() throws Exception
	{
		agentEnvironment = new AgentIntegrationEnvironmentCreator(AGENT_RMI_PORT);
		AgentManager underlyingAgentManager = agentEnvironment.getAgentManagerInstance();

		serverEnvironment = new ServerIntegrationEnvironmentCreator(SERVER_RMI_PORT);
		agentEnvironment.connectToLocalhostServer(SERVER_RMI_PORT);
		String agentId = agentEnvironment.getUnderlyingAgentId();
		serverEnvironment.waitForAgentConnection(agentId);
		PoolManager underlyingPoolManager = serverEnvironment.getPoolManager();

		Field deviceChangeListenerField = underlyingAgentManager.getClass()
																.getDeclaredField("currentDeviceChangeListener");

		deviceChangeListenerField.setAccessible(true);
		deviceChangeListener = deviceChangeListenerField.get(underlyingAgentManager);
		deviceConnectedMethod = deviceChangeListener.getClass().getDeclaredMethod("deviceConnected", IDevice.class);
		deviceConnectedMethod.setAccessible(true);
		deviceDisconnectedMethod = deviceChangeListener.getClass().getDeclaredMethod(	"deviceDisconnected",
																						IDevice.class);
		deviceDisconnectedMethod.setAccessible(true);

		Field poolItemsListField = underlyingPoolManager.getClass().getDeclaredField("poolItems");
		poolItemsListField.setAccessible(true);
		poolItemsList = (List<PoolItem>) poolItemsListField.get(underlyingPoolManager);
	}

	@After
	public void tearDown() throws Exception
	{
		serverEnvironment.close();
		agentEnvironment.close();
	}

	@Test
	public void testConnectOfflineDevice()
		throws IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException
	{
		final String fakeDeviceSerialNumber = "shalalala";
		IDevice fakeDevice = mock(IDevice.class);
		when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);

		when(fakeDevice.isOnline()).thenReturn(false);
		when(fakeDevice.isOffline()).thenReturn(true);

		int poolItemsBeforeAdd = poolItemsList.size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolItemsList.size();

		assertEquals(	"Connecting an offline device resulted in device connect event.",
						poolItemsBeforeAdd,
						poolItemsAfterAdd);
	}

	@Test
	public void testConnectOnlineDevice()
		throws IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException
	{
		final String fakeDeviceSerialNumber = "shalalala2";
		IDevice fakeDevice = mock(IDevice.class);
		when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);

		when(fakeDevice.isOnline()).thenReturn(true);
		when(fakeDevice.isOffline()).thenReturn(false);

		int poolItemsBeforeAdd = poolItemsList.size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolItemsList.size();

		assertEquals(	"Connecting an online device did not result in device connect event.",
						poolItemsBeforeAdd + 1,
						poolItemsAfterAdd);
	}

	@Test
	public void testConnectAndDisconnectOnlineDevice()
		throws IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException
	{
		final String fakeDeviceSerialNumber = "shalalala4";
		IDevice fakeDevice = mock(IDevice.class);
		when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);

		when(fakeDevice.isOnline()).thenReturn(true);
		when(fakeDevice.isOffline()).thenReturn(false);

		int poolItemsBeforeAdd = poolItemsList.size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolItemsList.size();

		assertEquals(	"Connecting an online device did not result in device connect event.",
						poolItemsBeforeAdd + 1,
						poolItemsAfterAdd);

		int poolItemsBeforeRemove = poolItemsList.size();
		deviceDisconnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterRemove = poolItemsList.size();

		assertEquals(	"Disconnecting an online device did not result in device disconnect event.",
						poolItemsBeforeRemove - 1,
						poolItemsAfterRemove);
	}

	@Test
	public void testConnectAndDisconnectOfflineDevice()
		throws IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException
	{
		final String fakeDeviceSerialNumber = "shalalala3";
		IDevice fakeDevice = mock(IDevice.class);
		when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);

		when(fakeDevice.isOnline()).thenReturn(false);
		when(fakeDevice.isOffline()).thenReturn(true);

		int poolItemsBeforeAdd = poolItemsList.size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolItemsList.size();

		assertEquals(	"Connecting an offline device resulted in device connect event.",
						poolItemsBeforeAdd,
						poolItemsAfterAdd);

		int poolItemsBeforeRemove = poolItemsList.size();
		deviceDisconnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterRemove = poolItemsList.size();

		assertEquals(	"Disconnecting an offline device resulted in device disconnect event.",
						poolItemsBeforeRemove,
						poolItemsAfterRemove);
	}

}
