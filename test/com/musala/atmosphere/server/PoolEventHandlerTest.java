package com.musala.atmosphere.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.agent.AgentManager;
import com.musala.atmosphere.server.pool.PoolManager;

/**
 * 
 * @author georgi.gaydarov
 * 
 */

public class PoolEventHandlerTest
{
	private final static int AGENT_RMI_PORT = 1999;

	private final static int SERVER_RMI_PORT = 1998;

	private static AgentIntegrationEnvironmentCreator agentEnvironment;

	private static ServerIntegrationEnvironmentCreator serverEnvironment;

	private static Object deviceChangeListener;

	private static Method deviceConnectedMethod;

	private static PoolManager poolManager;

	private static Method deviceDisconnectedMethod;

	@BeforeClass
	public static void setUp() throws Exception
	{
		serverEnvironment = new ServerIntegrationEnvironmentCreator(SERVER_RMI_PORT);

		agentEnvironment = new AgentIntegrationEnvironmentCreator(AGENT_RMI_PORT);
		agentEnvironment.connectToLocalhostServer(SERVER_RMI_PORT);

		AgentManager underlyingAgentManager = agentEnvironment.getAgentManagerInstance();
		String agentId = agentEnvironment.getUnderlyingAgentId();
		serverEnvironment.waitForAgentConnection(agentId);

		ServerManager serverManager = serverEnvironment.getServerManager();
		poolManager = PoolManager.getInstance(serverManager);

		Field deviceChangeListenerField = underlyingAgentManager.getClass()
																.getDeclaredField("currentDeviceChangeListener");

		deviceChangeListenerField.setAccessible(true);
		deviceChangeListener = deviceChangeListenerField.get(underlyingAgentManager);
		deviceConnectedMethod = deviceChangeListener.getClass().getDeclaredMethod("deviceConnected", IDevice.class);
		deviceConnectedMethod.setAccessible(true);
		deviceDisconnectedMethod = deviceChangeListener.getClass().getDeclaredMethod(	"deviceDisconnected",
																						IDevice.class);
		deviceDisconnectedMethod.setAccessible(true);
	}

	@AfterClass
	public static void tearDown() throws Exception
	{
		if (agentEnvironment != null)
		{
			agentEnvironment.close();
		}
		if (serverEnvironment != null)
		{
			serverEnvironment.close();
		}
	}

	@Test
	public void testConnectOfflineDevice() throws Exception
	{
		final String fakeDeviceSerialNumber = "mockDevice1";
		IDevice fakeDevice = mock(IDevice.class);
		when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);

		when(fakeDevice.isOnline()).thenReturn(false);
		when(fakeDevice.isOffline()).thenReturn(true);

		int poolItemsBeforeAdd = poolManager.getAllDeviceProxyIds().size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolManager.getAllDeviceProxyIds().size();

		assertEquals(	"Connecting an offline device resulted in device connect event.",
						poolItemsBeforeAdd,
						poolItemsAfterAdd);

	}

	@Test
	public void testConnectOnlineDevice() throws Exception
	{
		final String fakeDeviceSerialNumber = "mockDevice2";
		IDevice fakeDevice = mock(IDevice.class);
		when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);

		when(fakeDevice.isOnline()).thenReturn(true);
		when(fakeDevice.isOffline()).thenReturn(false);

		int poolItemsBeforeAdd = poolManager.getAllDeviceProxyIds().size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolManager.getAllDeviceProxyIds().size();

		assertEquals(	"Connecting an online device did not result in device connect event.",
						poolItemsBeforeAdd + 1,
						poolItemsAfterAdd);
	}

	@Test
	public void testConnectAndDisconnectOnlineDevice() throws Exception
	{
		final String fakeDeviceSerialNumber = "mockDevice3";
		IDevice fakeDevice = mock(IDevice.class);
		when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);
		doNothing().when(fakeDevice).executeShellCommand(	Mockito.eq("dumpsys battery"),
															any(IShellOutputReceiver.class),
															anyInt());

		doNothing().when(fakeDevice).executeShellCommand(	Mockito.eq("dumpsys power"),
															any(IShellOutputReceiver.class),
															anyInt());

		when(fakeDevice.isOnline()).thenReturn(true);
		when(fakeDevice.isOffline()).thenReturn(false);

		int poolItemsBeforeAdd = poolManager.getAllDeviceProxyIds().size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolManager.getAllDeviceProxyIds().size();

		assertEquals(	"Connecting an online device did not result in device connect event.",
						poolItemsBeforeAdd + 1,
						poolItemsAfterAdd);

		int poolItemsBeforeRemove = poolManager.getAllDeviceProxyIds().size();
		deviceDisconnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterRemove = poolManager.getAllDeviceProxyIds().size();

		assertEquals(	"Disconnecting an online device did not result in device disconnect event.",
						poolItemsBeforeRemove - 1,
						poolItemsAfterRemove);
	}

	@Test
	public void testConnectAndDisconnectOfflineDevice() throws Exception
	{
		final String fakeDeviceSerialNumber = "mockDevice4";
		IDevice fakeDevice = mock(IDevice.class);
		when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);

		when(fakeDevice.isOnline()).thenReturn(false);
		when(fakeDevice.isOffline()).thenReturn(true);

		int poolItemsBeforeAdd = poolManager.getAllDeviceProxyIds().size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolManager.getAllDeviceProxyIds().size();

		assertEquals(	"Connecting an offline device resulted in device connect event.",
						poolItemsBeforeAdd,
						poolItemsAfterAdd);

		int poolItemsBeforeRemove = poolManager.getAllDeviceProxyIds().size();
		deviceDisconnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterRemove = poolManager.getAllDeviceProxyIds().size();

		assertEquals(	"Disconnecting an offline device resulted in device disconnect event.",
						poolItemsBeforeRemove,
						poolItemsAfterRemove);
	}
}
