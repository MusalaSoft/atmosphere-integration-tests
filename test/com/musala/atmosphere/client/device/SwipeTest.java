package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedDown;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedLeft;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedRight;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedUp;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setupAndStartGestureActivity;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.geometry.Bounds;
import com.musala.atmosphere.client.geometry.Point;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.beans.SwipeDirection;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class SwipeTest extends BaseIntegrationTest {

    @BeforeClass
    public static void setUp() throws UiElementFetchingException, InterruptedException, ActivityStartingException {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);

        setupAndStartGestureActivity();
    }

    @AfterClass
    public static void tearDown() throws UiElementFetchingException {
        releaseDevice();
    }

    @Test
    public void testDeviceSwipe() throws InterruptedException, ActivityStartingException, UiElementFetchingException {
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        swipeValidator.clearText();
        UiElementSelector swipeValidatorSelector = swipeValidator.getElementSelector();
        Bounds elementBounds = swipeValidatorSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point centerPoint = elementBounds.getCenter();

        SwipeDirection directionDown = SwipeDirection.UP;

        boolean swipeResult = testDevice.swipe(centerPoint, directionDown);

        // assert that the method indicates success
        assertTrue("Swipping returned false.", swipeResult);

        // assert that UI element has received a double tap gesture
        // FIXME is getting the element really needed?
        swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        assertSwipedUp("The element did not recieve a swipe gesture.");
    }

    @Test
    public void testDeviceSwipeLeft() throws InterruptedException, UiElementFetchingException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        swipeValidator.clearText();
        SwipeDirection directionLeft = SwipeDirection.LEFT;
        boolean swipeResult = swipeValidator.swipe(directionLeft);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        // assert that UI element has received a swipe gesture
        swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        assertSwipedLeft("The element did not recieve a swipe left gesture.");
    }

    @Test
    public void testDeviceSwipeRight() throws InterruptedException, UiElementFetchingException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        swipeValidator.clearText();
        SwipeDirection directionRight = SwipeDirection.RIGHT;
        boolean swipeResult = swipeValidator.swipe(directionRight);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        // assert that UI element has received a swipe gesture
        swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        assertSwipedRight("The element did not recieve a swipe right gesture.");
    }

    @Test
    public void testSwipeDown() throws InterruptedException, UiElementFetchingException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        swipeValidator.clearText();
        SwipeDirection directionDown = SwipeDirection.DOWN;
        boolean swipeResult = swipeValidator.swipe(directionDown);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        // assert that UI element has received a swipe gesture
        swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        assertSwipedDown("The element did not recieve a swipe down gesture.");
    }

    @Test
    public void testSwipeUp() throws InterruptedException, UiElementFetchingException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        swipeValidator.clearText();
        SwipeDirection directionUp = SwipeDirection.UP;
        boolean swipeResult = swipeValidator.swipe(directionUp);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        // assert that UI element has received a swipe gesture
        swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        assertSwipedUp("The element did not recieve a swipe up gesture.");

    }

}
