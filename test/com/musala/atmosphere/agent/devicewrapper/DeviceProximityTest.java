package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertDeviceProximity;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertGetProximityValue;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startProximityActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.beans.DeviceProximity;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

/**
 * @author simeon.ivanov
 */
public class DeviceProximityTest extends BaseIntegrationTest {

    private static final int SETTING_PROXIMITY_TIMEOUT = 1000;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);

        setTestDevice(testDevice);

        startProximityActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testSetProximityNear() {
        testDevice.setProximity(DeviceProximity.BINARY_NEAR_VALUE);

        String proximityNearValueText = String.valueOf(DeviceProximity.BINARY_NEAR_VALUE);

        assertDeviceProximity("The device proximity was not set correctly.", proximityNearValueText);
    }

    @Test
    public void testSetProximityFar() {
        testDevice.setProximity(DeviceProximity.BINARY_FAR_VALUE);

        String proximityFarValueText = String.valueOf(DeviceProximity.BINARY_FAR_VALUE);

        assertDeviceProximity("The device proximity was not set correctly.", proximityFarValueText);
    }

    @Test
    public void testGetProximity() throws InterruptedException {
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
