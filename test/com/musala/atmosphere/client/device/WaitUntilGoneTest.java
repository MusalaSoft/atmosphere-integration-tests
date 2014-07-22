package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertUIElementOnScreen;
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
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class WaitUntilGoneTest extends BaseIntegrationTest {

    private static final Integer ELEMENT_WAIT_TIMEOUT = 10000;

    private static final String UNEXISTING_BUTTON_DESCRIPTOR = "UnexistingButton";

    private static final String CHANGING_TEXT_BUTTON_ORIGINAL_TEXT = "Text button";

    @BeforeClass
    public static void setUp()
        throws UiElementFetchingException,
            InterruptedException,
            ActivityStartingException,
            XPathExpressionException,
            InvalidCssQueryException {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);
        setTestDevice(testDevice);

        startWaitTestActivity();
    }

    @AfterClass
    public static void tearDown() throws UiElementFetchingException {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testWaitUntilPermamentlyExistingElementGone()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException {

        UiElementSelector selector = new UiElementSelector();
        selector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                       ContentDescriptor.CHANGING_TEXT_BUTTON_DESCRIPTOR.toString());
        Screen activeScreen = testDevice.getActiveScreen();
        boolean result = activeScreen.waitUntilElementGone(selector, ELEMENT_WAIT_TIMEOUT);
        assertFalse("Wait until element gone returned true.", result);
    }

    @Test
    public void testWaitUntilUnexistingElementGone()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException {

        UiElementSelector selector = new UiElementSelector();
        selector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, UNEXISTING_BUTTON_DESCRIPTOR);
        Screen activeScreen = testDevice.getActiveScreen();
        boolean result = activeScreen.waitUntilElementGone(selector, ELEMENT_WAIT_TIMEOUT);

        assertTrue("Wait until element gone returned false.", result);
    }

    @Test
    public void testWaitUntilTemporaryExistingElementGone()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {

        UiElementSelector selector = new UiElementSelector();
        selector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                       ContentDescriptor.CHANGING_TEXT_BUTTON_DESCRIPTOR.toString());

        assertUIElementOnScreen("Validation element does not exists on the screen at the beginning of the test.",
                                selector);

        Screen activeScreen = testDevice.getActiveScreen();

        UiElement buttonElement = activeScreen.getElement(selector);
        buttonElement.tap();

        selector.addSelectionAttribute(CssAttribute.TEXT, CHANGING_TEXT_BUTTON_ORIGINAL_TEXT);

        boolean result = activeScreen.waitUntilElementGone(selector, ELEMENT_WAIT_TIMEOUT);

        assertTrue("Wait until element gone returned false.", result);
    }
}
