package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertMagneticFieldX;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertMagneticFieldY;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertMagneticFieldZ;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMagneticFieldActivity;
import static org.junit.Assume.assumeNotNull;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.beans.DeviceMagneticField;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

public class DeviceMagneticFieldTest extends BaseIntegrationTest {
    // FIXME: Dirty fix - waits until the device screen rotation is over. It should be fixed!!!
    private static final Logger LOGGER = Logger.getLogger(DeviceMagneticFieldTest.class.getCanonicalName());

    private final static int TIME_TO_WAIT_FOR_ROTATION = 10000; // in ms

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        try {
            initTestDevice(testDeviceSelector);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize a test device", e);
        }

        assumeNotNull(testDevice);

        setTestDevice(testDevice);

        startMagneticFieldActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (testDevice != null) {
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        }

        releaseDevice();
    }

    @Test
    public void testDeviceRandomMagneticFieldSetting() throws Exception {
        // set "random" magnetic field
        final float magneticFieldX = 30.087f;
        final float magneticFieldY = -17.130f;
        final float magneticFieldZ = 50.904f;
        DeviceMagneticField deviceMagneticField = new DeviceMagneticField(magneticFieldX,
                                                                          magneticFieldY,
                                                                          magneticFieldZ);
        testDevice.setMagneticField(deviceMagneticField);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertMagneticFieldX("Device magnetic field on the X axis not set to the expected value.", magneticFieldX);
        assertMagneticFieldY("Device magnetic field on the Y axis not set to the expected value.", magneticFieldY);
        assertMagneticFieldZ("Device magnetic field on the Z axis not set to the expected value.", magneticFieldZ);
    }

}
