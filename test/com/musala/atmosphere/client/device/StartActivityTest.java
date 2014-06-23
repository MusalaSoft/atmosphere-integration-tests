package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class StartActivityTest extends BaseIntegrationTest {
    private final static String VALIDATOR_APP_PACKAGE = "com.musala.atmosphere.ondevice.validator";

    private final static String VALIDATOR_APP_ACTIVITY = ".MainActivity";

    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());
        installValidatorApplication();
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void testStartActivity() throws Exception {
        assertTrue("Starting the activity returned false.",
                   testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY, true));

        Screen activeScreen = testDevice.getActiveScreen();
        activeScreen.waitForWindowUpdate(VALIDATOR_APP_PACKAGE, 1000);
        assertValidatorIsStarted();
    }
}
