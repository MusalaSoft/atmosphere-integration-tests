package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAccelerationX;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAccelerationY;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAccelerationZ;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startAccelerationActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeNotNull;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.beans.DeviceAcceleration;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class DeviceAcceleratoinTest extends BaseIntegrationTest {
    // FIXME: Dirty fix - waits until the device screen rotation is over. It should be fixed!!!
    private static final Logger LOGGER = Logger.getLogger(DeviceAcceleratoinTest.class.getCanonicalName());

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

        startAccelerationActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (testDevice != null) {
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        }

        releaseDevice();
    }

    @Test
    public void testDeviceRandomAccelerationSetting() throws Exception {
        // set "random" acceleration
        final float accelerationX = 3.087f;
        final float accelerationY = -1.130f;
        final float accelerationZ = 5.904f;
        DeviceAcceleration deviceAcceleration = new DeviceAcceleration(accelerationX, accelerationY, accelerationZ);
        testDevice.setAcceleration(deviceAcceleration);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertAccelerationX("Device acceleration on the X axis not set to the expected value.", accelerationX);
        assertAccelerationY("Device acceleration on the Y axis not set to the expected value.", accelerationY);
        assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.", accelerationZ);
    }

    @Test
    public void testDeviceLandscapeAccelerationSetting() throws Exception {
        // set landscape acceleration
        DeviceAcceleration landscapeAcceleration = DeviceAcceleration.getLandscape();
        testDevice.setAcceleration(landscapeAcceleration);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
                            landscapeAcceleration.getAccelerationX());
        assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
                            landscapeAcceleration.getAccelerationY());
        assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
                            landscapeAcceleration.getAccelerationZ());
    }

    public void testDeviceReverseLandscapeAccelerationSetting() throws Exception {
        // set landscape acceleration
        DeviceAcceleration reverseLandscapeAcceleration = DeviceAcceleration.getReverseLandscape();
        testDevice.setAcceleration(reverseLandscapeAcceleration);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
                            reverseLandscapeAcceleration.getAccelerationX());
        assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
                            reverseLandscapeAcceleration.getAccelerationY());
        assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
                            reverseLandscapeAcceleration.getAccelerationZ());
    }

    @Test
    public void testDevicePortraitAccelerationSetting() throws Exception {
        // set portrait acceleration
        DeviceAcceleration portraitAcceleration = DeviceAcceleration.getPortrait();
        testDevice.setAcceleration(portraitAcceleration);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
                            portraitAcceleration.getAccelerationX());
        assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
                            portraitAcceleration.getAccelerationY());
        assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
                            portraitAcceleration.getAccelerationZ());
    }

    @Test
    public void testDeviceReversePortraitAccelerationSetting() throws Exception {
        // set portrait acceleration
        DeviceAcceleration reversePortraitAcceleration = DeviceAcceleration.getReversePortrait();
        testDevice.setAcceleration(reversePortraitAcceleration);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
                            reversePortraitAcceleration.getAccelerationX());
        assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
                            reversePortraitAcceleration.getAccelerationY());
        assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
                            reversePortraitAcceleration.getAccelerationZ());
    }

    @Test
    public void testDeviceLieDownAccelerationSetting() throws Exception {
        // set portrait acceleration
        DeviceAcceleration lieDownAcceleration = DeviceAcceleration.getLieDown();
        testDevice.setAcceleration(lieDownAcceleration);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
                            lieDownAcceleration.getAccelerationX());
        assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
                            lieDownAcceleration.getAccelerationY());
        assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
                            lieDownAcceleration.getAccelerationZ());
    }

    @Test
    public void testDeviceReverseLieDownAccelerationSetting() throws Exception {
        // set portrait acceleration
        DeviceAcceleration reverseLieDownAcceleration = DeviceAcceleration.getReverseLieDown();
        testDevice.setAcceleration(reverseLieDownAcceleration);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
                            reverseLieDownAcceleration.getAccelerationX());
        assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
                            reverseLieDownAcceleration.getAccelerationY());
        assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
                            reverseLieDownAcceleration.getAccelerationZ());
    }

    @Test
    public void testGetDeviceAcceleration() throws Exception {
        // Getting device acceleration works for both real devices and emulators.
        DeviceAcceleration deviceAcceleration = testDevice.getDeviceAcceleration();
        String validatorAccelerationX = getElementText(ContentDescriptor.ACCELERATION_X_BOX);
        String validatorAccelerationY = getElementText(ContentDescriptor.ACCELERATION_Y_BOX);
        String validatorAccelerationZ = getElementText(ContentDescriptor.ACCELERATION_Z_BOX);
        assertEquals("Received acceleration value on the X axis does not match the on-device information.",
                     validatorAccelerationX,
                     String.valueOf(deviceAcceleration.getAccelerationX()));
        assertEquals("Received acceleration value on the Y axis does not match the on-device information.",
                     validatorAccelerationY,
                     String.valueOf(deviceAcceleration.getAccelerationY()));
        assertEquals("Received acceleration value on the Z axis does not match the on-device information.",
                     validatorAccelerationZ,
                     String.valueOf(deviceAcceleration.getAccelerationZ()));
    }

    private String getElementText(ContentDescriptor contentDescriptor) throws Exception {
        UiElement elementByContentDescriptor = getElementByContentDescriptor(contentDescriptor.toString());
        return elementByContentDescriptor.getProperties().getText();
    }
}
