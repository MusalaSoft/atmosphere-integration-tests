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
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class TimePickerInteractionTest extends BaseIntegrationTest {
    private static final int JELLY_BEAN_MR2_API_LEVEL = 18;

    private static final int EXPECTED_HOUR = 11;

    private static final int EXPECTED_MINUTE = 45;

    private static final int HOUR_TO_SET = 21;

    private static int MINUTE_TO_SET = 32;

    private static final int AM = Calendar.AM;

    private static final int PM = Calendar.PM;

    private static final String DONE_BUTTON_TEXT = "Done";

    private static final String DONE_BUTTON_RESOURCE_ID = "android:id/button1";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
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

        int apiLevel = testDevice.getInformation().getApiLevel();
        if (apiLevel >= JELLY_BEAN_MR2_API_LEVEL) {
            doneButtonSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, DONE_BUTTON_RESOURCE_ID);
        } else {
            doneButtonSelector.addSelectionAttribute(CssAttribute.TEXT, DONE_BUTTON_TEXT);
        }

        UiElement doneButton = screen.getElement(doneButtonSelector);
        doneButton.tap();
    }

}
