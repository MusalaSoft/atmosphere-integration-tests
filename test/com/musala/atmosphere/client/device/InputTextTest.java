package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInputText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

/**
 * @author valyo.yolovski
 */
public class InputTextTest extends BaseIntegrationTest {
    private final static String INPUT_TEXT_BOX = "InputTextBox";

    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);
    }

    @Test
    public void inputTextTestOne() throws Exception {
        startMainActivity();
        String textToInput = "Hi! Кирилица. €%@$§№%()456*/0,.";
        UiElement inputTextBox = getElementByContentDescriptor(INPUT_TEXT_BOX);
        inputTextBox.inputText(textToInput);
        assertInputText("Inputting text failed.", textToInput);
    }

    @Test
    public void inputTextTestTwo() throws Exception {
        testDevice.pressButton(HardwareButton.HOME);
        startMainActivity();
        String textToInput = "Letters."; // Text to input.
        int inputInterval = 500;// Time interval between the input of each letter in ms.
        UiElement inputTextBox = getElementByContentDescriptor(INPUT_TEXT_BOX);
        inputTextBox.inputText(textToInput, inputInterval);
        assertInputText("Inputting text failed.", textToInput);
    }

    @After
    public void stopActivity() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }
}
