package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInputTextBoxIsFocused;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.geometry.Bounds;
import com.musala.atmosphere.commons.geometry.Point;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author yordan.petrov
 * 
 */
public class TapTest extends BaseIntegrationTest {
    private final static String IME_RELATIVE_LAYOUT_CONTENT_DESCRIPTOR = "ImeRelativeLayout";

    private final static String TAPPING_SCREEN_FAILED_MESSAGE = "Tapping screen returned false.";

    private final static String FOCUSING_ELEMENT_FAILED_MESSAGE = "Element is not focused.";

    private final static long TIMEOUT = 1000;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);

        setTestDevice(testDevice);
    }

    @Before
    public void setUpTest() throws Exception {
        startImeTestActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @After
    public void tearDownTest() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @Test
    public void testTap() throws Exception {
        UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());

        assertTrue(TAPPING_SCREEN_FAILED_MESSAGE, inputTextBox.tap());

        assertInputTextBoxIsFocused(FOCUSING_ELEMENT_FAILED_MESSAGE);
    }

    @Test
    public void testRelativeTap() throws Exception {
        UiElement imeRelativeLayout = getElementByContentDescriptor(IME_RELATIVE_LAYOUT_CONTENT_DESCRIPTOR);
        UiElementSelector imeRelativeLayoutSelector = imeRelativeLayout.getElementSelector();

        UiElement emptyTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        UiElementSelector emptyTextBoxSelector = emptyTextBox.getElementSelector();

        Bounds emptyTextBoxBoundsAttributeValue = emptyTextBoxSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point emptyTextBoxUpperLeftCorner = emptyTextBoxBoundsAttributeValue.getUpperLeftCorner();

        Bounds imeRelativeLayoutBoundsAttributeValue = imeRelativeLayoutSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point imeRelativeLayoutUpperLeftCorner = imeRelativeLayoutBoundsAttributeValue.getRelativePoint(emptyTextBoxUpperLeftCorner);

        assertTrue(TAPPING_SCREEN_FAILED_MESSAGE, imeRelativeLayout.tap(imeRelativeLayoutUpperLeftCorner));

        Thread.sleep(TIMEOUT);

        assertInputTextBoxIsFocused(FOCUSING_ELEMENT_FAILED_MESSAGE);
    }
}