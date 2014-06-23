package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startServiceActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class ForceStopProcessTest extends BaseIntegrationTest {

    private static final int APPLICATION_GO_BACKGROUND_TIMEOUT = 2000;

    private static final String SYSTEM_PROCESS_PACKAGE = "com.android.phone";

    private static final String HOME_SCREEN_PACKAGE = "com.android.launcher";

    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testForceStopForegroundProcess() throws Exception {
        startMainActivity();
        boolean result = testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        assertTrue("Shell command excecution return exception", result);
        assertFalse("isProcessRunning returned true after stopping foreground Process",
                    testDevice.isProcessRunning(VALIDATOR_APP_PACKAGE));
    }

    @Test
    public void testForceStopProcessWithService() throws Exception {
        startServiceActivity();
        boolean result = testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        assertTrue("Shell command excecution return exception", result);
        assertFalse("isProcessRunning returned true after stopping process with active service",
                    testDevice.isProcessRunning(VALIDATOR_APP_PACKAGE));
    }

    @Test
    public void testForceStopBackgroundProcess() throws Exception {
        startMainActivity();
        testDevice.pressButton(HardwareButton.HOME);
        testDevice.getActiveScreen().waitForWindowUpdate(HOME_SCREEN_PACKAGE, APPLICATION_GO_BACKGROUND_TIMEOUT);
        boolean result = testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        assertTrue("Shell command excecution return exception", result);
        assertFalse("isProcessRunning returned true after stopping background Process",
                    testDevice.isProcessRunning(VALIDATOR_APP_PACKAGE));
    }

    @Test
    public void testForceStopSystemProcess() throws Exception {
        boolean result = testDevice.forceStopProcess(SYSTEM_PROCESS_PACKAGE);
        assertTrue("Shell command excecution return exception", result);
        assertTrue("isProcessRunning returned false when system process is running",
                   testDevice.isProcessRunning(SYSTEM_PROCESS_PACKAGE));
    }
}
