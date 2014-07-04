package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertPinchedIn;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertPinchedOut;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startGestureActivity;
import static org.junit.Assert.assertTrue;

import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.geometry.Bounds;
import com.musala.atmosphere.client.geometry.Point;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.DeviceInformation;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.util.Pair;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author delyan.dimitrov
 * 
 */
public class PinchTest extends BaseIntegrationTest {
    // FIXME element becomes invalid every time the focus changes in all tests

    @BeforeClass
    public static void setUp()
        throws InterruptedException,
            UiElementFetchingException,
            ActivityStartingException,
            XPathExpressionException,
            InvalidCssQueryException {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);

        startGestureActivity();
    }

    @AfterClass
    public static void tearDown() throws UiElementFetchingException {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testDevicePinchIn()
        throws InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        UiElement gestureValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        gestureValidator.clearText();

        // getting the point at the center of the element
        gestureValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        UiElementSelector gestureValidatorSelector = gestureValidator.getElementSelector();

        Bounds elementBounds = gestureValidatorSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point firstFingerInitial = elementBounds.getUpperLeftCorner();
        Point secondFingerInitial = elementBounds.getLowerRightCorner();

        boolean pinchInResult = testDevice.pinchIn(firstFingerInitial, secondFingerInitial);

        // assert that the method indicates success
        assertTrue("Pinching in UiElement returned false.", pinchInResult);

        // assert that UI element has received a pinch in gesture
        assertPinchedIn("The element did not recieve a pinch in gesture.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDevicePinchInInvalidPoint() {
        DeviceInformation deviceInfo = testDevice.getInformation();
        Pair<Integer, Integer> resolution = deviceInfo.getResolution();
        Point testPoint = new Point(resolution.getKey() + 1, resolution.getValue() + 1);

        testDevice.pinchIn(testPoint, testPoint);
    }

    @Test
    public void testDevicePinchOut()
        throws InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        UiElement gestureValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        gestureValidator.clearText();

        // getting the point at the center of the element
        gestureValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        UiElementSelector gestureValidatorSelector = gestureValidator.getElementSelector();

        Bounds elementBounds = gestureValidatorSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point firstFingerEnd = elementBounds.getUpperLeftCorner();
        Point secondFingerEnd = elementBounds.getLowerRightCorner();

        boolean pinchOutResult = testDevice.pinchOut(firstFingerEnd, secondFingerEnd);

        // assert that the method indicates success
        assertTrue("Pinching out UiElement returned false.", pinchOutResult);

        // assert that UI element has received a pinch out gesture
        assertPinchedOut("The element did not recieve a pinch out gesture.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDevicePinchOutInvalidPoint() {
        DeviceInformation deviceInfo = testDevice.getInformation();
        Pair<Integer, Integer> resolution = deviceInfo.getResolution();
        Point testPoint = new Point(resolution.getKey() + 1, resolution.getValue() + 1);

        testDevice.pinchOut(testPoint, testPoint);
    }

    @Test
    public void testPinchIn()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        UiElement gestureValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        gestureValidator.clearText();

        gestureValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        boolean pinchInResult = gestureValidator.pinchIn();

        // assert that the method indicated success
        assertTrue("Pinching in UiElement returned false.", pinchInResult);

        // assert that UI element has received a pinch in gesture
        assertPinchedIn("The element did not recieve a pinch in gesture.");
    }

    @Test
    public void testPinchOut()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        UiElement gestureValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        gestureValidator.clearText();

        gestureValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        boolean pinchOutResult = gestureValidator.pinchOut();

        // assert that the method indicated success
        assertTrue("Pinching out UiElement returned false.", pinchOutResult);

        // assert that UI element has received a pinch out gesture
        assertPinchedOut("The element did not recieve a pinch out gesture.");
    }
}
