package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startUiElementsActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.AccessibilityUiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

/**
 * 
 * @author denis.bialev
 *
 */
public class GetAccessibilityUiElementsTest extends BaseIntegrationTest {
    // TODO Find what invisible elements represent in UiAutomator and add tests for them

    private static final boolean VISIBLE_ELEMENTS_ONLY = true;

    private static final boolean ALL_PRESENT_ELEMENTS = false;

    private static final String BUTTON_CLASS_NAME = "android.widget.Button";

    private static final String NON_EXISTENT_TEXT = "I don't exist";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder deviceSelectorBuilder = new DeviceSelectorBuilder();
        DeviceSelector deviceSelector = deviceSelectorBuilder.deviceType(DeviceType.DEVICE_PREFERRED).build();
        initTestDevice(deviceSelector);
        setTestDevice(testDevice);

        startUiElementsActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);

        releaseDevice();
    }

    @Test
    public void testFindListOfVisibleUiElements() {
        int expectedNumberOfFoundElements = 3;
        UiElementSelector elementSelector = new UiElementSelector();
        elementSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, BUTTON_CLASS_NAME);

        List<AccessibilityUiElement> foundElements = testDevice.getAccessibilityUiElements(elementSelector,
                                                                                           VISIBLE_ELEMENTS_ONLY);
        assertEquals("The found elements count is different from the expected.",
                     expectedNumberOfFoundElements,
                     foundElements.size());

        for (AccessibilityUiElement element : foundElements) {
            assertEquals("The found element has different field values from the selector.",
                         BUTTON_CLASS_NAME,
                         element.getProperties().getClassName());
        }
    }

    @Test
    public void testFindNonExistentElements() {
        UiElementSelector elementSelector = new UiElementSelector();
        elementSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, NON_EXISTENT_TEXT);

        List<AccessibilityUiElement> foundElements = testDevice.getAccessibilityUiElements(elementSelector,
                                                                                           ALL_PRESENT_ELEMENTS);

        assertTrue("The found elements count is different from the expected.", foundElements.isEmpty());
    }
}
