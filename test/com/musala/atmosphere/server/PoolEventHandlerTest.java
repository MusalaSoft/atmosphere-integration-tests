package com.musala.atmosphere.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
import com.musala.atmosphere.commons.sa.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.server.pool.PoolManager;

/**
 * 
 * @author georgi.gaydarov
 * 
 */

public class PoolEventHandlerTest extends BaseIntegrationTest {
    private static final long TIMEOUT_AFTER_DEVICE_CONNECT = 2000;

    private static final long TIMEOUT_AFTER_DEVICE_DISCONNECT = 2000;

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
    public static void tearDown() throws Exception {
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

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testConnectOfflineDevice() throws Exception {
        final String fakeDeviceSerialNumber = "mockDevice1";
        IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

        when(fakeDevice.isOnline()).thenReturn(false);
        when(fakeDevice.isOffline()).thenReturn(true);

        deviceChangeListener.deviceConnected(fakeDevice);

        Thread.sleep(TIMEOUT_AFTER_DEVICE_CONNECT);

        DeviceParameters deviceParameters = new DeviceParameters();
        deviceParameters.setSerialNumber(fakeDeviceSerialNumber);

        poolManager.allocateDevice(deviceParameters);

        deviceChangeListener.deviceDisconnected(fakeDevice);
    }

    @Test
    public void testConnectOnlineDevice() throws Exception {
        final String fakeDeviceSerialNumber = "mockDevice2";
        IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

        when(fakeDevice.isOnline()).thenReturn(true);
        when(fakeDevice.isOffline()).thenReturn(false);

        deviceChangeListener.deviceConnected(fakeDevice);

        Thread.sleep(TIMEOUT_AFTER_DEVICE_CONNECT);

        DeviceParameters deviceParameters = new DeviceParameters();
        deviceParameters.setSerialNumber(fakeDeviceSerialNumber);

        DeviceAllocationInformation deviceAllocationInformation = poolManager.allocateDevice(deviceParameters);
        String connectedDeviceId = deviceAllocationInformation.getDeviceId();

        String agentId = agent.getId();

        Class<?> pmc = PoolManager.class;
        Method getId = pmc.getDeclaredMethod("buildDeviceIdentifier", String.class, String.class);
        getId.setAccessible(true);
        String fakeDeviceId = (String) getId.invoke(null, agentId, fakeDeviceSerialNumber);

        assertEquals("Failed to connect an online device.", fakeDeviceId, connectedDeviceId);

        deviceChangeListener.deviceDisconnected(fakeDevice);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testDisconnectOnlineDevice() throws Exception {
        final String fakeDeviceSerialNumber = "mockDevice3";
        IDevice fakeDevice = configureFakeDevice(fakeDeviceSerialNumber);

        FakeOnDeviceComponentAnswer fakeOnDeviceComponentAnswer = new FakeOnDeviceComponentAnswer();
        Mockito.doAnswer(fakeOnDeviceComponentAnswer).when(fakeDevice).createForward(anyInt(), anyInt());

        when(fakeDevice.isOnline()).thenReturn(true);
        when(fakeDevice.isOffline()).thenReturn(false);

        deviceChangeListener.deviceConnected(fakeDevice);

        Thread.sleep(TIMEOUT_AFTER_DEVICE_CONNECT);

        DeviceParameters deviceParameters = new DeviceParameters();
        deviceParameters.setSerialNumber(fakeDeviceSerialNumber);

        deviceChangeListener.deviceDisconnected(fakeDevice);

        Thread.sleep(TIMEOUT_AFTER_DEVICE_DISCONNECT);

        poolManager.allocateDevice(deviceParameters);
    }
}
