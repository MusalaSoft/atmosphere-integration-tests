package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertDoubleTapped;
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
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author delyan.dimitrov
 * 
 */
public class DoubleTapTest extends BaseIntegrationTest {

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

    @Before
    public void setUpTest() throws XPathExpressionException, UiElementFetchingException, InvalidCssQueryException {
        UiElement clearTextButton = getElementByContentDescriptor(ContentDescriptor.CLEAR_TEXT_BUTTON.toString());
        clearTextButton.tap();
    }

    @Test
    public void testDeviceDoubleTap()
        throws InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        UiElement doubleTapValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());

        // getting the point at the center of the element
        doubleTapValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        UiElementSelector doubleTapValidatorSelector = doubleTapValidator.getElementSelector();
        Bounds elementBounds = doubleTapValidatorSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point centerPoint = elementBounds.getCenter();

        boolean tapResult = testDevice.doubleTap(centerPoint);

        // assert that the method indicates success
        assertTrue("Double tapping UI element returned false.", tapResult);

        // assert that UI element has received a double tap gesture
        doubleTapValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        assertDoubleTapped("The element did not recieve a double tap gesture.");
    }

    @Test
    public void testDoubleTap()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        // FIXME element becomes invalid every time the focus changes
        UiElement doubleTapValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());

        doubleTapValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        boolean tapResult = doubleTapValidator.doubleTap();

        // assert that the method indicated success
        assertTrue("Tapping screen returned false.", tapResult);

        // assert that UI element has received a double tap gesture
        doubleTapValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        assertDoubleTapped("The element did not recieve a double tap gesture.");
    }

    @Test
    public void testRelativeDoubleTap()
        throws InterruptedException,
            UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        UiElement doubleTapValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());

        // the tap point is relative to the element's coordinates
        doubleTapValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        Point tapPoint = new Point(0, 0);
        boolean tapResult = doubleTapValidator.doubleTap(tapPoint);

        // assert that the method indicated success
        assertTrue("Double tapping UI element returned false.", tapResult);

        // assert that UI element has received a double tap gesture
        doubleTapValidator = getElementByContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR.toString());
        assertDoubleTapped("The element did not recieve a double tap gesture.");
    }
}
