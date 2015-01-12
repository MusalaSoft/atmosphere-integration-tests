package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertLongClicked;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startGestureActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.geometry.Bounds;
import com.musala.atmosphere.client.geometry.Point;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class LongPressTest extends BaseIntegrationTest {
    private static final int LONG_PRESS_TIMEOUT = 2000;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);

        setTestDevice(testDevice);

        startGestureActivity();
    }

    @Before
    public void setUpTest() throws XPathExpressionException, UiElementFetchingException, InvalidCssQueryException {
        UiElementSelector clearTextButtonSelector = new UiElementSelector();
        clearTextButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                      ContentDescriptor.CLEAR_TEXT_BUTTON.toString());
        Screen deviceScreen = testDevice.getActiveScreen();
        UiElement clearTextButton = deviceScreen.getElementWhenPresent(clearTextButtonSelector);

        clearTextButton.tap();
    }

    @Test
    public void testValidLongPress()
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        UiElement longPressTextField = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        boolean longPressResult = longPressTextField.longPress();
        assertTrue("Long press indicated failure.", longPressResult);
        assertLongClicked("Text box for gesture verification did not receive a long press gesture.");
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
        boolean longPressResult = longPressTextField.longPress(outerPoint, LONG_PRESS_TIMEOUT);
        assertFalse("Text box for gesture verification was long pressed.", longPressResult);
    }

    @AfterClass
    public static void tearDown() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }
}
