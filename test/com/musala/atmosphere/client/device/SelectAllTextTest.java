package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertElementText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author yavor.stankov
 * 
 */
public class SelectAllTextTest extends BaseIntegrationTest {
    private final static String TEXT_TO_INPUT_FOR_VERIFICATION = "success";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        startImeTestActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testSelectAllTextSingleLine() throws Exception {

        UiElement selectTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());

        selectTextBox.selectAllText();

        selectTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());

        selectTextBox.inputText(TEXT_TO_INPUT_FOR_VERIFICATION);

        selectTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());

        assertElementText("Select all text failed! The text field content does not match the expected one.",
                          selectTextBox,
                          TEXT_TO_INPUT_FOR_VERIFICATION);
    }

    @Test
    public void testSelectAllTextMultiline() throws Exception {
        UiElement selectTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_FIRST_LINE_TEXT_BOX.toString());

        selectTextBox.selectAllText();

        selectTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_FIRST_LINE_TEXT_BOX.toString());

        selectTextBox.inputText(TEXT_TO_INPUT_FOR_VERIFICATION);

        selectTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_FIRST_LINE_TEXT_BOX.toString());

        assertElementText("Select all text failed! The text field content does not match the expected one.",
                          selectTextBox,
                          TEXT_TO_INPUT_FOR_VERIFICATION);
    }
}
