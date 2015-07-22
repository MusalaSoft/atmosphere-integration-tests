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
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

public class StopBackgroundProcessTest extends BaseIntegrationTest {

    private static final int APPLICATION_GO_BACKGROUND_TIMEOUT = 3000;

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
        testDevice.stopBackgroundProcess(VALIDATOR_APP_PACKAGE);
        assertTrue("Foreground process should't be stopped after stopBackgroundProcess",
                   testDevice.isProcessRunning(VALIDATOR_APP_PACKAGE));

    }

    @Test
    public void testStopBackgroundProcess() throws Exception {
        startMainActivity();
        testDevice.pressButton(HardwareButton.HOME);
        UiElementSelector selector = new UiElementSelector();
        selector.addSelectionAttribute(CssAttribute.PACKAGE_NAME, HOME_SCREEN_PACKAGE);
        testDevice.getActiveScreen().waitForElementExists(selector, APPLICATION_GO_BACKGROUND_TIMEOUT);
        testDevice.stopBackgroundProcess(VALIDATOR_APP_PACKAGE);
        assertFalse("isProcessRunning returned true after stopping background Process",
                    testDevice.isProcessRunning(VALIDATOR_APP_PACKAGE));
    }

    @Test
    public void testStopBackgroundSystemProcess() throws Exception {
        testDevice.stopBackgroundProcess(SYSTEM_PROCESS_PACKAGE);
        assertTrue("isProcessRunning returned false when system process is running",
                   testDevice.isProcessRunning(SYSTEM_PROCESS_PACKAGE));
    }
}
