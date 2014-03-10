package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertMagneticFieldX;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertMagneticFieldY;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertMagneticFieldZ;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.beans.DeviceMagneticField;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

public class DeviceMagneticFieldTest extends BaseIntegrationTest {
    private final static String VALIDATOR_APP_MAGNETIC_FIELD_ACTIVITY = ".MagneticFieldActivity";

    // FIXME: Dirty fix - waits until the device screen rotation is over. It should be fixed!!!
    private final static int TIME_TO_WAIT_FOR_ROTATION = 10000; // in ms

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters emulatorTestDevice = new DeviceParameters();
        emulatorTestDevice.setDeviceType(DeviceType.EMULATOR_ONLY);
        initTestDevice(emulatorTestDevice);
        installValidatorApplication();
        testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_MAGNETIC_FIELD_ACTIVITY, true);
        Thread.sleep(1000);
    }

    @Test
    public void testDeviceRandomMagneticFieldSetting() throws UiElementFetchingException, InterruptedException {
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
