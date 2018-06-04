package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

/**
 * 
 * @author yavor.stankov
 *
 */
public class ChangeGpsLocationStateTest extends BaseIntegrationTest {
    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        Assume.assumeTrue("Failed to disable the gps location.", testDevice.disableGpsLocation());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);

        releaseDevice();
    }

    @Test
    public void testEnableGpsLocation() throws Exception {
        testDevice.disableGpsLocation();

        assertFalse("Failed to disable the gps location.", testDevice.isGpsLocationEnabled());

        testDevice.enableGpsLocation();

        assertTrue("Failed to enable the gps location.", testDevice.isGpsLocationEnabled());
    }

    @Test
    public void testDisableGpsLocation() throws Exception {
        testDevice.enableGpsLocation();

        assertTrue("Failed to enable the gps location.", testDevice.isGpsLocationEnabled());

        testDevice.disableGpsLocation();

        assertFalse("Failed to disable the gps location.", testDevice.isGpsLocationEnabled());
    }

    @Test
    public void testEnableGpsLocationWhileApplicationIsRunning() throws Exception {
        testDevice.startApplication(VALIDATOR_APP_PACKAGE);

        assertValidatorIsStarted();

        testDevice.enableGpsLocation();

        assertValidatorIsStarted("Failed to get back to the application, after enabling GPS location.");
    }
}
