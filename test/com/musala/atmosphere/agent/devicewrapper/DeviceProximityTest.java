package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertDeviceProximity;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertGetProximityValue;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startProximityActivity;
import static org.junit.Assume.assumeNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.beans.DeviceProximity;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;

/**
 * @author simeon.ivanov
 */
public class DeviceProximityTest extends BaseIntegrationTest {

    private static final int SETTING_PROXIMITY_TIMEOUT = 1000;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        try {
            initTestDevice(testDeviceSelector);
            setTestDevice(testDevice);

            startProximityActivity();
        } catch (NoAvailableDeviceFoundException e) {
            // Nothing to do here
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (testDevice != null) {
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        }
        releaseDevice();
    }

    @Test
    public void testSetProximityNear() {
        assumeNotNull(testDevice);
        testDevice.setProximity(DeviceProximity.BINARY_NEAR_VALUE);

        String proximityNearValueText = String.valueOf(DeviceProximity.BINARY_NEAR_VALUE);

        assertDeviceProximity("The device proximity was not set correctly.", proximityNearValueText);
    }

    @Test
    public void testSetProximityFar() {
        assumeNotNull(testDevice);
        testDevice.setProximity(DeviceProximity.BINARY_FAR_VALUE);

        String proximityFarValueText = String.valueOf(DeviceProximity.BINARY_FAR_VALUE);

        assertDeviceProximity("The device proximity was not set correctly.", proximityFarValueText);
    }

    @Test
    public void testGetProximity() throws InterruptedException {
        assumeNotNull(testDevice);
        testDevice.setProximity(DeviceProximity.BINARY_FAR_VALUE);

        Thread.sleep(SETTING_PROXIMITY_TIMEOUT);

        assertGetProximityValue("The value returned by the get proximity method did not match the expected value.",
                                DeviceProximity.BINARY_FAR_VALUE);

        testDevice.setProximity(DeviceProximity.BINARY_NEAR_VALUE);

        Thread.sleep(SETTING_PROXIMITY_TIMEOUT);

        assertGetProximityValue("The value returned by the get proximity method did not match the expected value.",
                                DeviceProximity.BINARY_NEAR_VALUE);
    }
}
