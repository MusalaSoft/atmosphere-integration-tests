package com.musala.atmosphere.client.screen;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWaitTestActivity;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

/**
 * 
 * @author delyan.dimitrov
 * 
 */
public class GetElementWhenPresentTest extends BaseIntegrationTest {
    private static final String NON_EXISTING_ELEMENT_TEXT = "I do not exist!";

    private static final String TEST_BUTTON_CHANGED_TEXT = "Changed text button";

    private static final String TEST_BUTTON_INITIAL_TEXT = "Text button";

    private static final String TEST_BUTTON_INITIAL_TEXT_CSS = String.format("[text=%s]", TEST_BUTTON_INITIAL_TEXT);

    private static final String NULL_VALIDATION_ELEMENT_MESSAGE = "Fetching the validation element returned null.";

    private static final int LONG_WAIT_TIMEOUT = 10000;

    private static final int SHORT_WAIT_TIMEOUT = 1000;

    private static Screen deviceScreen;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        deviceScreen = testDevice.getActiveScreen();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Before
    public void setUpTest() throws Exception {
        startWaitTestActivity();
        deviceScreen.updateScreen();
    }

    @After
    public void tearDownTest() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @Test
    public void testGettingAppearingElement() throws Exception {
        UiElement testButton = deviceScreen.getElementByCSS(TEST_BUTTON_INITIAL_TEXT_CSS);
        testButton.tap();

        UiElementSelector changedButtonSelector = new UiElementSelector();
        changedButtonSelector.addSelectionAttribute(CssAttribute.TEXT, TEST_BUTTON_CHANGED_TEXT);

        UiElement element = deviceScreen.getElementWhenPresent(changedButtonSelector, LONG_WAIT_TIMEOUT);
        assertNotNull(NULL_VALIDATION_ELEMENT_MESSAGE, element);
    }

    @Test
    public void testGettingExistingElement() throws Exception {
        UiElementSelector testButtonSelector = new UiElementSelector();
        testButtonSelector.addSelectionAttribute(CssAttribute.TEXT, TEST_BUTTON_INITIAL_TEXT);
        UiElement testButton = deviceScreen.getElementWhenPresent(testButtonSelector);

        assertNotNull(NULL_VALIDATION_ELEMENT_MESSAGE, testButton);
    }

    @Test(expected = UiElementFetchingException.class)
    public void testGettingUnexistingElement() throws Exception {
        UiElementSelector unexistingElementSelector = new UiElementSelector();
        unexistingElementSelector.addSelectionAttribute(CssAttribute.TEXT, NON_EXISTING_ELEMENT_TEXT);

        deviceScreen.getElementWhenPresent(unexistingElementSelector, SHORT_WAIT_TIMEOUT);
    }
}
