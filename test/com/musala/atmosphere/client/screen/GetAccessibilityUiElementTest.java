package com.musala.atmosphere.client.screen;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startUiElementsActivity;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.MultipleElementsFoundException;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

/**
 * 
 * @author denis.bialev
 *
 */
public class GetAccessibilityUiElementTest extends BaseIntegrationTest {

    private static final String SELECTOR_PROPERTIES_MISSMATCH_MESSAGE = "The found element has different field values than the selector.";

    // TODO Find what invisible elements represent in UiAutomator and add tests for them

    private static final String VALIDATOR_BUTTON_CONTENT_DECSRIPTOR = "ATMOSPHEREValidator";

    private static final String BUTTON_CLASS_NAME = "android.widget.Button";

    private static final String FIRST_BUTTON_TEXT = "Button1";

    private static final String NON_EXISTENT_TEXT = "I don't exist";

    private static Screen screen;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder deviceSelectorBuilder = new DeviceSelectorBuilder();
        DeviceSelector deviceSelector = deviceSelectorBuilder.deviceType(DeviceType.DEVICE_PREFERRED).build();
        initTestDevice(deviceSelector);
        setTestDevice(testDevice);
        screen = testDevice.getActiveScreen();

        startUiElementsActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);

        releaseDevice();
    }

    @Test
    public void testGetVisibleUiElement() throws Exception {
        UiElementSelector elementSelector = new UiElementSelector();
        elementSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, BUTTON_CLASS_NAME);
        elementSelector.addSelectionAttribute(CssAttribute.TEXT, FIRST_BUTTON_TEXT);

        UiElement foundElement = screen.getElement(elementSelector);

        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE,
                     BUTTON_CLASS_NAME,
                     foundElement.getProperties().getClassName());
        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE, FIRST_BUTTON_TEXT, foundElement.getText());
    }

    @Test(expected = MultipleElementsFoundException.class)
    public void testGetMultipleUiElements() throws Exception {
        UiElementSelector elementSelector = new UiElementSelector();
        elementSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, BUTTON_CLASS_NAME);

        screen.getElement(elementSelector);
    }

    @Test(expected = UiElementFetchingException.class)
    public void testFindNonExistentElement() throws Exception {
        UiElementSelector elementSelector = new UiElementSelector();
        elementSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, NON_EXISTENT_TEXT);

        screen.getElement(elementSelector);
    }

    @Test
    public void testGetElementWhenContentDescriptionIsUninque() throws Exception {
        UiElementSelector elementSelector = new UiElementSelector();
        elementSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, VALIDATOR_BUTTON_CONTENT_DECSRIPTOR);

        UiElement foundElement = screen.getElement(elementSelector);

        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE,
                     VALIDATOR_BUTTON_CONTENT_DECSRIPTOR,
                     foundElement.getProperties().getContentDescriptor());
    }

    @Test
    public void testGetElementWhenTextIsUnique() throws Exception {
        UiElementSelector elementSelector = new UiElementSelector();
        elementSelector.addSelectionAttribute(CssAttribute.TEXT, FIRST_BUTTON_TEXT);

        UiElement foundElement = screen.getElement(elementSelector);

        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE, FIRST_BUTTON_TEXT, foundElement.getText());
    }
}
