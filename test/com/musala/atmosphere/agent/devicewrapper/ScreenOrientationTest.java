package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAutoRotationOff;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAutoRotationOn;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScreenOrientation;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.ScreenOrientation;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class ScreenOrientationTest extends BaseIntegrationTest {
    private static final long OPERATION_TIMEOUT = 4000;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        initTestDevice(testDeviceParams);
        installValidatorApplication();
        testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY, true);
        Thread.sleep(2000);
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void testSettingAutoRotationOff() throws Exception {
        assertTrue("Setting auto rotation returned false.", testDevice.setAutoRotation(false));
        Thread.sleep(OPERATION_TIMEOUT);
        assertAutoRotationOff("Auto rotation is not off.");
    }

    @Test
    public void testSettingAutoRotationOn() throws Exception {
        assertTrue("Setting auto rotation returned false.", testDevice.setAutoRotation(true));
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
