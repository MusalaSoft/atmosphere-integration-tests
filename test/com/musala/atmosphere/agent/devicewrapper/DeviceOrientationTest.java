package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationAzimuth;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationPitch;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationRoll;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startOrientationActivity;
import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.beans.DeviceOrientation;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

/**
 * 
 * @author valyo.yolovski
 * 
 */
public class DeviceOrientationTest extends BaseIntegrationTest {
    // FIXME: Dirty fix - waits until the device screen rotation is over. It should be fixed!!!
    private final static int TIME_TO_WAIT_FOR_ROTATION = 5000; // in ms

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
        startOrientationActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    // @Test
    public void testSetRandomDeviceOrientation() throws Exception {
        final float orientationAzimuth = 17.087f;
        final float orientationPitch = -13.130f;
        final float orientationRoll = 55.904f;
        DeviceOrientation deviceOrientation = new DeviceOrientation(orientationAzimuth,
                                                                    orientationPitch,
                                                                    orientationRoll);
        testDevice.setDeviceOrientation(deviceOrientation);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertOrientationAzimuth("Device Azimuth not set to the expected value.", orientationAzimuth);
        assertOrientationPitch("Device pitch not set to the expected value.", orientationPitch);
        assertOrientationRoll("Device roll not set to the expected value.", orientationRoll);
    }

    // @Test
    public void testSetDeviceOrientationPortrait() throws Exception {
        DeviceOrientation portraitOrientation = DeviceOrientation.getPortraitOrientation();
        testDevice.setDeviceOrientation(portraitOrientation);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertOrientationAzimuth("Device Azimuth not set to the expected value.", portraitOrientation.getAzimuth());
        assertOrientationPitch("Device pitch not set to the expected value.", portraitOrientation.getPitch());
        assertOrientationRoll("Device roll not set to the expected value.", portraitOrientation.getRoll());
    }

    // @Test
    public void testSetDeviceOrientationUpsideDownPortrait() throws Exception {
        DeviceOrientation upsideDownPortraitOrientation = DeviceOrientation.getUpsideDownPortrait();
        testDevice.setDeviceOrientation(upsideDownPortraitOrientation);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertOrientationAzimuth("Device Azimuth not set to the expected value.",
                                 upsideDownPortraitOrientation.getAzimuth());
        assertOrientationPitch("Device pitch not set to the expected value.", upsideDownPortraitOrientation.getPitch());
        assertOrientationRoll("Device roll not set to the expected value.", upsideDownPortraitOrientation.getRoll());
    }

    // @Test
    public void testSetDeviceOrientationLandscape() throws Exception {
        DeviceOrientation landscapeOrientation = DeviceOrientation.getLandscapeOrientation();
        testDevice.setDeviceOrientation(landscapeOrientation);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertOrientationAzimuth("Device Azimuth not set to the expected value.", landscapeOrientation.getAzimuth());
        assertOrientationPitch("Device pitch not set to the expected value.", landscapeOrientation.getPitch());
        assertOrientationRoll("Device roll not set to the expected value.", landscapeOrientation.getRoll());
    }

    // @Test
    public void testSetDeviceOrientationUpsideDownLandscape() throws Exception {
        DeviceOrientation upsideDownLandscapeOrientation = DeviceOrientation.getUpsideDownLandscape();
        testDevice.setDeviceOrientation(upsideDownLandscapeOrientation);

        Thread.sleep(TIME_TO_WAIT_FOR_ROTATION);

        assertOrientationAzimuth("Device Azimuth not set to the expected value.",
                                 upsideDownLandscapeOrientation.getAzimuth());
        assertOrientationPitch("Device pitch not set to the expected value.", upsideDownLandscapeOrientation.getPitch());
        assertOrientationRoll("Device roll not set to the expected value.", upsideDownLandscapeOrientation.getRoll());
    }

    @Test
    public void testGetDeviceOrientation() throws Exception {
        // Getting device orientation works for both real devices and emulators.
        DeviceOrientation deviceOrientation = testDevice.getDeviceOrientation();
        assertNotNull("Failed getting device orientation.", deviceOrientation);
        assertNotNull("Failed getting device azimuth value.", deviceOrientation.getAzimuth());
        assertNotNull("Failed getting device pitch value.", deviceOrientation.getPitch());
        assertNotNull("Failed getting device rol value.", deviceOrientation.getRoll());
    }
}
