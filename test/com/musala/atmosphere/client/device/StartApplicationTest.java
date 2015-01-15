package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

public class StartApplicationTest extends BaseIntegrationTest {
    private static final int APPLICATION_START_TIMEOUT = 5000;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);
        setTestDevice(testDevice);
        testDevice.pressButton(HardwareButton.HOME);
        Thread.sleep(2000);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testStartApplication() throws Exception {
        boolean result = testDevice.startApplication(VALIDATOR_APP_PACKAGE);

        Screen activeScreen = testDevice.getActiveScreen();
        activeScreen.waitForWindowUpdate(VALIDATOR_APP_PACKAGE, APPLICATION_START_TIMEOUT);

        assertTrue("startApplication returned false.", result);
        assertValidatorIsStarted();
    }
}
