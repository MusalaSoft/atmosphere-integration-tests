package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertElementText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.geometry.Point;
import com.musala.atmosphere.commons.ui.UiElementPropertiesContainer;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author yavor.stankov
 * 
 */
public class CutTextTest extends BaseIntegrationTest {
    private static final String CUT_TEXT_FAILED_MESSAGE = "Cut text failed! The text field is not empty.";

    private static final String PASTE_TEXT_FAILED_MESSAGE = "Paste text failed! The text field content does not match the expected one.";

    private static final String PASTE_BUTTON_CONTENT_DESCRIPTOR = "Paste";

    private static final String SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR = "Select all";

    private static final int LONG_PRESS_TIMEOUT = 1500;

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

    @Before
    public void setUpTest() throws Exception {
        startImeTestActivity();
    }

    @After
    public void tearDownTest() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @Test
    public void testCutTextSingleLine() throws Exception {
        Screen screen = testDevice.getActiveScreen();

        UiElement singleLineTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());
        UiElementPropertiesContainer singleLineTextBoxProperties = singleLineTextBox.getProperties();

        String expectedText = singleLineTextBoxProperties.getText();

        singleLineTextBox.longPress();

        screen.updateScreen();

        UiElement selectAllButton = getElementByContentDescriptor(SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR);
        selectAllButton.tap();

        singleLineTextBox.cutText();

        screen.updateScreen();

        singleLineTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());

        assertElementText(CUT_TEXT_FAILED_MESSAGE, singleLineTextBox, null);

        UiElement pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());

        Point pasteTextBoxBeginning = new Point(0, 0);
        pasteTextBox.longPress(pasteTextBoxBeginning, LONG_PRESS_TIMEOUT);

        screen.updateScreen();

        selectAllButton = getElementByContentDescriptor(SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR);
        selectAllButton.tap();

        UiElement pasteButton = getElementByContentDescriptor(PASTE_BUTTON_CONTENT_DESCRIPTOR);
        pasteButton.tap();

        screen.updateScreen();

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());

        assertElementText(PASTE_TEXT_FAILED_MESSAGE, pasteTextBox, expectedText);
    }

    @Test
    public void testCutTextMultiline() throws Exception {
        Screen screen = testDevice.getActiveScreen();

        UiElement multilineTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_FIRST_LINE_TEXT_BOX.toString());
        UiElementPropertiesContainer multilineTextBoxProperties = multilineTextBox.getProperties();

        String expectedText = multilineTextBoxProperties.getText();

        multilineTextBox.longPress();

        screen.updateScreen();

        UiElement selectAllButton = getElementByContentDescriptor(SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR);
        selectAllButton.tap();

        multilineTextBox.cutText();

        screen.updateScreen();

        multilineTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_FIRST_LINE_TEXT_BOX.toString());

        assertElementText(CUT_TEXT_FAILED_MESSAGE, multilineTextBox, null);

        UiElement pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());

        Point pasteTextBoxBeginning = new Point(0, 0);
        pasteTextBox.longPress(pasteTextBoxBeginning, LONG_PRESS_TIMEOUT);

        screen.updateScreen();

        selectAllButton = getElementByContentDescriptor(SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR);
        selectAllButton.tap();

        UiElement pasteButton = getElementByContentDescriptor(PASTE_BUTTON_CONTENT_DESCRIPTOR);
        pasteButton.tap();

        screen.updateScreen();

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());

        assertElementText(PASTE_TEXT_FAILED_MESSAGE, pasteTextBox, expectedText);
    }
}
