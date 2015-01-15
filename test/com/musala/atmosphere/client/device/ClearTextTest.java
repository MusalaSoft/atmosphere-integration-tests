package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertTextIsCleared;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;

import javax.xml.xpath.XPathExpressionException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class ClearTextTest extends BaseIntegrationTest {

    public static final String FAIL_MESSAGE = "The text has not been cleared.";

    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Before
    public void setUpTest() throws ActivityStartingException {
        testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
    }

    @After
    public void tearDownTest() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @Test
    public void testClearText()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        Screen screen = testDevice.getActiveScreen();
        UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.CLEAR_TEXT_BOX.toString());
        UiElementSelector boxSelector = inputTextBox.getElementSelector();
        String textToInput = "TEST";
        inputTextBox.inputText(textToInput);
        screen.updateScreen();
        inputTextBox = getElementByContentDescriptor(ContentDescriptor.CLEAR_TEXT_BOX.toString());
        inputTextBox.clearText();

        assertTextIsCleared(FAIL_MESSAGE);
    }

    @Test
    public void testClearTextMultiLine()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        Screen screen = testDevice.getActiveScreen();
        UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.CLEAR_TEXT_BOX.toString());
        UiElementSelector boxSelector = inputTextBox.getElementSelector();
        String textToInput = "TEST\nTest1\nTest2";
        inputTextBox.inputText(textToInput);
        screen.updateScreen();
        inputTextBox = getElementByContentDescriptor(ContentDescriptor.CLEAR_TEXT_BOX.toString());
        inputTextBox.clearText();

        assertTextIsCleared(FAIL_MESSAGE);
    }

    @Test
    public void testClearTextMultiLineLeadingLine()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        Screen screen = testDevice.getActiveScreen();
        UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.CLEAR_TEXT_BOX.toString());
        UiElementSelector boxSelector = inputTextBox.getElementSelector();
        String textToInput = "\nTEST\nTest1\nTest2";
        inputTextBox.inputText(textToInput);
        screen.updateScreen();
        inputTextBox = getElementByContentDescriptor(ContentDescriptor.CLEAR_TEXT_BOX.toString());
        inputTextBox.clearText();

        assertTextIsCleared(FAIL_MESSAGE);
    }
}
