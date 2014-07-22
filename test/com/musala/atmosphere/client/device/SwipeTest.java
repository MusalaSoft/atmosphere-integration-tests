package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedDown;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedLeft;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedRight;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedUp;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startGestureActivity;
import static org.junit.Assert.assertTrue;

import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.Before;
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
import com.musala.atmosphere.commons.beans.SwipeDirection;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class SwipeTest extends BaseIntegrationTest {

    @BeforeClass
    public static void setUp()
        throws UiElementFetchingException,
            InterruptedException,
            ActivityStartingException,
            XPathExpressionException,
            InvalidCssQueryException {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);
        setTestDevice(testDevice);

        startGestureActivity();
    }

    @AfterClass
    public static void tearDown() throws UiElementFetchingException {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Before
    public void setUpTest() throws XPathExpressionException, UiElementFetchingException, InvalidCssQueryException {
        UiElement clearTextButton = getElementByContentDescriptor(ContentDescriptor.CLEAR_TEXT_BUTTON.toString());
        clearTextButton.tap();
    }

    @Test
    public void testDeviceSwipe()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        UiElementSelector swipeValidatorSelector = swipeValidator.getElementSelector();
        Bounds elementBounds = swipeValidatorSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point centerPoint = elementBounds.getCenter();

        SwipeDirection directionDown = SwipeDirection.UP;

        boolean swipeResult = testDevice.swipe(centerPoint, directionDown);

        // assert that the method indicates success
        assertTrue("Swipping returned false.", swipeResult);

        assertSwipedUp("The element did not recieve a swipe gesture.");
    }

    @Test
    public void testDeviceSwipeLeft()
        throws InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        SwipeDirection directionLeft = SwipeDirection.LEFT;
        boolean swipeResult = swipeValidator.swipe(directionLeft);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        assertSwipedLeft("The element did not recieve a swipe left gesture.");
    }

    @Test
    public void testDeviceSwipeRight()
        throws InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        SwipeDirection directionRight = SwipeDirection.RIGHT;
        boolean swipeResult = swipeValidator.swipe(directionRight);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        assertSwipedRight("The element did not recieve a swipe right gesture.");
    }

    @Test
    public void testSwipeDown()
        throws InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        SwipeDirection directionDown = SwipeDirection.DOWN;
        boolean swipeResult = swipeValidator.swipe(directionDown);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        assertSwipedDown("The element did not recieve a swipe down gesture.");
    }

    @Test
    public void testSwipeUp()
        throws InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        SwipeDirection directionUp = SwipeDirection.UP;
        boolean swipeResult = swipeValidator.swipe(directionUp);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        assertSwipedUp("The element did not recieve a swipe up gesture.");

    }

}
