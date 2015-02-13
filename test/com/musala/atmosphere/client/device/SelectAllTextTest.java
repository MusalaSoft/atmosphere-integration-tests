package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertElementText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;

import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

/**
 * 
 * @author yavor.stankov
 * 
 */
public class SelectAllTextTest extends BaseIntegrationTest {
    private final static String INPUT_TEXT_BOX_CONTENT_DESCRIPTOR = "InputTextBox";

    private final static String TEXT_TO_INPUT_FIRST = "Select All Text";

    private final static String TEXT_TO_INPUT_SECOND = "success";

    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);
        testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testSelectAllText()
        throws XPathExpressionException,
            UiElementFetchingException,
            InvalidCssQueryException,
            InterruptedException {
        Screen screen = testDevice.getActiveScreen();

        UiElement inputTextBox = getElementByContentDescriptor(INPUT_TEXT_BOX_CONTENT_DESCRIPTOR);
        inputTextBox.inputText(TEXT_TO_INPUT_FIRST);

        screen.updateScreen();

        inputTextBox = getElementByContentDescriptor(INPUT_TEXT_BOX_CONTENT_DESCRIPTOR);
        inputTextBox.selectAllText();
        inputTextBox.inputText(TEXT_TO_INPUT_SECOND);

        screen.updateScreen();

        inputTextBox = getElementByContentDescriptor(INPUT_TEXT_BOX_CONTENT_DESCRIPTOR);

        assertElementText("Select all text failed! The text field content does not match the expected one.",
                          inputTextBox,
                          TEXT_TO_INPUT_SECOND);
    }
}
