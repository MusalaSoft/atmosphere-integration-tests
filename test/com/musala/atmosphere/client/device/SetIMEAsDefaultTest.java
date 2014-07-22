package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

/**
 * 
 * @author denis.bialev
 * 
 */
public class SetIMEAsDefaultTest extends BaseIntegrationTest {

    private final static String TEXT_BOX_CONTENT_DESCRIPTOR = "InputTextBox";

    private final static String INPUT_STRING = "Letters";

    private final static String DEFAULT_KEYBOARD_ID = "com.android.inputmethod.latin/.LatinIME";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);
        setTestDevice(testDevice);
        startMainActivity();
    }

    @AfterClass
    public static void tearDown() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testSettingDefaultKeyboards() throws Exception {
        Screen screen = testDevice.getActiveScreen();

        UiElement inputTextBox = getElementByContentDescriptor(TEXT_BOX_CONTENT_DESCRIPTOR);

        testDevice.setDefaultIME(DEFAULT_KEYBOARD_ID);
        inputTextBox.inputText(INPUT_STRING);

        screen.updateScreen();

        UiElement inputTextBoxAfterInput = getElementByContentDescriptor(TEXT_BOX_CONTENT_DESCRIPTOR);
        String resultFromInput = inputTextBoxAfterInput.getText();

        assertFalse("There should be no text in the field.", INPUT_STRING.equals(resultFromInput));

        screen.updateScreen();

        UiElement inputTextBoxAtmosphereIME = getElementByContentDescriptor(TEXT_BOX_CONTENT_DESCRIPTOR);

        testDevice.setAtmosphereIME();
        inputTextBoxAtmosphereIME.inputText(INPUT_STRING);

        screen.updateScreen();
        UiElement inputTextBoxAtmosphereIMEAfterInput = getElementByContentDescriptor(TEXT_BOX_CONTENT_DESCRIPTOR);
        String resultWithAtmosphereIME = inputTextBoxAtmosphereIMEAfterInput.getText();

        assertEquals("The entered text is not as expected.", INPUT_STRING, resultWithAtmosphereIME);
    }
}
