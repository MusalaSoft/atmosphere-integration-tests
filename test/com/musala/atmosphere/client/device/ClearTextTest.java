package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertTextIsCleared;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class ClearTextTest extends BaseIntegrationTest {
    public static final String FAIL_MESSAGE = "The text has not been cleared.";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Before
    public void setUpTest() throws Exception {
        startImeTestActivity();
    }

    @After
    public void tearDownTest() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @Test
    public void testClearText() throws Exception {
        UiElement clearTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());

        clearTextBox.clearText();

        assertTextIsCleared(FAIL_MESSAGE, ContentDescriptor.CONTENT_TEXT_BOX);
    }

    @Test
    public void testClearTextLeadingEmptyLine() throws Exception {
        UiElement clearTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_FIRST_LINE_TEXT_BOX.toString());

        clearTextBox.clearText();

        assertTextIsCleared(FAIL_MESSAGE, ContentDescriptor.EMPTY_FIRST_LINE_TEXT_BOX);
    }
}
