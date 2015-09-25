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

/**
 * 
 * @author denis.bialev
 *
 */
public class GetElementByCssTest extends BaseIntegrationTest {

    private static final String SELECTOR_PROPERTIES_MISSMATCH_MESSAGE = "The found element has different field values than the selector.";

    // TODO Find what invisible elements represent in UiAutomator and add tests for them

    private static final String VALIDATOR_BUTTON_CONTENT_DECSRIPTOR = "ATMOSPHEREValidator";

    private static final String BUTTON_CLASS_NAME = "android.widget.Button";

    private static final String FIRST_BUTTON_TEXT = "Button1";

    private static final String FIRST_BUTTON_CSS_QUERY = "[className=android.widget.Button][text=Button1]";

    private static final String BUTTON_CSS_QUERY = "[className=android.widget.Button]";

    private static final String NON_EXISTENT_ELEMENT_CSS_QUERY = "[text=nonExistent]";

    private static final String UNIQUE_TEXT_CSS_QUERY = "[text=Button1]";

    private static final String INIQUE_CONTENT_DESC_CSS_QUERY = "[contentDesc=ATMOSPHEREValidator]";

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
    public void testGetVisibleUiElementByCss() throws Exception {
        UiElement foundElement = screen.getElementByCSS(FIRST_BUTTON_CSS_QUERY);

        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE,
                     BUTTON_CLASS_NAME,
                     foundElement.getProperties().getClassName());
        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE, FIRST_BUTTON_TEXT, foundElement.getText());
    }

    @Test(expected = MultipleElementsFoundException.class)
    public void testGetMultipleUiElementsByCss() throws Exception {
        screen.getElementByCSS(BUTTON_CSS_QUERY);
    }

    @Test(expected = UiElementFetchingException.class)
    public void testFindNonExistentElementByCss() throws Exception {
        screen.getElementByCSS(NON_EXISTENT_ELEMENT_CSS_QUERY);
    }

    @Test
    public void testGetElementByCssWhenContentDescriptorIsUninque() throws Exception {
        UiElement foundElement = screen.getElementByCSS(INIQUE_CONTENT_DESC_CSS_QUERY);

        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE,
                     VALIDATOR_BUTTON_CONTENT_DECSRIPTOR,
                     foundElement.getProperties().getContentDescriptor());
    }

    @Test
    public void testGetElementWhenTextIsUniqueByCss() throws Exception {
        UiElement foundElement = screen.getElementByCSS(UNIQUE_TEXT_CSS_QUERY);

        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE, FIRST_BUTTON_TEXT, foundElement.getText());
    }
}
