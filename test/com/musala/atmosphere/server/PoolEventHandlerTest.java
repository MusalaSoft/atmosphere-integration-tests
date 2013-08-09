package com.musala.atmosphere.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
// FIXME integration test.
public class PoolEventHandlerTest
{
	private final static int AGENT_RMI_PORT = 1999;

	private final static int SERVER_RMI_PORT = 1998;

	private AgentIntegrationEnvironmentCreator agentEnvironment;

	private ServerIntegrationEnvironmentCreator serverEnvironment;

	private Object deviceChangeListener;

	private Method deviceConnectedMethod;

	private Method deviceDisconnectedMethod;

	private List<String> poolItemsIdList;

	@Before
	public void setUp() throws Exception
	{
		serverEnvironment = new ServerIntegrationEnvironmentCreator(SERVER_RMI_PORT);

		agentEnvironment = new AgentIntegrationEnvironmentCreator(AGENT_RMI_PORT);
		agentEnvironment.connectToLocalhostServer(SERVER_RMI_PORT);

		AgentManager underlyingAgentManager = agentEnvironment.getAgentManagerInstance();
		String agentId = agentEnvironment.getUnderlyingAgentId();
		serverEnvironment.waitForAgentConnection(agentId);

		ServerManager serverManager = serverEnvironment.getServerManager();
		PoolManager poolManager = PoolManager.getInstance(serverManager);

		Field deviceChangeListenerField = underlyingAgentManager.getClass()
																.getDeclaredField("currentDeviceChangeListener");

		deviceChangeListenerField.setAccessible(true);
		deviceChangeListener = deviceChangeListenerField.get(underlyingAgentManager);
		deviceConnectedMethod = deviceChangeListener.getClass().getDeclaredMethod("deviceConnected", IDevice.class);
		deviceConnectedMethod.setAccessible(true);
		deviceDisconnectedMethod = deviceChangeListener.getClass().getDeclaredMethod(	"deviceDisconnected",
																						IDevice.class);
		deviceDisconnectedMethod.setAccessible(true);

		Field poolItemsListField = poolManager.getClass().getDeclaredField("poolItems");
		poolItemsListField.setAccessible(true);
		poolItemsIdList = poolManager.getAllDeviceProxyIds();
	}

	@After
	public void tearDown() throws Exception
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

		int poolItemsBeforeAdd = poolItemsIdList.size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolItemsIdList.size();

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

		int poolItemsBeforeAdd = poolItemsIdList.size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolItemsIdList.size();

		assertEquals(	"Connecting an online device did not result in device connect event.",
						poolItemsBeforeAdd + 1,
						poolItemsAfterAdd);
	}

	@Test
	public void testConnectAndDisconnectOnlineDevice() throws Exception
	{
		final String fakeDeviceSerialNumber = "shalalala4";
		IDevice fakeDevice = mock(IDevice.class);
		when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);

		doAnswer(new Answer<Void>()
		{
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable
			{
				IShellOutputReceiver isor = (IShellOutputReceiver) invocation.getArguments()[1];
				String stringResponse = "status: 1";
				byte[] response = stringResponse.getBytes("ISO-8859-1");
				isor.addOutput(response, 0, response.length);
				isor.flush();
				return null;
			}
		}).when(fakeDevice).executeShellCommand(Mockito.eq("dumpsys battery"),
												(IShellOutputReceiver) any(),
												(int) any());

		doAnswer(new Answer<Void>()
		{
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable
			{
				IShellOutputReceiver isor = (IShellOutputReceiver) invocation.getArguments()[1];
				String stringResponse = "mPlugType=1";
				byte[] response = stringResponse.getBytes("ISO-8859-1");
				isor.addOutput(response, 0, response.length);
				isor.flush();
				return null;
			}
		}).when(fakeDevice).executeShellCommand(Mockito.eq("dumpsys power"), (IShellOutputReceiver) any(), (int) any());

		when(fakeDevice.isOnline()).thenReturn(true);
		when(fakeDevice.isOffline()).thenReturn(false);

		int poolItemsBeforeAdd = poolItemsIdList.size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolItemsIdList.size();

		assertEquals(	"Connecting an online device did not result in device connect event.",
						poolItemsBeforeAdd + 1,
						poolItemsAfterAdd);

		int poolItemsBeforeRemove = poolItemsIdList.size();
		deviceDisconnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterRemove = poolItemsIdList.size();

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

		int poolItemsBeforeAdd = poolItemsIdList.size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolItemsIdList.size();

		assertEquals(	"Connecting an offline device resulted in device connect event.",
						poolItemsBeforeAdd,
						poolItemsAfterAdd);

		int poolItemsBeforeRemove = poolItemsIdList.size();
		deviceDisconnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterRemove = poolItemsIdList.size();

		assertEquals(	"Disconnecting an offline device resulted in device disconnect event.",
						poolItemsBeforeRemove,
						poolItemsAfterRemove);
	}

}
