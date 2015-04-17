package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.commons.ConnectionType;
import com.musala.atmosphere.commons.beans.MobileDataState;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.cs.exception.DeviceNotFoundException;

public class NetworkConnectionTest {

    private static Device testDevice;

    private static Builder builder;

    @BeforeClass
    public static void setUp() {
        builder = Builder.getInstance();
    }

    @After
    public void tearDown() throws DeviceNotFoundException {
        builder.releaseDevice(testDevice);
    }

    /**
     * Works only for emulators!
     */
    @Test
    public void getAndSetMobileDataStateOnEmulators() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        testDevice = builder.getDevice(testDeviceSelector);
        assertNotNull("No emulator found.", testDevice);

        MobileDataState expectedState = MobileDataState.UNREGISTERED;
        testDevice.setMobileDataState(expectedState);
        MobileDataState actualState = testDevice.getMobileDataState();
        assertEquals("Mobile data states didn't match.", expectedState, actualState);
    }

    /**
     * Works only for real devices!
     */
    @Test
    public void getConnectionTypeOnRealDevicesTest() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY);
        DeviceSelector deviceSelector = selectorBuilder.build();
        testDevice = builder.getDevice(deviceSelector);
        assertNotNull("No real device found.", testDevice);

        ConnectionType connectionType = testDevice.getConnectionType();
        assertEquals("Connection type is not the same.", ConnectionType.WIFI, connectionType);
    }
}
