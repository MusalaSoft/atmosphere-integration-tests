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
import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.agent.AgentManager;
import com.musala.atmosphere.agent.util.FakeServiceAnswer;
import com.musala.atmosphere.server.pool.PoolManager;

/**
 * 
 * @author georgi.gaydarov
 * 
 */

public class PoolEventHandlerTest extends BaseIntegrationTest
{
	private static Object deviceChangeListener;

	private static Method deviceConnectedMethod;

	private static PoolManager poolManager;

	private static Method deviceDisconnectedMethod;

	@Before
	public void setUp() throws Exception
	{
		AgentManager underlyingAgentManager = agentIntegrationEnvironment.getAgentManager();

		poolManager = PoolManager.getInstance();

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

		when(fakeDevice.getBatteryLevel()).thenReturn(50);

		FakeServiceAnswer fakeServiceAnswer = new FakeServiceAnswer();
		Mockito.doAnswer(fakeServiceAnswer).when(fakeDevice).createForward(anyInt(), anyInt());
		
		return fakeDevice;
	}

	@Test
	public void testConnectOfflineDevice() throws Exception
	{
		final String fakeDeviceSerialNumber = "mockDevice1";
		IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

		when(fakeDevice.isOnline()).thenReturn(false);
		when(fakeDevice.isOffline()).thenReturn(true);

		int poolItemsBeforeAdd = poolManager.getAllUnderlyingDeviceProxyIds().size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolManager.getAllUnderlyingDeviceProxyIds().size();
		assertEquals(	"Connecting an offline device resulted in device connect event.",
						poolItemsBeforeAdd,
						poolItemsAfterAdd);
		
		deviceDisconnectedMethod.invoke(deviceChangeListener, fakeDevice);
	}

	@Test
	public void testConnectOnlineDevice() throws Exception
	{
		final String fakeDeviceSerialNumber = "mockDevice2";
		IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

		when(fakeDevice.isOnline()).thenReturn(true);
		when(fakeDevice.isOffline()).thenReturn(false);

		int poolItemsBeforeAdd = poolManager.getAllUnderlyingDeviceProxyIds().size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolManager.getAllUnderlyingDeviceProxyIds().size();

		assertEquals(	"Connecting an online device did not result in device connect event.",
						poolItemsBeforeAdd + 1,
						poolItemsAfterAdd);
		
		deviceDisconnectedMethod.invoke(deviceChangeListener, fakeDevice);
	}

	@Test
	public void testConnectAndDisconnectOnlineDevice() throws Exception
	{
		final String fakeDeviceSerialNumber = "mockDevice3";
		IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

		FakeServiceAnswer fakeServiceAnswer = new FakeServiceAnswer();
		Mockito.doAnswer(fakeServiceAnswer).when(fakeDevice).createForward(anyInt(), anyInt());
		
		when(fakeDevice.isOnline()).thenReturn(true);
		when(fakeDevice.isOffline()).thenReturn(false);

		int poolItemsBeforeAdd = poolManager.getAllUnderlyingDeviceProxyIds().size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolManager.getAllUnderlyingDeviceProxyIds().size();
		assertEquals(	"Connecting an online device did not result in device connect event.",
						poolItemsBeforeAdd + 1,
						poolItemsAfterAdd);

		int poolItemsBeforeRemove = poolManager.getAllUnderlyingDeviceProxyIds().size();
		deviceDisconnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterRemove = poolManager.getAllUnderlyingDeviceProxyIds().size();
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

		int poolItemsBeforeAdd = poolManager.getAllUnderlyingDeviceProxyIds().size();
		deviceConnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterAdd = poolManager.getAllUnderlyingDeviceProxyIds().size();
		assertEquals(	"Connecting an offline device resulted in device connect event.",
						poolItemsBeforeAdd,
						poolItemsAfterAdd);

		int poolItemsBeforeRemove = poolManager.getAllUnderlyingDeviceProxyIds().size();
		deviceDisconnectedMethod.invoke(deviceChangeListener, fakeDevice);
		int poolItemsAfterRemove = poolManager.getAllUnderlyingDeviceProxyIds().size();
		assertEquals(	"Disconnecting an offline device resulted in device disconnect event.",
						poolItemsBeforeRemove,
						poolItemsAfterRemove);
	}
}
