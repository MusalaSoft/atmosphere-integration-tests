package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInputTextBoxIsFocused;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.geometry.Bounds;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.gesture.Anchor;
import com.musala.atmosphere.commons.gesture.Gesture;
import com.musala.atmosphere.commons.gesture.Timeline;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * Modified by georgi.gaydarov, originally by
 * 
 * @author yordan.petrov
 * 
 */
public class GestureExecutionTest extends BaseIntegrationTest {
    private static final long TIMEOUT = 1000;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);

        setTestDevice(testDevice);

        startImeTestActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testTap() throws Exception {
        UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        UiElementSelector selector = inputTextBox.getElementSelector();

        Bounds boxBounds = selector.getBoundsValue(CssAttribute.BOUNDS);
        int xCoord = boxBounds.getCenter().getX();
        int yCoord = boxBounds.getCenter().getY();

        Anchor tapPoint = new Anchor(xCoord, yCoord, 0);

        Timeline tapTimeline = new Timeline();
        tapTimeline.add(tapPoint);

        Gesture tapGesture = new Gesture();
        tapGesture.add(tapTimeline);

        testDevice.executeGesture(tapGesture);

        Thread.sleep(TIMEOUT);

        assertInputTextBoxIsFocused("Input text box not focused.");
    }
}