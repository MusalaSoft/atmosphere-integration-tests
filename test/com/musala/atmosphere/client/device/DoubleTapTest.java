package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertIsDoubleTapped;
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
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

/**
 * 
 * @author delyan.dimitrov
 * 
 */
public class DoubleTapTest extends BaseIntegrationTest {

    private final static String DOUBLE_TAP_EDIT_TEXT = "DoubleTapValidator";

    private final static int APPLICATION_START_TIMEOUT = 1000;

    @BeforeClass
    public static void setUp() throws InterruptedException, UiElementFetchingException {
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
    public void testDoubleTap() throws InterruptedException, ActivityStartingException, UiElementFetchingException {
        // FIXME element becomes invalid every time the focus changes
        UiElement doubleTapValidator = getElementByContentDescriptor(DOUBLE_TAP_EDIT_TEXT);
        doubleTapValidator.clearText();

        doubleTapValidator = getElementByContentDescriptor(DOUBLE_TAP_EDIT_TEXT);
        boolean tapResult = doubleTapValidator.doubleTap();

        // assert that the method indicated success
        assertTrue("Tapping screen returned false.", tapResult);

        // assert that UI element has received a double tap gesture
        doubleTapValidator = getElementByContentDescriptor(DOUBLE_TAP_EDIT_TEXT);
        assertIsDoubleTapped("The element did not recieve a double tap gesture.", doubleTapValidator);
    }

    @Test
    public void testRelativeDoubleTap() throws InterruptedException, UiElementFetchingException {
        UiElement doubleTapValidator = getElementByContentDescriptor(DOUBLE_TAP_EDIT_TEXT);
        doubleTapValidator.clearText();

        // the tap point is relative to the element's coordinates
        doubleTapValidator = getElementByContentDescriptor(DOUBLE_TAP_EDIT_TEXT);
        Point tapPoint = new Point(0, 0);
        boolean tapResult = doubleTapValidator.doubleTap(tapPoint);

        // assert that the method indicated success
        assertTrue("Double tapping UI element returned false.", tapResult);

        // assert that UI element has received a double tap gesture
        doubleTapValidator = getElementByContentDescriptor(DOUBLE_TAP_EDIT_TEXT);
        assertIsDoubleTapped("The element did not recieve a double tap gesture.", doubleTapValidator);
    }

    @Test
    public void testDeviceDoubleTap() throws InterruptedException, UiElementFetchingException {
        UiElement doubleTapValidator = getElementByContentDescriptor(DOUBLE_TAP_EDIT_TEXT);
        doubleTapValidator.clearText();

        // getting the point at the center of the element
        doubleTapValidator = getElementByContentDescriptor(DOUBLE_TAP_EDIT_TEXT);
        UiElementSelector doubleTapValidatorSelector = doubleTapValidator.getElementSelector();
        Bounds elementBounds = doubleTapValidatorSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point centerPoint = elementBounds.getCenter();

        boolean tapResult = testDevice.doubleTap(centerPoint);

        // assert that the method indicates success
        assertTrue("Double tapping UI element returned false.", tapResult);

        // assert that UI element has received a double tap gesture
        doubleTapValidator = getElementByContentDescriptor(DOUBLE_TAP_EDIT_TEXT);
        assertIsDoubleTapped("The element did not recieve a double tap gesture.", doubleTapValidator);
    }
}
