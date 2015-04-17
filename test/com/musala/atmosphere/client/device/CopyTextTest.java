package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertElementText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertTextIsCleared;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;
import static org.junit.Assert.assertTrue;

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
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author filareta.yordanova
 * 
 */
public class CopyTextTest extends BaseIntegrationTest {
    private static final String TEXT_MISSMATCH_ERROR_MESSAGE = "Text field content does not match the expected text before performing copy operation.";

    private static final String FAILURE_MESSAGE = "Copy selected text failed! Text field content does not match the expected one.";

    private static final int LONG_PRESS_TIMEOUT = 2000;

    private static final String EXPECTED_TEXT_RESULT = "Sample Text";

    private static final String EXPECTED_TEXT_BEFORE_COPY = "Paste Here";

    private static final String PASTE_BUTTON_CONTENT_DESCRIPTOR = "Paste";

    private static final String SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR = "Select all";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
    }

    @Before
    public void setUpTest() throws Exception {
        startImeTestActivity();
    }

    @After
    public void tearDownTest() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Test
    public void testCopyTextWhenTextIsSelected() throws Exception {
        Screen screen = testDevice.getActiveScreen();

        UiElement copyTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());
        copyTextBox.longPress();

        screen.updateScreen();

        UiElementSelector selectAllButtonSelector = new UiElementSelector();
        selectAllButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                      SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR);

        UiElement selectAllButton = screen.getElement(selectAllButtonSelector);
        selectAllButton.tap();

        assertTrue(copyTextBox.copyText());

        UiElement pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());

        Point pasteTextBoxBeginning = new Point(0, 0);
        pasteTextBox.longPress(pasteTextBoxBeginning, LONG_PRESS_TIMEOUT);

        assertElementText(TEXT_MISSMATCH_ERROR_MESSAGE, pasteTextBox, EXPECTED_TEXT_BEFORE_COPY);

        screen.updateScreen();

        selectAllButton = screen.getElement(selectAllButtonSelector);
        selectAllButton.tap();

        UiElementSelector pasteButtonSelector = new UiElementSelector();
        pasteButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, PASTE_BUTTON_CONTENT_DESCRIPTOR);

        UiElement pasteButton = screen.getElement(pasteButtonSelector);
        pasteButton.tap();

        screen.updateScreen();

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());

        assertElementText(FAILURE_MESSAGE, pasteTextBox, EXPECTED_TEXT_RESULT);
    }

    @Test
    public void testCopyTextWhenNoTextIsSelected() throws Exception {
        Screen screen = testDevice.getActiveScreen();

        UiElement copyTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        copyTextBox.longPress();

        assertTrue(copyTextBox.copyText());

        UiElement pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());

        Point pasteTextBoxBeginning = new Point(0, 0);
        pasteTextBox.longPress(pasteTextBoxBeginning, LONG_PRESS_TIMEOUT);

        assertElementText(TEXT_MISSMATCH_ERROR_MESSAGE, pasteTextBox, EXPECTED_TEXT_BEFORE_COPY);

        screen.updateScreen();

        UiElementSelector selectAllButtonSelector = new UiElementSelector();
        selectAllButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                      SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR);

        UiElement selectAllButton = screen.getElement(selectAllButtonSelector);
        selectAllButton.tap();

        UiElementSelector pasteButtonSelector = new UiElementSelector();
        pasteButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, PASTE_BUTTON_CONTENT_DESCRIPTOR);

        UiElement pasteButton = screen.getElement(pasteButtonSelector);
        pasteButton.tap();

        screen.updateScreen();

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());

        assertTextIsCleared(FAILURE_MESSAGE, ContentDescriptor.PASTE_CONTAINER_TEXT_BOX);
    }
}
