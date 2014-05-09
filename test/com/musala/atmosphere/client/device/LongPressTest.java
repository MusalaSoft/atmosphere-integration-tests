package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertLongClicked;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.geometry.Bounds;
import com.musala.atmosphere.client.geometry.Point;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;
import com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert;

public class LongPressTest extends BaseIntegrationTest {

    @Before
    public void setUp() throws Exception {
        initTestDevice(new DeviceParameters());
        installValidatorApplication();
        OnDeviceValidatorAssert.startGestureActivity();
    }

    @Test
    public void testValidLongPress() throws UiElementFetchingException {
        UiElement longPressTextField = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        boolean longPressResult = longPressTextField.longPress();
        assertTrue("Long press returned true.", longPressResult);
        assertLongClicked("Text box for gesture verification not long pressed.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailedLongPress() throws UiElementFetchingException {

        UiElement longPressTextField = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());

        // make sure to click outside the element
        final Bounds elementBounds = longPressTextField.getElementSelector().getBoundsValue(CssAttribute.BOUNDS);
        Point lowerRightCorner = elementBounds.getLowerRightCorner();
        final int x = lowerRightCorner.getX();
        final int y = lowerRightCorner.getY();
        final int offset = 1;
        Point outerPoint = new Point(x + offset, y + offset);

        // long press
        final int timeout = 2000; // 2s
        boolean longPressResult = longPressTextField.longPress(outerPoint, timeout);
        assertFalse("Text box for gesture verification was long pressed.", longPressResult);
    }

    @After
    public void tearDown() {
        releaseDevice();
    }
}
