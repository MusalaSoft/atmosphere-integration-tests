package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAutoRotationOff;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAutoRotationOn;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScreenOrientation;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.ScreenOrientation;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

public class ScreenOrientationTest extends BaseIntegrationTest {
    private static final long OPERATION_TIMEOUT = 4000;

    private static ScreenOrientation defaultOrientation;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);

        setTestDevice(testDevice);

        defaultOrientation = testDevice.getScreenOrientation();

        startMainActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        testDevice.setScreenOrientation(defaultOrientation);
        releaseDevice();
    }

    @Test
    public void testDisablingScreenAutoRotation() throws Exception {
        assertTrue("Setting auto rotation returned false.", testDevice.disableScreenAutoRotation());
        Thread.sleep(OPERATION_TIMEOUT);
        assertAutoRotationOff("Auto rotation is not off.");
    }

    @Test
    public void testEnablingScreenAutoRotation() throws Exception {
        assertTrue("Setting auto rotation returned false.", testDevice.enableScreenAutoRotation());
        Thread.sleep(OPERATION_TIMEOUT);
        assertAutoRotationOn("Auto rotation is not on.");
    }

    @Test
    public void testSettingScreenOrientationPortrait() throws Exception {
        assertTrue("Setting screen orientation returned false.",
                   testDevice.setScreenOrientation(ScreenOrientation.PORTRAIT));
        Thread.sleep(OPERATION_TIMEOUT);
        assertScreenOrientation("Screen rotation expected to be portrait", ScreenOrientation.PORTRAIT);
    }

    @Test
    public void testSettingScreenOrientationUpsideDownPortrait() throws Exception {
        assertTrue("Setting screen orientation returned false.",
                   testDevice.setScreenOrientation(ScreenOrientation.UPSIDE_DOWN_PORTRAIT));
        Thread.sleep(OPERATION_TIMEOUT);
        assertScreenOrientation("Screen rotation expected to be upside down portrait",
                                ScreenOrientation.UPSIDE_DOWN_PORTRAIT);
    }

    @Test
    public void testSettingScreenOrientationLandscape() throws Exception {
        assertTrue("Setting screen orientation returned false.",
                   testDevice.setScreenOrientation(ScreenOrientation.LANDSCAPE));
        Thread.sleep(OPERATION_TIMEOUT);
        assertScreenOrientation("Screen rotation expected to be landscape", ScreenOrientation.LANDSCAPE);
    }

    @Test
    public void testSettingScreenOrientationUpsideDown() throws Exception {
        assertTrue("Setting screen orientation returned false.",
                   testDevice.setScreenOrientation(ScreenOrientation.UPSIDE_DOWN_LANDSCAPE));
        Thread.sleep(OPERATION_TIMEOUT);
        assertScreenOrientation("Screen rotation expected to be upside down landscape",
                                ScreenOrientation.UPSIDE_DOWN_LANDSCAPE);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testScreenOrientationWithNull() {
        testDevice.setScreenOrientation(null);
    }
}
