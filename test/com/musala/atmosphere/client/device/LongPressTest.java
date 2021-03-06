package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertLongClicked;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startGestureActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.musala.atmosphere.commons.geometry.Bounds;
import com.musala.atmosphere.commons.geometry.Point;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class LongPressTest extends BaseIntegrationTest {
    private static final int LONG_PRESS_TIMEOUT = 2000;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);

        setTestDevice(testDevice);

        startGestureActivity();
    }

    @Before
    public void setUpTest() throws Exception {
        UiElementSelector clearTextButtonSelector = new UiElementSelector();
        clearTextButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                      ContentDescriptor.CLEAR_TEXT_BUTTON.toString());
        Screen deviceScreen = testDevice.getActiveScreen();
        UiElement clearTextButton = deviceScreen.getElementWhenPresent(clearTextButtonSelector);

        clearTextButton.tap();
    }

    @Test
    public void testValidLongPress() throws Exception {
        UiElement longPressTextField = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        boolean longPressResult = longPressTextField.longPress();
        assertTrue("Long press indicated failure.", longPressResult);
        assertLongClicked("Text box for gesture verification did not receive a long press gesture.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailedLongPress() throws Exception {

        UiElement longPressTextField = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());

        // make sure to click outside the element
        final Bounds elementBounds = longPressTextField.getProperties().getBounds();
        Point lowerRightCorner = elementBounds.getLowerRightCorner();
        final int x = lowerRightCorner.getX();
        final int y = lowerRightCorner.getY();
        final int offset = 1;
        Point outerPoint = new Point(x + offset, y + offset);

        // long press
        boolean longPressResult = longPressTextField.longPress(outerPoint, LONG_PRESS_TIMEOUT);
        assertFalse("Text box for gesture verification was long pressed.", longPressResult);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }
}
