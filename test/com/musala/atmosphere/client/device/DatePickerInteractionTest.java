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
import com.musala.atmosphere.client.DatePicker;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.util.settings.ElementValidationType;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class DatePickerInteractionTest extends BaseIntegrationTest {
    private static final int JELLY_BEAN_MR2_API_LEVEL = 18;

    private static final int EXPECTED_YEAR = 2014;

    private static final int EXPECTED_MONTH = 7;

    private static final int EXPECTED_DAY = 21;

    private static final String EXPECTED_GET_DATE_STRING = "Aug-21-2014";

    private static final int MONTH_TO_SET = 1;

    private static final int DAY_OF_MONTH_TO_SET = 14;

    private static final int YEAR_TO_SET = 2015;

    private static final String EXPECTED_SET_DATE_STRING = "Feb-14-2015";

    private static final String DONE_BUTTON_TEXT = "Done";

    private static final String DONE_BUTTON_RESOURCE_ID = "android:id/button1";

    private static final int DEFAULT_WAIT_FOR_GET_PICKER = 2000;

    private static final String DATE_PICKER_WIDGET_PACKAGE = "android.widget.DatePicker";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
        startPickerActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
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
    public void testGetCurrentDateFromPicker() throws Exception {
        UiElement datePickerButton = getElementByContentDescriptor(ContentDescriptor.DATE_PICKER.toString());
        datePickerButton.tap();
        Screen screen = testDevice.getActiveScreen();

        waitForDatePicker(screen);

        DatePicker datePicker = screen.getDatePicker();

        Calendar calendar = datePicker.getValue();
        assertEquals("Year from date picker does not match expected result.",
                     EXPECTED_YEAR,
                     calendar.get(Calendar.YEAR));
        assertEquals("Month from date picker does not match expected result.",
                     EXPECTED_MONTH,
                     calendar.get(Calendar.MONTH));
        assertEquals("Day of month from date picker does not match expected result.",
                     EXPECTED_DAY,
                     calendar.get(Calendar.DAY_OF_MONTH));

        assertEquals("Date string value from date picker does not match expected result",
                     EXPECTED_GET_DATE_STRING,
                     datePicker.getStringValue());
    }

    @Test
    public void testSetDateInDatePicker() throws Exception {

        UiElement datePickerButton = getElementByContentDescriptor(ContentDescriptor.DATE_PICKER.toString());
        datePickerButton.setValidationType(ElementValidationType.MANUAL);
        datePickerButton.tap();

        Screen screen = testDevice.getActiveScreen();

        waitForDatePicker(screen);

        DatePicker datePicker = screen.getDatePicker();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, YEAR_TO_SET);
        calendar.set(Calendar.MONTH, MONTH_TO_SET);
        calendar.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH_TO_SET);

        boolean result = datePicker.setValue(calendar);

        assertTrue("Setting date in a date picker failed", result);

        clickDone(screen);

        screen.updateScreen();

        datePickerButton = getElementByContentDescriptor(ContentDescriptor.DATE_PICKER.toString());
        datePickerButton.tap();

        waitForDatePicker(screen);

        datePicker = screen.getDatePicker();
        Calendar pickerCalendar = datePicker.getValue();
        assertEquals("Year from date picker does not match expected result.",
                     YEAR_TO_SET,
                     pickerCalendar.get(Calendar.YEAR));
        assertEquals("Month from date picker does not match expected result.",
                     MONTH_TO_SET,
                     pickerCalendar.get(Calendar.MONTH));
        assertEquals("Day of month from date picker does not match expected result.",
                     DAY_OF_MONTH_TO_SET,
                     pickerCalendar.get(Calendar.DAY_OF_MONTH));

        assertEquals("Date string value from date picker does not match expected result",
                     EXPECTED_SET_DATE_STRING,
                     datePicker.getStringValue());
    }

    /**
     * Press the button done of the date picker.
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

    /**
     * Waits for the DatePicker widget to appear.
     * 
     * @param screen
     */
    private void waitForDatePicker(Screen screen) {
        UiElementSelector datePickerSelector = new UiElementSelector();
        datePickerSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, DATE_PICKER_WIDGET_PACKAGE);
        assertTrue("Date picker widget should be on Screen",
                   screen.waitForElementExists(datePickerSelector, DEFAULT_WAIT_FOR_GET_PICKER));
        screen.updateScreen();
    }
}
