package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedDown;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedLeft;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedRight;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertSwipedUp;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startGestureActivity;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.beans.SwipeDirection;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.geometry.Bounds;
import com.musala.atmosphere.commons.geometry.Point;
import com.musala.atmosphere.commons.ui.UiElementPropertiesContainer;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class SwipeTest extends BaseIntegrationTest {

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        startGestureActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Before
    public void setUpTest() throws Exception {
        UiElement clearTextButton = getElementByContentDescriptor(ContentDescriptor.CLEAR_TEXT_BUTTON.toString());
        clearTextButton.tap();
    }

    @Test
    public void testDeviceSwipe() throws Exception {
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        UiElementPropertiesContainer swipeValidatorProperties = swipeValidator.getProperties();
        Bounds elementBounds = swipeValidatorProperties.getBounds();
        Point centerPoint = elementBounds.getCenter();

        SwipeDirection directionDown = SwipeDirection.UP;

        boolean swipeResult = testDevice.swipe(centerPoint, directionDown);

        // assert that the method indicates success
        assertTrue("Swipping returned false.", swipeResult);

        assertSwipedUp("The element did not recieve a swipe gesture.");
    }

    @Test
    public void testDeviceSwipeLeft() throws Exception {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        SwipeDirection directionLeft = SwipeDirection.LEFT;
        boolean swipeResult = swipeValidator.swipe(directionLeft);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        assertSwipedLeft("The element did not recieve a swipe left gesture.");
    }

    @Test
    public void testDeviceSwipeRight() throws Exception {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        SwipeDirection directionRight = SwipeDirection.RIGHT;
        boolean swipeResult = swipeValidator.swipe(directionRight);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        assertSwipedRight("The element did not recieve a swipe right gesture.");
    }

    @Test
    public void testSwipeDown() throws Exception {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        SwipeDirection directionDown = SwipeDirection.DOWN;
        boolean swipeResult = swipeValidator.swipe(directionDown);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        assertSwipedDown("The element did not recieve a swipe down gesture.");
    }

    @Test
    public void testSwipeUp() throws Exception {
        // FIXME element becomes invalid every time the focus changes
        UiElement swipeValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        SwipeDirection directionUp = SwipeDirection.UP;
        boolean swipeResult = swipeValidator.swipe(directionUp);

        // assert that the method indicated success
        assertTrue("Swipe returned false.", swipeResult);

        assertSwipedUp("The element did not recieve a swipe up gesture.");

    }

}
