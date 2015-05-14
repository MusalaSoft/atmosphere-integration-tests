package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import org.junit.After;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.geometry.Point;

public class ScreenOffTimeoutTest extends BaseIntegrationTest {
    private static final long SCREEN_OFF_TIMEOUT = 7000;

    private static final long TIME_TO_WAIT = 2000;

    private long defaultTimeOut;

    @After
    public void tearDown() throws Exception {
        if (testDevice != null) {
            testDevice.setScreenOffTimeout(defaultTimeOut);
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        }
        releaseDevice();
    }

    @Test
    public void testSetScreenOffTimeoutOnRealDevice() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        try {
            initTestDevice(testDeviceSelector);
        } catch (NoAvailableDeviceFoundException e) {
        }

        assumeNotNull(testDevice);
        setTestDevice(testDevice);
        startMainActivity();

        defaultTimeOut = testDevice.getScreenOffTimeout();

        testDevice.setScreenOffTimeout(SCREEN_OFF_TIMEOUT);
        testDevice.tapScreenLocation(new Point(2, 3));
        assertTrue("Device is not awake.", testDevice.isAwake());

        assertEquals("Expected screen of timeout does not match.", testDevice.getScreenOffTimeout(), SCREEN_OFF_TIMEOUT);

        Thread.sleep(SCREEN_OFF_TIMEOUT + TIME_TO_WAIT);
        assertFalse("Device is awake.", testDevice.isAwake());
    }

    @Test
    public void testOnlySetScreenOffTimeoutOnDevice() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        assumeNotNull(testDevice);
        setTestDevice(testDevice);
        startMainActivity();

        defaultTimeOut = testDevice.getScreenOffTimeout();

        testDevice.setScreenOffTimeout(SCREEN_OFF_TIMEOUT);
        assertEquals("Expected screen of timeout does not match.", testDevice.getScreenOffTimeout(), SCREEN_OFF_TIMEOUT);
    }

}
