package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInputText;
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

/**
 * @author valyo.yolovski
 */
public class InputTextTest extends BaseIntegrationTest {
    private static final long INPUT_INTERVAL = 500;

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
    public void stopActivity() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @Test
    public void testInputTextCyrillic() throws Exception {
        String textToInput = "Hi! Кирилица. €%@$§№%()456*/0,.";

        UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        inputTextBox.inputText(textToInput);

        assertInputText("Inputting text failed.", textToInput);
    }

    @Test
    public void testInputTextLatin() throws Exception {
        String textToInput = "Letters.";

        UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        inputTextBox.inputText(textToInput, INPUT_INTERVAL);

        assertInputText("Inputting text failed.", textToInput);
    }
}
