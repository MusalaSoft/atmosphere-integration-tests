package com.musala.atmosphere.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.android.ddmlib.IDevice;
import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.agent.AgentManager;
import com.musala.atmosphere.agent.AndroidDebugBridgeManager;
import com.musala.atmosphere.agent.DeviceManager;
import com.musala.atmosphere.agent.devicewrapper.IWrapDevice;
import com.musala.atmosphere.commons.DeviceInformation;
import com.musala.atmosphere.commons.RoutingAction;
import com.musala.atmosphere.commons.util.Pair;
import com.musala.atmosphere.server.pool.PoolManager;

/**
 * Tests whether the device is added/removed to/from the Server's pool when a device is added/removed from/to the Agent.
 * The test should verify whether the required device information for connected/disconnected device has been received on
 * the Server's {@link PoolManager} poolManager.
 *
 * @author dimcho.nedev
 *
 */
public class AddAndRemoveDeviceTest extends BaseIntegrationTest {
    private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(AddAndRemoveDeviceTest.class.getName());

    private static final String ON_DEVICE_CHANGE_LISTENER_PACKAGE = "com.musala.atmosphere.agent.DeviceChangeListener";

    private static final String ON_DEVICE_LIST_CHANGED_METHOD_NAME = "onDeviceListChanged";

    private static final String ADB_MANAGER_FIELD_NAME = "androidDebugBridgeManager";

    private static final String SERVER_MANAGER_FIELD_NAME = "serverManager";

    private static final String POOL_MANAGER_FIELD_NAME = "poolManager";

    private static final String AGENT_MANAGER_FIELD_NAME = "agentManager";

    private static final String DEVICE_MANAGER_FIELD_NAME = "deviceManager";

    private static final String DEVICE_SERIAL_TO_WRAPPER_MAP_NAME = "deviceSerialToDeviceWrapper";

    private static final String DEVICE_SERIAL_NO = "DeviceSerialNo_0";

    private static final int WAIT_TIMEOUT = 5000;

    private static IDevice mockedDevice;

    private static PoolManager poolManager;

    private static DeviceInformation mockDeviceInformation = createMockDeviceInformation();

    @BeforeClass
    public static void setUp() throws Exception {
        // checks whether the Agent is connected to the Server
        String currentConnectedAgentId = agent.getId();
        boolean isConnected = server.isAgentWithIdConnected(currentConnectedAgentId);

        Assume.assumeTrue("The agent is not connected to the server", isConnected);

        // Create mock device wrapper
        IWrapDevice wrapperMock = mock(IWrapDevice.class);
        when(wrapperMock.route(RoutingAction.GET_DEVICE_INFORMATION)).thenReturn(mockDeviceInformation);

        addWrapper(DEVICE_SERIAL_NO, wrapperMock);

        // Create mocked device
        mockedDevice = mock(IDevice.class);
        when(mockedDevice.getSerialNumber()).thenReturn(DEVICE_SERIAL_NO);

        poolManager = getPoolManager(server);
    }

    private static DeviceInformation createMockDeviceInformation() {
        DeviceInformation fakeDeviceInformation = new DeviceInformation();

        fakeDeviceInformation.setApiLevel(23);
        fakeDeviceInformation.setCpu("cpu");
        fakeDeviceInformation.setDpi(300);
        fakeDeviceInformation.setManufacturer("Fake Manufacturer");
        fakeDeviceInformation.setModel("Fake Model");
        fakeDeviceInformation.setOs("Fake OS");
        fakeDeviceInformation.setRam(3072);
        fakeDeviceInformation.setResolution(new Pair<Integer, Integer>(1920, 1080));
        fakeDeviceInformation.setSerialNumber("DeviceSerialNo_0");
        fakeDeviceInformation.setEmulator(false);
        fakeDeviceInformation.setTablet(false);
        fakeDeviceInformation.setCamera(true);

        return fakeDeviceInformation;
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

        Assume.assumeTrue(String.format("Test assumption failed: the device with serial No=%s may have not been removed.",
                                        DEVICE_SERIAL_NO),
                          isRemoved);
    }

