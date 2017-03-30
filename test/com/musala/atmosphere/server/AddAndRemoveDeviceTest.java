package com.musala.atmosphere.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.android.ddmlib.IDevice;
import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.agent.AndroidDebugBridgeManager;
import com.musala.atmosphere.commons.exceptions.CommandFailedException;
import com.musala.atmosphere.commons.util.Pair;
import com.musala.atmosphere.server.dao.exception.DevicePoolDaoException;
import com.musala.atmosphere.server.pool.PoolManager;

/**
 * Tests whether the device is added/removed to/from the Server's pool when a
 * device is added/removed from/to the Agent. The test should verify whether the
 * required device information for connected/disconnected device has been
 * received on the Server's {@link PoolManager} poolManager.
 * 
 * @author dimcho.nedev
 *
 */
public class AddAndRemoveDeviceTest extends BaseIntegrationTest {
	private static java.util.logging.Logger LOGGER = java.util.logging.Logger
			.getLogger(AddAndRemoveDeviceTest.class.getName());

	private static final String ON_DEVICE_CHANGE_LISTENER_PACKAGE = "com.musala.atmosphere.agent.DeviceChangeListener";

	private static final String ON_DEVICE_LIST_CHANGED_METHOD_NAME = "onDeviceListChanged";

	private static final String ADB_MANAGER_FIELD_NAME = "androidDebugBridgeManager";
	
	private static final String SERVER_MANAGER_FIELD_NAME = "serverManager";
	
	private static final String POOL_MANAGER_FIELD_NAME = "poolManager";

	private static final String DEVICE_SERIAL_NO = "DeviceSerialNo_0";

	private static IDevice mockedDevice;

	private static PoolManager poolManager;

	@BeforeClass
	public static void isCurrenAgentIsConnectedToTheServer()
			throws RemoteException, NoSuchFieldException, IllegalAccessException {

		// checks whether the Agent is connected to the Server
		String currenConnectedAgentId = agent.getId();
		boolean isConnected = server.isAgentWithIdConnected(currenConnectedAgentId);

		Assume.assumeTrue("The agent is not connected to the server", isConnected);
		
		// Create mock device wrapper
		// Remove the line below after the WebSocket migration
		WrapMockDevice mockWrapDevice = new WrapMockDevice();

		// Bind the wrapper to the agent registry RMI port
		// Remove the line below after the WebSocket migration
		Registry rmiRegistry = LocateRegistry.getRegistry(agent.getAgentRmiPort());
		rmiRegistry.rebind(DEVICE_SERIAL_NO, mockWrapDevice);

		// Create mocked device
		mockedDevice = mock(IDevice.class);
		when(mockedDevice.getSerialNumber()).thenReturn(DEVICE_SERIAL_NO);

		poolManager = getPoolManager();
	}

	@Before
	@After
	public void assumeDeviceIsNotAlreadyAdded() throws NoSuchFieldException, IllegalAccessException {
		boolean isRemoved = false;
		try {
			poolManager.removeDevice(DEVICE_SERIAL_NO);
			isRemoved = true;
		} catch (NullPointerException e1) {
			isRemoved = true;
		} catch (Exception e2) {
			// fails to remove - nothing to do here
		}

		Assume.assumeTrue(
				String.format("Test assumption failed: the device with serial No=%s may have not been removed.",
						DEVICE_SERIAL_NO),
				isRemoved);
	}

	@Test
	public void dynamicallyDeviceAllocationTest()
			throws CommandFailedException, NotBoundException, NoSuchFieldException, IllegalAccessException,
			RemoteException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException,
			InvocationTargetException, DevicePoolDaoException {

		callOnDeviceListChanged(mockedDevice, true);

		List<Pair<String, String>> devicesOnTheServer = poolManager.getAllAvailableDevices();

		boolean containsMockedDevice = isContainsMockedDevice(devicesOnTheServer);

		Assert.assertTrue(
				String.format("The mocket device with serial number \"%s\" is not added to the Server's PollManager",
						DEVICE_SERIAL_NO),
				containsMockedDevice);
	}

	private boolean isContainsMockedDevice(List<Pair<String, String>> devices) {
		boolean containsMockedDevice = false;

		for (Pair<String, String> pair : devices) {
			if (pair.getKey().equals(DEVICE_SERIAL_NO) && pair.getValue().equals("Fake Model")) {
				containsMockedDevice = true;
			}

			LOGGER.log(Level.INFO, String.format("Device serial = %s", pair.getKey()));
			LOGGER.log(Level.INFO, String.format("Device model = %s", pair.getValue()));
		}
		return containsMockedDevice;
	}

	@Test
	public void dynamicallyDeviceRemoveTest() throws CommandFailedException, NotBoundException, NoSuchFieldException,
			IllegalAccessException, RemoteException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException, DevicePoolDaoException {

		callOnDeviceListChanged(mockedDevice, true);
		List<Pair<String, String>> devicesOnTheServer = poolManager.getAllAvailableDevices();
		boolean containsMockedDevice = isContainsMockedDevice(devicesOnTheServer);
		Assert.assertTrue(containsMockedDevice);

		callOnDeviceListChanged(mockedDevice, false);

		devicesOnTheServer = poolManager.getAllAvailableDevices();
		containsMockedDevice = isContainsMockedDevice(devicesOnTheServer);

		Assert.assertFalse(containsMockedDevice);

	}

	// the method calls the "onDeviceListChanged" method from
	// DeviceChangeListener class using reflection
	private void callOnDeviceListChanged(IDevice device, boolean connected) throws NoSuchFieldException,
			IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

		// Get AndroidDebugBridgeManager instance
		Class<?> agentClass = agent.getClass();
		Field androidDebugBridgeManagerField = agentClass.getDeclaredField(ADB_MANAGER_FIELD_NAME);
		androidDebugBridgeManagerField.setAccessible(true);
		AndroidDebugBridgeManager adbManager = (AndroidDebugBridgeManager) androidDebugBridgeManagerField.get(agent);

		Object onDeviceChangeListener = adbManager.getCurrentListener();

		Class<?> deviceChangeListenerClass = Class.forName(ON_DEVICE_CHANGE_LISTENER_PACKAGE);

		Method onDeviceChanged = deviceChangeListenerClass.getMethod(ON_DEVICE_LIST_CHANGED_METHOD_NAME,
				new Class[] { IDevice.class, boolean.class });
		onDeviceChanged.setAccessible(true);
		onDeviceChanged.invoke(onDeviceChangeListener, device, connected);
	}

	private static PoolManager getPoolManager() throws NoSuchFieldException, IllegalAccessException {
		Class<?> serverClass = server.getClass();
		Field serverManager = serverClass.getDeclaredField(SERVER_MANAGER_FIELD_NAME);
		serverManager.setAccessible(true);
		ServerManager sManager = (ServerManager) serverManager.get(server);

		Class<?> sManagerClass = sManager.getClass();
		Field poolManager = sManagerClass.getDeclaredField(POOL_MANAGER_FIELD_NAME);
		poolManager.setAccessible(true);
		PoolManager pManager = (PoolManager) poolManager.get(sManager);

		return pManager;
	}

}
