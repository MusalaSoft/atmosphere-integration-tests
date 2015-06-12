package com.musala.atmosphere.client.uielement;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWaitTestActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 *
 * @author yordan.petrov
 *
 */
public class UiElementRevalidationTest extends BaseIntegrationTest {

    private static final Integer ELEMENT_WAIT_TIMEOUT = 10000;

    private static final String CHANGING_TEXT_BUTTON_ORIGINAL_TEXT = "Text button";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        startWaitTestActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testRevalidatePermamentlyExistingElement() throws Exception {
        UiElementSelector selector = new UiElementSelector();
        selector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                       ContentDescriptor.CHANGING_TEXT_BUTTON_DESCRIPTOR.toString());
        Screen activeScreen = testDevice.getActiveScreen();
        boolean isPresent = activeScreen.waitForElementExists(selector, ELEMENT_WAIT_TIMEOUT);
        assertTrue("Wait for element existance returned false.", isPresent);

        UiElement changingTextButton = activeScreen.getElement(selector);
        assertTrue("Element revalidation returned false, but the element is present on the screen.",
                   changingTextButton.revalidate());
    }

    @Test
    public void testRevalidateTemporaryExistingElement() throws Exception {
        UiElementSelector selector = new UiElementSelector();
        selector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                       ContentDescriptor.CHANGING_TEXT_BUTTON_DESCRIPTOR.toString());
        Screen activeScreen = testDevice.getActiveScreen();
        boolean isPresent = activeScreen.waitForElementExists(selector, ELEMENT_WAIT_TIMEOUT);
        assertTrue("Wait for element existance returned false.", isPresent);

        UiElement changingTextButton = activeScreen.getElement(selector);
        assertTrue("Element revalidation returned false, but the element is present on the screen.",
                   changingTextButton.revalidate());

        changingTextButton.tap();
        selector.addSelectionAttribute(CssAttribute.TEXT, CHANGING_TEXT_BUTTON_ORIGINAL_TEXT);
        boolean isGone = activeScreen.waitUntilElementGone(selector, ELEMENT_WAIT_TIMEOUT);

        assertTrue("Wait until element gone returned false.", isGone);

        assertFalse("Element revalidation returned true, but the element is not present on the screen.",
                    changingTextButton.revalidate());
    }
}