    @Test
    public void addDeviceTest() throws Exception {
        callOnDeviceListChanged(mockedDevice, true);

        Thread.sleep(WAIT_TIMEOUT);

        List<Pair<String, String>> devicesOnTheServer = poolManager.getAllAvailableDevices();

        boolean containsMockedDevice = isContainsMockedDevice(devicesOnTheServer);

        Assert.assertTrue(String.format("The mocked device with serial number \"%s\" is not added to the Server's PoolManager",
                                        DEVICE_SERIAL_NO),
                          containsMockedDevice);

        DeviceInformation actualDeviceInformation = poolManager.getDeviceById(agent.getId() + "_" + DEVICE_SERIAL_NO)
                                                               .getInformation();

        Assert.assertEquals(mockDeviceInformation.getApiLevel(), actualDeviceInformation.getApiLevel());
        Assert.assertEquals(mockDeviceInformation.getCpu(), actualDeviceInformation.getCpu());
        Assert.assertEquals(mockDeviceInformation.getDpi(), actualDeviceInformation.getDpi());
        Assert.assertEquals(mockDeviceInformation.getManufacturer(), actualDeviceInformation.getManufacturer());
        Assert.assertEquals(mockDeviceInformation.getModel(), actualDeviceInformation.getModel());
        Assert.assertEquals(mockDeviceInformation.getOS(), actualDeviceInformation.getOS());
        Assert.assertEquals(mockDeviceInformation.getRam(), actualDeviceInformation.getRam());
        // TODO: fix this assert
        // Assert.assertEquals(mockDeviceInformation.getResolution(), actualDeviceInformation.getResolution());
        Assert.assertFalse(actualDeviceInformation.isEmulator());
        Assert.assertFalse(actualDeviceInformation.isTablet());
        Assert.assertTrue(actualDeviceInformation.hasCamera());
    }

    @Test
    public void removeDeviceTest() throws Exception {
        callOnDeviceListChanged(mockedDevice, true);

        Thread.sleep(WAIT_TIMEOUT);

        List<Pair<String, String>> devicesOnTheServer = poolManager.getAllAvailableDevices();
        boolean containsMockedDevice = isContainsMockedDevice(devicesOnTheServer);
        Assert.assertTrue(containsMockedDevice);

        callOnDeviceListChanged(mockedDevice, false);

        Thread.sleep(WAIT_TIMEOUT);

        devicesOnTheServer = poolManager.getAllAvailableDevices();
        containsMockedDevice = isContainsMockedDevice(devicesOnTheServer);

        Assert.assertFalse(containsMockedDevice);

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

    // the method calls the "onDeviceListChanged" method from
    // DeviceChangeListener class using reflection
    private void callOnDeviceListChanged(IDevice device, boolean connected) throws Exception {
        // Get AndroidDebugBridgeManager instance
        Class<?> agentClass = agent.getClass();
        Field androidDebugBridgeManagerField = agentClass.getDeclaredField(ADB_MANAGER_FIELD_NAME);
        androidDebugBridgeManagerField.setAccessible(true);
        AndroidDebugBridgeManager adbManager = (AndroidDebugBridgeManager) androidDebugBridgeManagerField.get(agent);

        Object onDeviceChangeListener = adbManager.getCurrentListener();

        Class<?> deviceChangeListenerClass = Class.forName(ON_DEVICE_CHANGE_LISTENER_PACKAGE);

        Method onDeviceChanged = deviceChangeListenerClass.getMethod(ON_DEVICE_LIST_CHANGED_METHOD_NAME,
                                                                     new Class[] {IDevice.class, boolean.class});
        onDeviceChanged.setAccessible(true);
        onDeviceChanged.invoke(onDeviceChangeListener, device, connected);
    }

    private static void addWrapper(String deviceSerial, IWrapDevice deviceWrapper) throws Exception {
        AgentManager agentManager = AtmosphereReflectionUtils.getFieldObject(agent, AGENT_MANAGER_FIELD_NAME);
        DeviceManager deviceManager = AtmosphereReflectionUtils.getFieldObject(agentManager, DEVICE_MANAGER_FIELD_NAME);
        Map<String, IWrapDevice> deviceSerialToDeviceWrapper = AtmosphereReflectionUtils.getFieldObject(deviceManager,
                                                                                                        DEVICE_SERIAL_TO_WRAPPER_MAP_NAME);
        deviceSerialToDeviceWrapper.put(deviceSerial, deviceWrapper);
        AtmosphereReflectionUtils.setFieldObject(deviceManager,
                                                 DEVICE_SERIAL_TO_WRAPPER_MAP_NAME,
                                                 deviceSerialToDeviceWrapper);
    }

    private static PoolManager getPoolManager(Server server) throws Exception {
        PoolManager poolManager = AtmosphereReflectionUtils.getFieldValue(server,
                                                                          SERVER_MANAGER_FIELD_NAME,
                                                                          POOL_MANAGER_FIELD_NAME);

        return poolManager;
    }
}
