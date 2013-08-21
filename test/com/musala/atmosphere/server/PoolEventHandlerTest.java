package com.musala.atmosphere.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;

/**
 * 
 * @author georgi.gaydarov
 * 
 */

public class PoolEventHandlerTest
{
	private static Object deviceChangeListener;

	private static Method deviceConnectedMethod;

	private static PoolManager poolManager;

	private static Method deviceDisconnectedMethod;

	@Before
	public void setUp() throws Exception
	{
		AgentIntegrationEnvironmentCreator agentEnvironment = AtmosphereIntegrationTestsSuite.getAgentIntegrationEnvironmentCreator();
		ServerIntegrationEnvironmentCreator serverEnvironment = AtmosphereIntegrationTestsSuite.getServerIntegrationEnvironmentCreator();

		AgentManager underlyingAgentManager = agentEnvironment.getAgentManagerInstance();

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

	private IDevice configureFakeDevice(String fakeDeviceSerialNumber) throws Exception
	{
		IDevice fakeDevice = mock(IDevice.class);
		when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);

		DumpsysBatteryAnswer dumpsysBatteryAnswer = new DumpsysBatteryAnswer();
		DumpsysPowerAnswer dumpsysPowerAnswer = new DumpsysPowerAnswer();

		when(fakeDevice.getBatteryLevel()).thenReturn(50);
		doAnswer(dumpsysPowerAnswer).when(fakeDevice).executeShellCommand(	Mockito.eq("dumpsys power"),
																			any(IShellOutputReceiver.class),
																			anyInt());
		doAnswer(dumpsysBatteryAnswer).when(fakeDevice).executeShellCommand(Mockito.eq("dumpsys battery"),
																			any(IShellOutputReceiver.class),
																			anyInt());
		return fakeDevice;
	}

	@Test
	public void testConnectOfflineDevice() throws Exception
	{
		final String fakeDeviceSerialNumber = "mockDevice1";
		IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

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
		IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

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
		IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

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
		IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

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

class DumpsysPowerAnswer implements Answer<Void>
{
	@Override
	public Void answer(InvocationOnMock invocation) throws Exception
	{

		Object[] arguments = invocation.getArguments();
		IShellOutputReceiver shellOutputReceiver = (IShellOutputReceiver) arguments[1];
		String stringResponse = "mPlugType=1";
		byte[] response = stringResponse.getBytes("ISO-8859-1");
		shellOutputReceiver.addOutput(response, 0, response.length);
		shellOutputReceiver.flush();
		return null;
	}
}

class DumpsysBatteryAnswer implements Answer<Void>
{
	@Override
	public Void answer(InvocationOnMock invocation) throws Exception
	{
		Object[] arguments = invocation.getArguments();
		IShellOutputReceiver shellOutputReceiver = (IShellOutputReceiver) arguments[1];
		String stringResponse = "status: 1";
		byte[] response = stringResponse.getBytes("ISO-8859-1");
		shellOutputReceiver.addOutput(response, 0, response.length);
		shellOutputReceiver.flush();
		return null;
	}
}
