package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author denis.bialev
 * 
 */
public class SetIMEAsDefaultTest extends BaseIntegrationTest {
    private final static String INPUT_STRING = "Letters";

    private final static String DEFAULT_KEYBOARD_ID = "com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME";

    private final static String ATMOSPHERE_IME_PACKAGE = "com.musala.atmosphere.ime";

    private static final int WAIT_FOR_SETTIING_IME = 2000;

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
    public void testSettingDefaultKeyboards() throws Exception {
        Screen screen = testDevice.getActiveScreen();

        UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());

        testDevice.setDefaultIME(DEFAULT_KEYBOARD_ID);

        // To ensure the broadcast receiver of the ATMOSPHERE IME has fully stopped.
        testDevice.forceStopProcess(ATMOSPHERE_IME_PACKAGE);

        inputTextBox.inputText(INPUT_STRING);

        screen.updateScreen();

        UiElement inputTextBoxAfterInput = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        String resultFromInput = inputTextBoxAfterInput.getText();

        assertFalse("There should be no text in the field.", INPUT_STRING.equals(resultFromInput));

        screen.updateScreen();

        UiElement inputTextBoxAtmosphereIME = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());

        testDevice.setAtmosphereIME();
        Thread.sleep(WAIT_FOR_SETTIING_IME);
        inputTextBoxAtmosphereIME.inputText(INPUT_STRING);

        screen.updateScreen();
        UiElement inputTextBoxAtmosphereIMEAfterInput = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        String resultWithAtmosphereIME = inputTextBoxAtmosphereIMEAfterInput.getText();

        assertEquals("The entered text is not as expected.", INPUT_STRING, resultWithAtmosphereIME);
    }
}
