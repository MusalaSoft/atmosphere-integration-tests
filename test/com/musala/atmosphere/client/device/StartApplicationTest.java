package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class StartApplicationTest extends BaseIntegrationTest {

    private static int START_APPLICATION_TIMEOUT = 1000;

    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());
        installValidatorApplication();
        testDevice.pressButton(HardwareButton.HOME);
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void testStartApplication() throws Exception {
        boolean result = testDevice.startApplication(VALIDATOR_APP_PACKAGE);
        Thread.sleep(START_APPLICATION_TIMEOUT);

        assertTrue("startApplication returned false.", result);
        assertValidatorIsStarted();
    }
}
