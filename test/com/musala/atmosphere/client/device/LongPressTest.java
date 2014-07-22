package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertLongClicked;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startGestureActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.xpath.XPathExpressionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.geometry.Bounds;
import com.musala.atmosphere.client.geometry.Point;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class LongPressTest extends BaseIntegrationTest {

    @Before
    public void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);

        setTestDevice(testDevice);

        startGestureActivity();
    }

    @Before
    public void setUpTest() throws XPathExpressionException, UiElementFetchingException, InvalidCssQueryException {
        UiElement clearTextButton = getElementByContentDescriptor(ContentDescriptor.CLEAR_TEXT_BUTTON.toString());
        clearTextButton.tap();
    }

    @Test
    public void testValidLongPress()
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        UiElement longPressTextField = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        boolean longPressResult = longPressTextField.longPress();
        assertTrue("Long press returned true.", longPressResult);
        assertLongClicked("Text box for gesture verification not long pressed.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailedLongPress()
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {

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
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }
}
