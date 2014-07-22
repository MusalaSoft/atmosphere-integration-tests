package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startPickerActivity;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.TimePicker;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class TimePickerInteractionTest extends BaseIntegrationTest {
    private static final int EXPECTED_HOUR = 11;

    private static final int EXPECTED_MINUTE = 45;

    private static final int EXPECTED_MERIDIEM = Calendar.AM;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);
        setTestDevice(testDevice);
        startPickerActivity();
    }

    @AfterClass
    public static void tearDown() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @After
    public void tearDownTest() {
        testDevice.pressButton(HardwareButton.BACK);
    }

    @Test
    public void testGetCurrentTimeFromPicker() throws Exception {
        UiElement timePickerButton = getElementByContentDescriptor(ContentDescriptor.TIME_PICKER.toString());
        timePickerButton.tap();

        Screen screen = testDevice.getActiveScreen();
        TimePicker timePicker = screen.getTimePicker();
        Calendar calendar = timePicker.getValue();
        assertEquals("Hour from time picker does not match expected result.",
                     calendar.get(Calendar.HOUR),
                     EXPECTED_HOUR);
        assertEquals("Minute from time picker does not match expected result.",
                     calendar.get(Calendar.MINUTE),
                     EXPECTED_MINUTE);
        assertEquals("Meridiem from time picker does not match expected result.",
                     calendar.get(Calendar.AM_PM),
                     EXPECTED_MERIDIEM);
    }

    @Test
    public void testGetCurrentTimeFromPicker24HoursFormat() throws Exception {
        UiElement timePickerButton = getElementByContentDescriptor(ContentDescriptor.TIME_PICKER_24_HOURS_FORMAT.toString());
        timePickerButton.tap();

        Screen screen = testDevice.getActiveScreen();
        TimePicker timePicker = screen.getTimePicker();
        Calendar calendar = timePicker.getValue();
        assertEquals("Hour from time picker does not match expected result.",
                     calendar.get(Calendar.HOUR),
                     EXPECTED_HOUR);
        assertEquals("MINUTE from time picker does not match expected result.",
                     calendar.get(Calendar.MINUTE),
                     EXPECTED_MINUTE);
    }

}
