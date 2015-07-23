package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startToastMessageActivity;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

/**
 *
 * @author yordan.petrov
 *
 */
public class GetLastToastTest extends BaseIntegrationTest {

    private static final String LONG_TOAST_TEXT = "I am a really long toast.";

    private static final String SHORT_TOAST_TEXT = "I am a really short toast.";

    private static final String LONG_TOAST_BUTTON_DESCRIPTOR = "ShowLongToastButton";

    private static final String SHORT_TOAST_BUTTON_DESCRIPTOR = "ShowShortToastButton";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        startToastMessageActivity();
    }

    @Test
    public void testGetShortToastMessage() throws Exception {
        UiElement shortToastButton = getElementByContentDescriptor(SHORT_TOAST_BUTTON_DESCRIPTOR);
        shortToastButton.tap();

        assertEquals("The last toast message text did not match the expected value.",
                     SHORT_TOAST_TEXT,
                     testDevice.getLastToast());
    }

    @Test
    public void testGetLongToastMessage() throws Exception {
        UiElement longToastButton = getElementByContentDescriptor(LONG_TOAST_BUTTON_DESCRIPTOR);
        longToastButton.tap();

        assertEquals("The last toast message text did not match the expected value.",
                     LONG_TOAST_TEXT,
                     testDevice.getLastToast());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }
}
