package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedDown;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedLeft;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedRight;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedUp;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
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

public class SwipeTest extends BaseIntegrationTest {

    private final static String SWIPE_EDIT_TEXT = "GestureValidator";

    // TODO Move this constant to the BaseIntegrationTest class
    private final static int APPLICATION_START_TIMEOUT = 3000;

    @BeforeClass
    public static void setUp() throws UiElementFetchingException, InterruptedException {
        initTestDevice(new DeviceParameters());
        installValidatorApplication();
        testDevice.startApplication(VALIDATOR_APP_PACKAGE);

        Thread.sleep(APPLICATION_START_TIMEOUT);
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void testDeviceSwipe() throws InterruptedException, ActivityStartingException, UiElementFetchingException {
        UiElement swipeValidator = getElementByContentDescriptor(SWIPE_EDIT_TEXT);
        swipeValidator.clearText();
        UiElementSelector swipeValidatorSelector = swipeValidator.getElementSelector();
        Bounds elementBounds = swipeValidatorSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point centerPoint = elementBounds.getCenter();

        SwipeDirection directionDown = SwipeDirection.UP;

        boolean swipeResult = testDevice.swipe(centerPoint, directionDown);

        // assert that the method indicates success
        assertTrue("Swipping returned false.", swipeResult);

        // assert that UI element has received a double tap gesture
        swipeValidator = getElementByContentDescriptor(SWIPE_EDIT_TEXT);
        assertSwipedUp("The element did not recieve a swipe gesture.");
    }

    @Test
    public void testSwipeUp() throws InterruptedException, UiElementFetchingException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(SWIPE_EDIT_TEXT);
        swipeValidator.clearText();
        SwipeDirection directionUp = SwipeDirection.UP;
        boolean swipeResult = swipeValidator.swipe(directionUp);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        // assert that UI element has received a swipe gesture
        swipeValidator = getElementByContentDescriptor(SWIPE_EDIT_TEXT);
        assertSwipedUp("The element did not recieve a swipe up gesture.");

    }

    @Test
    public void testSwipeDown() throws InterruptedException, UiElementFetchingException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(SWIPE_EDIT_TEXT);
        swipeValidator.clearText();
        SwipeDirection directionDown = SwipeDirection.DOWN;
        boolean swipeResult = swipeValidator.swipe(directionDown);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        // assert that UI element has received a swipe gesture
        swipeValidator = getElementByContentDescriptor(SWIPE_EDIT_TEXT);
        assertSwipedDown("The element did not recieve a swipe down gesture.");
    }

    @Test
    public void testDeviceSwipeRight() throws InterruptedException, UiElementFetchingException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(SWIPE_EDIT_TEXT);
        swipeValidator.clearText();
        SwipeDirection directionRight = SwipeDirection.RIGHT;
        boolean swipeResult = swipeValidator.swipe(directionRight);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        // assert that UI element has received a swipe gesture
        swipeValidator = getElementByContentDescriptor(SWIPE_EDIT_TEXT);
        assertSwipedRight("The element did not recieve a swipe right gesture.");
    }

    @Test
    public void testDeviceSwipeLeft() throws InterruptedException, UiElementFetchingException {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(SWIPE_EDIT_TEXT);
        swipeValidator.clearText();
        SwipeDirection directionLeft = SwipeDirection.LEFT;
        boolean swipeResult = swipeValidator.swipe(directionLeft);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        // assert that UI element has received a swipe gesture
        swipeValidator = getElementByContentDescriptor(SWIPE_EDIT_TEXT);
        assertSwipedLeft("The element did not recieve a swipe left gesture.");
    }

}
