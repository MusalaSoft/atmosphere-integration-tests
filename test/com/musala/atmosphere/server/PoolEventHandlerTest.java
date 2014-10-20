package com.musala.atmosphere.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.agent.AndroidDebugBridgeManager;
import com.musala.atmosphere.agent.util.FakeOnDeviceComponentAnswer;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceAllocationInformation;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.server.pool.PoolManager;

/**
 * 
 * @author georgi.gaydarov
 * 
 */

public class PoolEventHandlerTest extends BaseIntegrationTest {
    /* FIXME : after finishing the logic with the publishing and unpublishing devices, fix this test */
    private static IDeviceChangeListener deviceChangeListener;

    private static PoolManager poolManager;

    @Before
    public void setUp() throws Exception {
        poolManager = PoolManager.getInstance();

        // Get AndroidDebugBridgeManager instance
        Class<?> agentClass = agent.getClass();
        Field androidDebugBridgeManagerField = agentClass.getDeclaredField("androidDebugBridgeManager");
        androidDebugBridgeManagerField.setAccessible(true);
        AndroidDebugBridgeManager androidDebugBridgeManager = (AndroidDebugBridgeManager) androidDebugBridgeManagerField.get(agent);

        // Get the current device change listener
        Class<?> adbManagerClass = androidDebugBridgeManager.getClass();
        Field currentDeviceChangeListenerField = adbManagerClass.getDeclaredField("currentDeviceChangeListener");
        currentDeviceChangeListenerField.setAccessible(true);
        deviceChangeListener = (IDeviceChangeListener) currentDeviceChangeListenerField.get(androidDebugBridgeManager);
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    private static IDevice configureFakeDevice(String fakeDeviceSerialNumber) throws Exception {
        IDevice fakeDevice = mock(IDevice.class);
        when(fakeDevice.getSerialNumber()).thenReturn(fakeDeviceSerialNumber);

        when(fakeDevice.getBatteryLevel()).thenReturn(50);

        FakeOnDeviceComponentAnswer fakeOnDeviceComponentAnswer = new FakeOnDeviceComponentAnswer();
        Mockito.doAnswer(fakeOnDeviceComponentAnswer).when(fakeDevice).createForward(anyInt(), anyInt());

        return fakeDevice;
    }

    // @Test(expected = NoAvailableDeviceFoundException.class)
    public void testConnectOfflineDevice() throws Exception {
        final String fakeDeviceSerialNumber = "mockDevice1";
        IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

        when(fakeDevice.isOnline()).thenReturn(false);
        when(fakeDevice.isOffline()).thenReturn(true);

        deviceChangeListener.deviceConnected(fakeDevice);
        // required for proper device registering
        Thread.sleep(1500);

        poolManager.allocateDevice(new DeviceParameters());

        deviceChangeListener.deviceDisconnected(fakeDevice);
    }

    @Test
    public void testConnectOnlineDevice() throws Exception {
        final String fakeDeviceSerialNumber = "mockDevice2";
        IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

        when(fakeDevice.isOnline()).thenReturn(true);
        when(fakeDevice.isOffline()).thenReturn(false);

        deviceChangeListener.deviceConnected(fakeDevice);
        // required for proper device registering
        Thread.sleep(1500);

        DeviceAllocationInformation deviceAllocationInformation = poolManager.allocateDevice(new DeviceParameters());

        String deviceIdd = agent.getId() + "_" + fakeDeviceSerialNumber;
        String deviceId = deviceAllocationInformation.getDeviceId();
        System.out.println(deviceIdd);
        System.out.println(deviceId);
        assertEquals("Connecting an offline device resulted in device connect event.", deviceIdd, deviceId);

        deviceChangeListener.deviceDisconnected(fakeDevice);
    }

    // @Test(expected = NoAvailableDeviceFoundException.class)
    public void testDisconnectOnlineDevice() throws Exception {
        final String fakeDeviceSerialNumber = "mockDevice3";
        IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

        FakeOnDeviceComponentAnswer fakeOnDeviceComponentAnswer = new FakeOnDeviceComponentAnswer();
        Mockito.doAnswer(fakeOnDeviceComponentAnswer).when(fakeDevice).createForward(anyInt(), anyInt());

        when(fakeDevice.isOnline()).thenReturn(true);
        when(fakeDevice.isOffline()).thenReturn(false);

        deviceChangeListener.deviceConnected(fakeDevice);
        // required for proper device registering
        Thread.sleep(1500);

        deviceChangeListener.deviceDisconnected(fakeDevice);

        poolManager.allocateDevice(new DeviceParameters());
    }
}
