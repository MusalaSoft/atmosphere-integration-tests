package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertUIElementNotOnScreen;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWaitTestActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class WaitForExistsTest extends BaseIntegrationTest {

    private static final Integer ELEMENT_WAIT_TIMEOUT = 10000;

    private static final String UNEXISTING_BUTTON_DESCRIPTOR = "UnexistingButton";

    private static final String CHANGING_TEXT_BUTTON_DESCRIPTOR = "ChangingTextButton";

    private static final String CHANGING_TEXT_BUTTON_CHANGED_TEXT = "Changed text button";

    @BeforeClass
    public static void setUp()
        throws UiElementFetchingException,
            InterruptedException,
            ActivityStartingException,
            XPathExpressionException,
            InvalidCssQueryException {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);

        startWaitTestActivity();
    }

    @AfterClass
    public static void tearDown() throws UiElementFetchingException {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void waitForExistingElement()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException {

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
    public void waitForAppearingElement()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {

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
