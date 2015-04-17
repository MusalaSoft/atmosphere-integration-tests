package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

public class StopBackgroundProcessTest extends BaseIntegrationTest {

    private static final int APPLICATION_GO_BACKGROUND_TIMEOUT = 2000;

    private static final String SYSTEM_PROCESS_PACKAGE = "com.android.phone";

    private static final String HOME_SCREEN_PACKAGE = "com.android.launcher";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testStopForegroundProcess() throws Exception {
        startMainActivity();
        boolean result = testDevice.stopBackgroundProcess(VALIDATOR_APP_PACKAGE);
        assertTrue("Shell command execution returned exception", result);
        assertTrue("Foreground process should't be stopped after stopBackgroundProcess",
                   testDevice.isProcessRunning(VALIDATOR_APP_PACKAGE));

    }

    @Test
    public void testStopBackgroundProcess() throws Exception {

        startMainActivity();
        testDevice.pressButton(HardwareButton.HOME);
        testDevice.getActiveScreen().waitForWindowUpdate(HOME_SCREEN_PACKAGE, APPLICATION_GO_BACKGROUND_TIMEOUT);
        boolean result = testDevice.stopBackgroundProcess(VALIDATOR_APP_PACKAGE);
        assertTrue("Shell command excecution return exception", result);
        assertFalse("isProcessRunning returned true after stopping background Process",
                    testDevice.isProcessRunning(VALIDATOR_APP_PACKAGE));
    }

    @Test
    public void testStopBackgroundSystemProcess() throws Exception {

        boolean result = testDevice.stopBackgroundProcess(SYSTEM_PROCESS_PACKAGE);
        assertTrue("Shell command excecution return exception", result);
        assertTrue("isProcessRunning returned false when system process is running",
                   testDevice.isProcessRunning(SYSTEM_PROCESS_PACKAGE));
    }
}
