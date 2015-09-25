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
public class GetElementByXPathTest extends BaseIntegrationTest {

    private static final String SELECTOR_PROPERTIES_MISSMATCH_MESSAGE = "The found element has different field values than the query.";

    // TODO Find what invisible elements represent in UiAutomator and add tests for them

    private static final String VALIDATOR_BUTTON_CONTENT_DECSRIPTOR = "ATMOSPHEREValidator";

    private static final String BUTTON_CLASS_NAME = "android.widget.Button";

    private static final String FIRST_BUTTON_TEXT = "Button1";

    private static final String FIRST_BUTTON_QUERY = "//*[@className='android.widget.Button' and @text='Button1']";

    private static final String BUTTON_QUERY = "//*[@className='android.widget.Button']";

    private static final String NON_EXISTENT_ELEMENT_QUERY = "//*[@text='nonExistent']";

    private static final String UNIQUE_TEXT_QUERY = "//*[@text='Button1']";

    private static final String INIQUE_CONTENT_DESC_QUERY = "//*[@contentDesc='ATMOSPHEREValidator']";

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
    public void testGetVisibleUiElementByXPath() throws Exception {
        UiElement foundElement = screen.getElementByXPath(FIRST_BUTTON_QUERY);

        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE,
                     BUTTON_CLASS_NAME,
                     foundElement.getProperties().getClassName());
        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE, FIRST_BUTTON_TEXT, foundElement.getText());
    }

    @Test(expected = MultipleElementsFoundException.class)
    public void testGetMultipleUiElementsByXPath() throws Exception {
        screen.getElementByXPath(BUTTON_QUERY);
    }

    @Test(expected = UiElementFetchingException.class)
    public void testFindNonExistentElementByXPath() throws Exception {
        screen.getElementByXPath(NON_EXISTENT_ELEMENT_QUERY);
    }

    @Test
    public void testGetElementByXPathWhenContentDescriptorIsUnique() throws Exception {
        UiElement foundElement = screen.getElementByXPath(INIQUE_CONTENT_DESC_QUERY);

        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE,
                     VALIDATOR_BUTTON_CONTENT_DECSRIPTOR,
                     foundElement.getProperties().getContentDescriptor());
    }

    @Test
    public void testGetElementByXPathWhenTextIsUnique() throws Exception {
        UiElement foundElement = screen.getElementByXPath(UNIQUE_TEXT_QUERY);

        assertEquals(SELECTOR_PROPERTIES_MISSMATCH_MESSAGE, FIRST_BUTTON_TEXT, foundElement.getText());
    }
}
