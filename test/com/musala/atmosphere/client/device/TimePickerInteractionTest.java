package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startPickerActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.TimePicker;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.client.util.settings.ElementValidationType;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class TimePickerInteractionTest extends BaseIntegrationTest {
    private static final int EXPECTED_HOUR = 11;

    private static final int EXPECTED_MINUTE = 45;

    private static final int HOUR_TO_SET = 21;

    private static int MINUTE_TO_SET = 32;

    private static final int AM = Calendar.AM;

    private static final int PM = Calendar.PM;

    private static final String DONE = "Done";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Before
    public void setUpTest() throws Exception {
        startPickerActivity();
    }

    @After
    public void tearDownTest() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @Test
    public void testGetCurrentTimeFromPicker() throws Exception {
        UiElement timePickerButton = getElementByContentDescriptor(ContentDescriptor.TIME_PICKER.toString());
        timePickerButton.tap();

        Screen screen = testDevice.getActiveScreen();
        TimePicker timePicker = screen.getTimePicker();
        Calendar calendar = timePicker.getValue();

        assertEquals("Hour from time picker does not match expected result.",
                     EXPECTED_HOUR,
                     calendar.get(Calendar.HOUR));
        assertEquals("Minute from time picker does not match expected result.",
                     EXPECTED_MINUTE,
                     calendar.get(Calendar.MINUTE));
        assertEquals("Meridiem from time picker does not match expected result.", AM, calendar.get(Calendar.AM_PM));
    }

    @Test
    public void testGetCurrentTimeFromPicker24HoursFormat() throws Exception {
        UiElement timePickerButton = getElementByContentDescriptor(ContentDescriptor.TIME_PICKER_24_HOURS_FORMAT.toString());
        timePickerButton.tap();

        Screen screen = testDevice.getActiveScreen();
        TimePicker timePicker = screen.getTimePicker();
        Calendar calendar = timePicker.getValue();

        assertEquals("Hour from time picker does not match expected result.",
                     EXPECTED_HOUR,
                     calendar.get(Calendar.HOUR));
        assertEquals("MINUTE from time picker does not match expected result.",
                     EXPECTED_MINUTE,
                     calendar.get(Calendar.MINUTE));
    }

    @Test
    public void testSetTimeInTimePicker() throws Exception {
        UiElement timePickerButton = getElementByContentDescriptor(ContentDescriptor.TIME_PICKER.toString());
        timePickerButton.setValidationType(ElementValidationType.MANUAL);
        timePickerButton.tap();

        Screen screen = testDevice.getActiveScreen();
        TimePicker timePicker = screen.getTimePicker();

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, HOUR_TO_SET);
        calendar.set(Calendar.MINUTE, MINUTE_TO_SET);

        boolean result = timePicker.setValue(calendar);

        assertTrue("Setting time in a time picker failed", result);

        clickDone(screen);

        screen.updateScreen();

        timePickerButton = getElementByContentDescriptor(ContentDescriptor.TIME_PICKER.toString());
        timePickerButton.tap();

        screen.updateScreen();

        timePicker = screen.getTimePicker();
        Calendar pickerCalendar = timePicker.getValue();

        assertEquals("Hour from time picker does not match expected result.",
                     HOUR_TO_SET,
                     pickerCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals("Minute from time picker does not match expected result.",
                     MINUTE_TO_SET,
                     pickerCalendar.get(Calendar.MINUTE));
        assertEquals("Meridiem from time picker does not match expected result.",
                     PM,
                     pickerCalendar.get(Calendar.AM_PM));
    }

    @Test
    public void testSetTimeInTimePicker24HoursFormat() throws Exception {
        UiElement timePickerButton = getElementByContentDescriptor(ContentDescriptor.TIME_PICKER_24_HOURS_FORMAT.toString());
        timePickerButton.tap();

        Screen screen = testDevice.getActiveScreen();
        TimePicker timePicker = screen.getTimePicker();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, HOUR_TO_SET);
        calendar.set(Calendar.MINUTE, MINUTE_TO_SET);

        boolean result = timePicker.setValue(calendar);

        assertTrue("Setting time in a time picker failed", result);

        clickDone(screen);

        screen.updateScreen();

        timePickerButton = getElementByContentDescriptor(ContentDescriptor.TIME_PICKER_24_HOURS_FORMAT.toString());
        timePickerButton.tap();

        screen.updateScreen();

        timePicker = screen.getTimePicker();
        Calendar pickerCalendar = timePicker.getValue();

        assertEquals("Hour from time picker does not match expected result.",
                     HOUR_TO_SET,
                     pickerCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals("Minute from time picker does not match expected result.",
                     MINUTE_TO_SET,
                     pickerCalendar.get(Calendar.MINUTE));
    }

    /**
     * Press the button done of the time picker.
     * 
     * @param screen
     * @throws Exception
     */
    private void clickDone(Screen screen) throws Exception {
        UiElementSelector doneButtonSelector = new UiElementSelector();
        doneButtonSelector.addSelectionAttribute(CssAttribute.TEXT, DONE);
        UiElement doneButton = screen.getElement(doneButtonSelector);
        doneButton.tap();
    }

}
