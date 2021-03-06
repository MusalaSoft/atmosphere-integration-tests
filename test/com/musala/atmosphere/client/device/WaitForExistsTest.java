package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertUIElementNotOnScreen;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWaitTestActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

public class WaitForExistsTest extends BaseIntegrationTest {

    private static final Integer ELEMENT_WAIT_TIMEOUT = 10000;

    private static final String UNEXISTING_BUTTON_DESCRIPTOR = "UnexistingButton";

    private static final String CHANGING_TEXT_BUTTON_DESCRIPTOR = "ChangingTextButton";

    private static final String CHANGING_TEXT_BUTTON_CHANGED_TEXT = "Changed text button";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        startWaitTestActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void waitForExistingElement() throws Exception {

        UiElementSelector selector = new UiElementSelector();
        selector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, CHANGING_TEXT_BUTTON_DESCRIPTOR);
        Screen activeScreen = testDevice.getActiveScreen();
        boolean result = activeScreen.waitForElementExists(selector, ELEMENT_WAIT_TIMEOUT);
        assertTrue("Wait for element exists returned false.", result);
    }

    @Test
    public void waitForUnexistingElement()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException {

        UiElementSelector selector = new UiElementSelector();
        selector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, UNEXISTING_BUTTON_DESCRIPTOR);
        Screen activeScreen = testDevice.getActiveScreen();
        boolean result = activeScreen.waitForElementExists(selector, ELEMENT_WAIT_TIMEOUT);

        assertFalse("Wait for element exists returned true.", result);
    }

    @Test
    public void waitForAppearingElement() throws Exception {

        UiElementSelector selector = new UiElementSelector();
        selector.addSelectionAttribute(CssAttribute.TEXT, CHANGING_TEXT_BUTTON_CHANGED_TEXT);
        selector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, CHANGING_TEXT_BUTTON_DESCRIPTOR);

        assertUIElementNotOnScreen("Validation element exists on the screen at the beginning of the test.", selector);

        UiElementSelector buttonSelector = new UiElementSelector();
        buttonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, CHANGING_TEXT_BUTTON_DESCRIPTOR);

        Screen activeScreen = testDevice.getActiveScreen();

        UiElement buttonElement = activeScreen.getElement(buttonSelector);
        buttonElement.tap();

        boolean result = activeScreen.waitForElementExists(selector, ELEMENT_WAIT_TIMEOUT);

        assertTrue("Wait for element exists returned false.", result);
    }
}
