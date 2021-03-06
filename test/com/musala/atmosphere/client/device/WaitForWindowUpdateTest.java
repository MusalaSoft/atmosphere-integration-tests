package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWaitTestActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

public class WaitForWindowUpdateTest extends BaseIntegrationTest {
    private static final String MAIN_ACTIVITY_SWITCHER_DESCRIPTOR = "MainActivityButton";

    private static final int WINDOW_UPDATE_TIMEOUT = 10000;

    private static final String VALIDATOR_PACKAGE = "com.musala.atmosphere.ondevice.validator";

    private static final String UNEXISTING_PACKAGE = "com.made.up";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @After
    public void stopValidator() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @Test
    public void testWaitForWindowUpdateAnyPackage() throws Exception {
        startWaitTestActivity();

        UiElementSelector activitySwitcherSelector = new UiElementSelector();
        activitySwitcherSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                       MAIN_ACTIVITY_SWITCHER_DESCRIPTOR);

        Screen deviceActiveScreen = testDevice.getActiveScreen();
        UiElement activitySwitchButton = deviceActiveScreen.getElement(activitySwitcherSelector);
        activitySwitchButton.tap();

        boolean waitResult = deviceActiveScreen.waitForWindowUpdate(null, WINDOW_UPDATE_TIMEOUT);
        assertTrue("Wait method timed out without detecting a window update.", waitResult);
    }

    @Test
    public void testWaitForWindowUpdateValidator() throws Exception {
        startWaitTestActivity();

        UiElementSelector activitySwitcherSelector = new UiElementSelector();
        activitySwitcherSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                       MAIN_ACTIVITY_SWITCHER_DESCRIPTOR);

        Screen deviceActiveScreen = testDevice.getActiveScreen();
        UiElement activitySwitchButton = deviceActiveScreen.getElement(activitySwitcherSelector);
        activitySwitchButton.tap();

        boolean waitResult = deviceActiveScreen.waitForWindowUpdate(VALIDATOR_PACKAGE, WINDOW_UPDATE_TIMEOUT);
        assertTrue("Wait method timed out without detecting a window update.", waitResult);
    }

    @Test
    public void testWaitForWindowUpdateNoUpdate() throws Exception {
        startWaitTestActivity();

        Screen deviceActiveScreen = testDevice.getActiveScreen();

        boolean waitResult = deviceActiveScreen.waitForWindowUpdate(VALIDATOR_PACKAGE, WINDOW_UPDATE_TIMEOUT);
        assertFalse("Wait method indicated success, when no window update occurred.", waitResult);
    }

    @Test
    public void testWaitForWindowUpdateWrongPackage() throws Exception {
        startWaitTestActivity();

        UiElementSelector activitySwitcherSelector = new UiElementSelector();
        activitySwitcherSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                       MAIN_ACTIVITY_SWITCHER_DESCRIPTOR);

        Screen deviceActiveScreen = testDevice.getActiveScreen();
        UiElement activitySwitchButton = deviceActiveScreen.getElement(activitySwitcherSelector);
        activitySwitchButton.tap();

        boolean waitResult = deviceActiveScreen.waitForWindowUpdate(UNEXISTING_PACKAGE, WINDOW_UPDATE_TIMEOUT);
        assertFalse("Wait method indicated success, when window update was from a different package.", waitResult);
    }
}
