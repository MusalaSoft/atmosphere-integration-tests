package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCameraNotPresent;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCameraPresent;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeThat;

import org.junit.After;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

public class EmulatorCameraTest extends BaseIntegrationTest {

    @Test
    public void emulatorMissingCameraTest() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
        startMainActivity();

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
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
        startMainActivity();

        boolean expectedCamera = testDevice.getInformation().hasCamera();
        assumeThat(expectedCamera, is(true));

        final String ERROR_MESSAGE = "Device does not have hardware camera!";
        assertCameraPresent(ERROR_MESSAGE);
    }

    @After
    public void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }
}
