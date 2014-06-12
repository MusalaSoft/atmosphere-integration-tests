package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCameraNotPresent;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCameraPresent;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setupAndStartMainActivity;
import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeThat;

import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

public class EmulatorCameraTest extends BaseIntegrationTest {

    @Test
    public void emulatorMissingCameraTest() throws Exception {

        DeviceParameters deviceParameters = new DeviceParameters();
        deviceParameters.setDeviceType(DeviceType.EMULATOR_ONLY); // these tests will work for emulators only

        initTestDevice(deviceParameters);
        setTestDevice(testDevice);
        setupAndStartMainActivity();

        // Note: this should be false, because we can't change camera settings of AVD when creating it through command
        // line as intended in ATMOSPHERE.
        boolean expectedCamera = testDevice.getInformation().hasCamera();
        assumeThat(expectedCamera, is(false));

        // Emulators have no hardware cameras, so the ondevicevalidator implementation will catch this case correctly.
        final String ERROR_MESSAGE = "Emulator has hardware camera!";
        assertCameraNotPresent(ERROR_MESSAGE);
    }

    @Test
    public void deviceCameraTest() throws Exception {

        DeviceParameters deviceParameters = new DeviceParameters();
        deviceParameters.setDeviceType(DeviceType.DEVICE_PREFERRED); // these tests will work for emulators only

        initTestDevice(deviceParameters);
        setTestDevice(testDevice);
        setupAndStartMainActivity();

        boolean expectedCamera = testDevice.getInformation().hasCamera();
        assumeThat(expectedCamera, is(true));

        final String ERROR_MESSAGE = "Device does not have hardware camera!";
        assertCameraPresent(ERROR_MESSAGE);
    }
}
