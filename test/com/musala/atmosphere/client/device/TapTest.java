package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInputTextBoxIsFocused;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertTrue;

import org.junit.After;
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
 * @author yordan.petrov
 * 
 */
public class TapTest extends BaseIntegrationTest {
    private final static String WIDGET_MAIN_LAYOUT = "MainLinearLayout";

    private final static String INPUT_TEXT_BOX = "InputTextBox";

    @BeforeClass
    public static void setUp() {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @After
    public void stopValidator() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @Test
    public void testTap() throws InterruptedException, ActivityStartingException, UiElementFetchingException {
        startMainActivity();

        UiElement inputTextBox = getElementByContentDescriptor(INPUT_TEXT_BOX);
        assertTrue("Tapping screen returned false.", inputTextBox.tap());
        assertInputTextBoxIsFocused("Input text box not focused.");
    }

    @Test
    public void testRelativeTap() throws InterruptedException, ActivityStartingException, UiElementFetchingException {
        startMainActivity();

        UiElement widgetMainLayout = getElementByContentDescriptor(WIDGET_MAIN_LAYOUT);
        UiElementSelector widgetRelativeLayoutSelector = widgetMainLayout.getElementSelector();
        UiElement batteryStatusBox = getElementByContentDescriptor(INPUT_TEXT_BOX);
        UiElementSelector batteryStatusBoxSelector = batteryStatusBox.getElementSelector();

        Bounds batteryBoundsAttributeValue = batteryStatusBoxSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point batteryStatusBoxUpperLeftCorner = batteryBoundsAttributeValue.getUpperLeftCorner();
        Bounds widgetBoundsAttributeValue = widgetRelativeLayoutSelector.getBoundsValue(CssAttribute.BOUNDS);
        Point BatteryStatusRelativeUpperLeftCorner = widgetBoundsAttributeValue.getRelativePoint(batteryStatusBoxUpperLeftCorner);

        assertTrue("Tapping screen returned false.", widgetMainLayout.tap(BatteryStatusRelativeUpperLeftCorner));
        Thread.sleep(1000);
        assertInputTextBoxIsFocused("Input text box not focused.");
    }
}