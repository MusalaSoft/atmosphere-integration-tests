package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertElementText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author yavor.stankov
 * 
 */
public class PasteTextTest extends BaseIntegrationTest {
    private static final String EXPECTED_TEXT_RESULT = "Sample Text";

    private static final String COPY_BUTTON_CONTENT_DESCRIPTOR = "Copy";

    private static final String SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR = "Select all";

    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);

        startImeTestActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testPasteText() throws Exception {
        Screen screen = testDevice.getActiveScreen();

        UiElement copyTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());
        copyTextBox.longPress();

        screen.updateScreen();

        UiElementSelector selectAllButtonSelector = new UiElementSelector();
        selectAllButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                      SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR);

        UiElement selectAllButton = screen.getElement(selectAllButtonSelector);
        selectAllButton.tap();

        UiElementSelector copyButtonSelector = new UiElementSelector();
        copyButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, COPY_BUTTON_CONTENT_DESCRIPTOR);

        UiElement copyButton = screen.getElement(copyButtonSelector);
        copyButton.tap();

        UiElement pasteTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        pasteTextBox.pasteText();

        screen.updateScreen();

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());

        assertElementText("Paste text failed! The text field content does not match the expected one.",
                          pasteTextBox,
                          EXPECTED_TEXT_RESULT);
    }
}
