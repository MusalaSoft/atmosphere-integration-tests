package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertDeviceProximity;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startProximityActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.beans.DeviceProximity;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

/**
 * @author simeon.ivanov
 */
public class DeviceProximityTest extends BaseIntegrationTest {

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters emulatorTestDevice = new DeviceParameters();
        emulatorTestDevice.setDeviceType(DeviceType.EMULATOR_ONLY);
        initTestDevice(emulatorTestDevice);

        setTestDevice(testDevice);

        startProximityActivity();
    }

    @AfterClass
    public static void tearDown() {
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
}
