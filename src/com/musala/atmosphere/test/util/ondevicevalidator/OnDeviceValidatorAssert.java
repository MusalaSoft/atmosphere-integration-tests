package com.musala.atmosphere.test.util.ondevicevalidator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.ScrollableView;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.ScreenOrientation;
import com.musala.atmosphere.commons.SmsMessage;
import com.musala.atmosphere.commons.beans.BatteryState;
import com.musala.atmosphere.commons.beans.PhoneNumber;

/**
 * Class containing assertions and other useful methods implementing logic for the OnDeviceValidator.
 * 
 * @author yordan.petrov
 * 
 */
public class OnDeviceValidatorAssert {
    private final static String CONTENT_DESCRIPTOR = "[content-desc=%s]";

    private final static String CSS_CLASS = "[class=%s]";

    private final static String CSS_PACKAGE = "[package=%s]";

    private final static String PATH_TO_APK_DIR = "./ondeviceComponents/";

    private final static String NAME_OF_APK_FILE = "OnDeviceValidator-release.apk";

    private final static String PATH_TO_APK = PATH_TO_APK_DIR + NAME_OF_APK_FILE;

    private final static String VALIDATOR_APP_PACKAGE = "com.musala.atmosphere.ondevice.validator";

    private final static String VALIDATOR_MAIN_ACTIVITY = ".MainActivity";

    private final static String VALIDATOR_ORIENTATION_ACTIVITY = ".OrientationActivity";

    private final static String VALIDATOR_ACCELERATION_ACTIVITY = ".AccelerationActivity";

    private final static String VALIDATOR_GESTURE_ACTIVITY = ".GestureActivity";

    private final static String VALIDATOR_WAIT_TEST_ACTIVITY = ".WaitTestActivity";

    private final static String VALIDATOR_SCROLL_ACTIVITY = ".ScrollableViewActivity";

    private final static String VALIDATOR_MAGNETIC_FIELD_ACTIVITY = ".MagneticFieldActivity";

    private final static String VALIDATOR_HORIZONTAL_SCROLL_ACTIVITY = ".HorizontalScrollableViewActivity";

    private final static String VALIDATOR_PICKER_ACTIVITY = ".PickerViewActivity";

    private final static String VALIDATOR_NOTIFICATION_TEST_ACTIVITY = ".NotificationTestActivity";

    private final static String VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC = "ATMOSPHEREValidator";

    private final static String VALIDATOR_IS_NOT_STARTED_MESSAGE = "ATMOSPHERE Validator has not beeen started.";

    private static final String VALIDATOR_SERVICE_ACTIVITY = ".StartServiceActivity";

    private final static String PHONE_PACKAGE_NAME = "com.android.phone";

    private final static String INCOMING_CALL_TEXT = "Incoming call";

    private final static String ON_HOLD_TEXT = "On hold";

    private final static String END_CALL_BUTTON_DESCRIPTOR = "End";

    private final static int ASSERT_TIMEOUT = 3000;

    private final static int APP_STARTUP_WAIT_TIME = 4000;

    private final static String EXPECTED_SWIPE_UP_TEXT = "Swiped up!";

    private final static String EXPECTED_SWIPE_DOWN_TEXT = "Swiped down!";

    private final static String EXPECTED_SWIPE_LEFT_TEXT = "Swiped left!";

    private final static String EXPECTED_SWIPE_RIGHT_TEXT = "Swiped right!";

    private final static String EXPECTED_DOUBLE_TAP_TEXT = "Double tapped!";

    private static final String EXPECTED_PINCH_IN_TEXT = "Pinched in!";

    private static final String EXPECTED_PINCH_OUT_TEXT = "Pinched out!";

    private static final String EXPECTED_LONG_PRESS = "Long pressed!";

    private static final String EXPECTED_SCROLL_TO_END = "Bottom";

    private static final Object EXPECTED_SCROLL_TO_BEGINNING = "Top";

    private static final String START_SERVICE_ACTIVITY = ".StartServiceActivity";

    private static Device device;

    private static Screen screen;

    // TODO move methods concerning setting up the OnDeviceValidator application
    // and starting its activities in an
    // utility class

    /**
     * Asserts that the acceleration on the x axis of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected acceleration on the x axis.
     * @throws UiElementFetchingException
     */
    public static void assertAccelerationX(String message, float expected) throws UiElementFetchingException {
        UiElementSelector accelerationXSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.ACCELERATION_X_BOX,
                                                                                           String.valueOf(expected));

        assertElementExists(message, accelerationXSelector);
    }

    /**
     * Asserts that the acceleration on the y axis of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected acceleration on the y axis.
     * @throws UiElementFetchingException
     */
    public static void assertAccelerationY(String message, float expected) throws UiElementFetchingException {
        UiElementSelector accelerationYSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.ACCELERATION_Y_BOX,
                                                                                           String.valueOf(expected));

        assertElementExists(message, accelerationYSelector);
    }

    /**
     * Asserts that the acceleration on the z axis of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected acceleration on the z axis.
     * @throws UiElementFetchingException
     */
    public static void assertAccelerationZ(String message, float expected) throws UiElementFetchingException {
        UiElementSelector accelerationZBoxSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.ACCELERATION_Z_BOX,
                                                                                              String.valueOf(expected));

        assertElementExists(message, accelerationZBoxSelector);
    }

    /**
     * Asserts that the auto rotation of the test device is turned off.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertAutoRotationOff(String message) throws UiElementFetchingException {
        UiElementSelector autoRotationOffSelector = new UiElementSelector();
        autoRotationOffSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                      ContentDescriptor.AUTO_ROTATION_BUTTON.toString());
        autoRotationOffSelector.addSelectionAttribute(CssAttribute.ENABLED, false);

        assertElementExists(message, autoRotationOffSelector);
    }

    /**
     * Asserts that the auto rotation of the test device is turned on.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertAutoRotationOn(String message) throws UiElementFetchingException {
        UiElementSelector autoRotationOnSelector = new UiElementSelector();

        autoRotationOnSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                     ContentDescriptor.AUTO_ROTATION_BUTTON.toString());
        autoRotationOnSelector.addSelectionAttribute(CssAttribute.ENABLED, true);

        assertElementExists(message, autoRotationOnSelector);
    }

    /**
     * Asserts that the battery level of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to display if assertion fails.
     * @param expectedBatteryLevel
     *        - the expected battery level.
     * @throws UiElementFetchingException
     */
    public static void assertBatteryLevel(String message, int expectedBatteryLevel) throws UiElementFetchingException {
        UiElementSelector batteryLevelSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.BATTERY_LEVEL_BOX,
                                                                                          String.valueOf(expectedBatteryLevel));

        assertElementExists(message, batteryLevelSelector);
    }

    /**
     * Asserts that the battery of the test device is low.
     * 
     * @param message
     *        - message to be displayed if the assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertBatteryLow(String message) throws UiElementFetchingException {
        UiElementSelector batteryLowSelector = new UiElementSelector();

        batteryLowSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                 ContentDescriptor.BATTERY_LOW_FLAG.toString());
        batteryLowSelector.addSelectionAttribute(CssAttribute.ENABLED, true);

        assertElementExists(message, batteryLowSelector);
    }

    /**
     * Asserts that the battery of the test device is not low.
     * 
     * @param message
     *        - message to be displayed if the assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertBatteryNotLow(String message) throws UiElementFetchingException {
        UiElementSelector batteryNotLowSelector = new UiElementSelector();

        batteryNotLowSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                    ContentDescriptor.BATTERY_LOW_FLAG.toString());
        batteryNotLowSelector.addSelectionAttribute(CssAttribute.ENABLED, false);

        assertElementExists(message, batteryNotLowSelector);
    }

    /**
     * Asserts that the battery state of the device has been set to the expected value.
     * 
     * @param message
     *        - message to display if assertion fails.
     * @param expectedBatteryState
     *        - the expected battery state.
     * @throws UiElementFetchingException
     */
    public static void assertBatteryState(String message, BatteryState expectedBatteryState)
        throws UiElementFetchingException {
        UiElementSelector batteryStateSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.BATTERY_STATUS_BOX,
                                                                                          String.valueOf(expectedBatteryState));

        assertElementExists(message, batteryStateSelector);
    }

    /**
     * Asserts that a call by a phone number is accepted by the device.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param phoneNumber
     *        - the expected number to be in call with the device.
     * 
     * @throws UiElementFetchingException
     */
    public static void assertCallAccepted(String message, PhoneNumber phoneNumber) throws UiElementFetchingException {
        UiElementSelector incomingCallElementSelector = createSelectorByPhonePackage(INCOMING_CALL_TEXT);
        UiElementSelector onHoldElementSelector = createSelectorByPhonePackage(ON_HOLD_TEXT);
        UiElementSelector phoneNumberElementSelector = createSelectorByPhonePackage(String.valueOf(phoneNumber));
        UiElementSelector endCallButtonSelector = new UiElementSelector();

        endCallButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, END_CALL_BUTTON_DESCRIPTOR);

        assertElementExists(message, endCallButtonSelector);
        assertElementGone(message, incomingCallElementSelector);
        assertElementGone(message, onHoldElementSelector);
        assertElementExists(message, phoneNumberElementSelector);
    }

    /**
     * Asserts that the sent call is successfully canceled by the device.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * 
     * @throws UiElementFetchingException
     */
    public static void assertCallCanceled(String message) throws UiElementFetchingException {
        UiElementSelector incomingCallElementSelector = createSelectorByPhonePackage(INCOMING_CALL_TEXT);

        assertElementGone(message, incomingCallElementSelector);
    }

    /**
     * Asserts that a call by a phone number is put on hold by the device.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param phoneNumber
     *        - the expected number to be put on hold on the device.
     * 
     * @throws UiElementFetchingException
     */
    public static void assertCallOnHold(String message, PhoneNumber phoneNumber) throws UiElementFetchingException {
        UiElementSelector onHoldElementSelector = createSelectorByPhonePackage(ON_HOLD_TEXT);
        UiElementSelector phoneNumberElementSelector = createSelectorByPhonePackage(String.valueOf(phoneNumber));

        assertElementExists(message, onHoldElementSelector);
        assertElementExists(message, phoneNumberElementSelector);
    }

    /**
     * Asserts that a phone number is calling the device.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param phoneNumber
     *        - the expected number to be calling the device.
     * 
     * @throws UiElementFetchingException
     */
    public static void assertCallReceived(String message, PhoneNumber phoneNumber) throws UiElementFetchingException {
        UiElementSelector phoneNumberElementSelector = createSelectorByPhonePackage(String.valueOf(phoneNumber));
        UiElementSelector incomingCallElementSelector = createSelectorByPhonePackage(INCOMING_CALL_TEXT);

        assertElementExists(message, incomingCallElementSelector);
        assertElementExists(message, phoneNumberElementSelector);
    }

    /**
     * Asserts that there is a physical camera on the tested device.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertCameraPresent(String message) throws UiElementFetchingException {
        UiElementSelector cameraNumberTextSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.CAMERA_NUMBER_TEXT_BOX,
                                                                                              String.valueOf(0));

        assertElementGone(message, cameraNumberTextSelector);
    }

    /**
     * Asserts that there is no physical camera on the tested device.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertCameraNotPresent(String message) throws UiElementFetchingException {
        UiElementSelector cameraNumberTextSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.CAMERA_NUMBER_TEXT_BOX,
                                                                                              String.valueOf(0));

        assertElementExists(message, cameraNumberTextSelector);
    }

    /**
     * Asserts that the connection type of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected connection type.
     * @throws UiElementFetchingException
     */
    public static void assertConnectionType(String message, int expected) throws UiElementFetchingException {
        UiElementSelector connectionTypeSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.CONNECTION_TYPE_BOX,
                                                                                            String.valueOf(expected));

        assertElementExists(message, connectionTypeSelector);
    }

    /**
     * Asserts that the text of {@link UiElement} which shows when you double tap is the same as expected
     * 
     * @param message
     *        - message to be displayed if the assertion fails
     */
    public static void assertDoubleTapped(String message) throws UiElementFetchingException {
        assertGestureReceived(message, EXPECTED_DOUBLE_TAP_TEXT);
    }

    /**
     * Asserts that the text of the passed {@link UiElement} is the same as the expected. <b>Worsk correctly only for
     * edit text views.</b>
     * 
     * @param message
     *        - message to be displayed if the assertion fails
     * @param element
     *        - the element whose text will be checked
     * @param expected
     *        - the expected text of the element
     */
    public static void assertElementText(String message, UiElement element, String expected) {
        UiElementSelector selector = element.getElementSelector();
        String elementText = selector.getStringValue(CssAttribute.TEXT);

        assertEquals(message, expected, elementText);
    }

    /**
     * Asserts that the text of {@link UiElement} is the same as the expected
     * 
     * @param message
     *        - message to be displayed if the assertion fails
     * @param expectedText
     *        - the expected text of the element
     */
    public static void assertGestureReceived(String message, String expectedText) throws UiElementFetchingException {
        UiElementSelector gestureReceivedSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.GESTURE_VALIDATOR,
                                                                                             expectedText);

        assertElementExists(message, gestureReceivedSelector);
    }

    /**
     * Asserts that the the test device is in airplane mode.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertInAirplaneMode(String message) throws UiElementFetchingException {
        UiElementSelector airplaneModeSelector = new UiElementSelector();

        airplaneModeSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                   ContentDescriptor.AIRPLANE_MODE_FLAG.toString());
        airplaneModeSelector.addSelectionAttribute(CssAttribute.ENABLED, true);

        assertElementExists(message, airplaneModeSelector);
    }

    /**
     * Asserts that a given string has been inputted on the test device.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected input string.
     * @throws UiElementFetchingException
     */
    public static void assertInputText(String message, String expected) throws UiElementFetchingException {
        UiElementSelector inputTextSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.INPUT_TEXT_BOX,
                                                                                       expected);

        assertElementExists(message, inputTextSelector);
    }

    /**
     * Asserts that the battery status box is focused.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertInputTextBoxIsFocused(String message) throws UiElementFetchingException {
        UiElementSelector inputTextSelector = new UiElementSelector();

        inputTextSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                ContentDescriptor.INPUT_TEXT_BOX.toString());
        inputTextSelector.addSelectionAttribute(CssAttribute.FOCUSED, true);

        assertElementExists(message, inputTextSelector);
    }

    /**
     * Asserts that the text of {@link UiElement} which shows when you long press is the same as expected
     * 
     * @param message
     *        - message to be displayed if the assertion fails
     */
    public static void assertLongClicked(String message) throws UiElementFetchingException {
        assertGestureReceived(message, EXPECTED_LONG_PRESS);
    }

    /**
     * Asserts that the magnetic field on the x axis of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected magnetic field on the x axis.
     * @throws UiElementFetchingException
     */
    public static void assertMagneticFieldX(String message, float expected) throws UiElementFetchingException {
        UiElementSelector magneticFieldXSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.MAGNETIC_FIELD_X_BOX,
                                                                                            String.valueOf(expected));

        assertElementExists(message, magneticFieldXSelector);
    }

    /**
     * Asserts that the magnetic field on the y axis of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected magnetic field on the y axis.
     * @throws UiElementFetchingException
     */
    public static void assertMagneticFieldY(String message, float expected) throws UiElementFetchingException {
        UiElementSelector magneticFieldYSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.MAGNETIC_FIELD_Y_BOX,
                                                                                            String.valueOf(expected));

        assertElementExists(message, magneticFieldYSelector);
    }

    /**
     * Asserts that the magnetic field on the z axis of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected magnetic field on the z axis.
     * @throws UiElementFetchingException
     */
    public static void assertMagneticFieldZ(String message, float expected) throws UiElementFetchingException {
        UiElementSelector magneticFieldZSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.MAGNETIC_FIELD_Z_BOX,
                                                                                            String.valueOf(expected));

        assertElementExists(message, magneticFieldZSelector);
    }

    /**
     * Asserts that the the test device is not in airplane mode.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertNotInAirplaneMode(String message) throws UiElementFetchingException {
        UiElementSelector airplaneModeSelector = new UiElementSelector();

        airplaneModeSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                   ContentDescriptor.AIRPLANE_MODE_FLAG.toString());
        airplaneModeSelector.addSelectionAttribute(CssAttribute.ENABLED, false);

        assertElementExists(message, airplaneModeSelector);
    }

    /**
     * Asserts that the test device is not connected to power.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertNotPowerConnected(String message) throws UiElementFetchingException {
        UiElementSelector powerConnectedSelector = new UiElementSelector();

        powerConnectedSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                     ContentDescriptor.POWER_CONNECTED_FLAG.toString());
        powerConnectedSelector.addSelectionAttribute(CssAttribute.ENABLED, false);

        assertElementExists(message, powerConnectedSelector);
    }

    /**
     * Asserts that the orientation azimuth of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected orientation azimuth.
     * @throws UiElementFetchingException
     */
    public static void assertOrientationAzimuth(String message, float expected) throws UiElementFetchingException {
        UiElementSelector orientationAzimuthSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.ORIENTATION_AZIMUTH_BOX,
                                                                                                String.valueOf(expected));

        assertElementExists(message, orientationAzimuthSelector);
    }

    /**
     * Asserts that the orientation pitch of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected orientation pitch.
     * @throws UiElementFetchingException
     */
    public static void assertOrientationPitch(String message, float expected) throws UiElementFetchingException {
        UiElementSelector orientationPitchSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.ORIENTATION_PITCH_BOX,
                                                                                              String.valueOf(expected));

        assertElementExists(message, orientationPitchSelector);
    }

    /**
     * Asserts that the orientation roll of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected orientation roll.
     * @throws UiElementFetchingException
     */
    public static void assertOrientationRoll(String message, float expected) throws UiElementFetchingException {
        UiElementSelector orientationRollSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.ORIENTATION_ROLL_BOX,
                                                                                             String.valueOf(expected));

        assertElementExists(message, orientationRollSelector);
    }

    /**
     * Asserts that the {@link UiElement} has responded to the pinch in gesture as expected.
     * 
     * @param message
     *        - the message to be displayed if the assertion fails
     * @throws UiElementFetchingException
     */
    public static void assertPinchedIn(String message) throws UiElementFetchingException {
        assertGestureReceived(message, EXPECTED_PINCH_IN_TEXT);
    }

    /**
     * Asserts that the {@link UiElement} has responded to the pinch out gesture as expected.
     * 
     * @param message
     *        - the message to be displayed if the assertion fails
     * @throws UiElementFetchingException
     */
    public static void assertPinchedOut(String message) throws UiElementFetchingException {
        assertGestureReceived(message, EXPECTED_PINCH_OUT_TEXT);
    }

    /**
     * Asserts that the test device is connected to power.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertPowerConnected(String message) throws UiElementFetchingException {
        UiElementSelector powerConnectedSelector = new UiElementSelector();

        powerConnectedSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                     ContentDescriptor.POWER_CONNECTED_FLAG.toString());
        powerConnectedSelector.addSelectionAttribute(CssAttribute.ENABLED, true);

        assertElementExists(message, powerConnectedSelector);
    }

    /**
     * Asserts that the sent sms message is successfully received by device
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param smsMessage
     *        - the expected message to be received.
     * 
     * @throws UiElementFetchingException
     */
    public static void assertReceivedSms(String message, SmsMessage smsMessage) throws UiElementFetchingException {
        UiElementSelector senderPhoneSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.SMS_SENDER_PHONE_BOX,
                                                                                         smsMessage.getPhoneNumber()
                                                                                                   .toString());
        UiElementSelector smsTextSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.SMS_TEXT_BOX,
                                                                                     smsMessage.getText());

        assertElementExists(message, senderPhoneSelector);
        assertElementExists(message, smsTextSelector);
    }

    /**
     * Asserts that the screen rotation of the test device has been set to the expected value.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param expected
     *        - the expected screen orientation.
     * @throws UiElementFetchingException
     */
    public static void assertScreenOrientation(String message, ScreenOrientation expected)
        throws UiElementFetchingException {
        UiElementSelector screenOrientationSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.SCREEN_ORIENTATION_BOX,
                                                                                               String.valueOf(expected.getOrientationNumber()));

        assertElementExists(message, screenOrientationSelector);
    }

    /**
     * Asserts that the text of {@link UiElement} which shows when you swipe down is the same as expected
     * 
     * @param message
     *        - message to be displayed if the assertion fails
     */
    public static void assertSwipedDown(String message) throws UiElementFetchingException {

        assertGestureReceived(message, EXPECTED_SWIPE_DOWN_TEXT);
    }

    /**
     * Asserts that the text of {@link UiElement} which shows when you swipe left is the same as expected
     * 
     * @param message
     *        - message to be displayed if the assertion fails
     */
    public static void assertSwipedLeft(String message) throws UiElementFetchingException {
        assertGestureReceived(message, EXPECTED_SWIPE_LEFT_TEXT);
    }

    /**
     * Asserts that the text of {@link UiElement} which shows when you swipe right is the same as expected
     * 
     * @param message
     *        - message to be displayed if the assertion fails
     */
    public static void assertSwipedRight(String message) throws UiElementFetchingException {

        assertGestureReceived(message, EXPECTED_SWIPE_RIGHT_TEXT);
    }

    /**
     * Asserts that the text of {@link UiElement} which shows when you swipe up is the same as expected
     * 
     * @param message
     *        - message to be displayed if the assertion fails
     */
    public static void assertSwipedUp(String message) throws UiElementFetchingException {

        assertGestureReceived(message, EXPECTED_SWIPE_UP_TEXT);
    }

    /**
     * Asserts the the OnDeviceValidator has been started.
     * 
     * @throws UiElementFetchingException
     */
    public static void assertValidatorIsStarted() throws UiElementFetchingException {
        // If the validator app activity is not started, this element fetching
        // will fail.
        UiElementSelector validationViewSelector = new UiElementSelector();

        validationViewSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                     VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC);
        validationViewSelector.addSelectionAttribute(CssAttribute.ENABLED, true);

        assertElementExists(VALIDATOR_IS_NOT_STARTED_MESSAGE, validationViewSelector);
    }

    public static void assertValidatorIsNotStarted(String message) throws UiElementFetchingException {
        // If the validator app activity is not started, this element fetching
        // will fail.
        UiElementSelector validationViewSelector = new UiElementSelector();

        validationViewSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                     VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC);
        validationViewSelector.addSelectionAttribute(CssAttribute.ENABLED, true);

        assertElementGone(message, validationViewSelector);
    }

    /**
     * Asserts that the WiFi button is disabled.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertWiFiIsOff(String message) throws UiElementFetchingException {
        UiElementSelector wifiButtonSelector = new UiElementSelector();

        wifiButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                 ContentDescriptor.WIFI_FLAG.toString());

        wifiButtonSelector.addSelectionAttribute(CssAttribute.ENABLED, false);

        assertElementExists(message, wifiButtonSelector);
    }

    /**
     * Asserts that the WiFi button is enabled.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @throws UiElementFetchingException
     */
    public static void assertWiFiIsOn(String message) throws UiElementFetchingException {
        UiElementSelector wifiButtonSelector = new UiElementSelector();

        wifiButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                 ContentDescriptor.WIFI_FLAG.toString());

        wifiButtonSelector.addSelectionAttribute(CssAttribute.ENABLED, true);

        assertElementExists(message, wifiButtonSelector);
    }

    /**
     * Asserts that a given UI element is not on screen.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param selector
     *        - the selector of the given UI element.
     * @throws UiElementFetchingException
     */
    public static void assertUIElementNotOnScreen(String message, UiElementSelector selector) {
        try {
            screen.getElements(selector);
            fail(message);
        } catch (Exception e) {
            if (!(e instanceof UiElementFetchingException)) {
                fail(message);
            }
        }
    }

    /**
     * Asserts that a given UI element is on screen.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param selector
     *        - the selector of the given UI element.
     */
    public static void assertUIElementOnScreen(String message, UiElementSelector selector) {
        assertElementExists(message, selector);
    }

    /**
     * Asserts that the view is scrolled to end.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     */
    public static void assertScrollToEnd(String message) {
        UiElementSelector scrollToEndSelector = new UiElementSelector();
        scrollToEndSelector.addSelectionAttribute(CssAttribute.TEXT, EXPECTED_SCROLL_TO_END);

        assertElementExists(message, scrollToEndSelector);
    }

    /**
     * Asserts that the view is scrolled to beginning.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     */
    public static void assertScrollToBeginning(String message) {
        UiElementSelector scrollToBeginningSelector = new UiElementSelector();
        scrollToBeginningSelector.addSelectionAttribute(CssAttribute.TEXT, EXPECTED_SCROLL_TO_BEGINNING);

        assertElementExists(message, scrollToBeginningSelector);
    }

    /**
     * Asserts that the view is scrolled backwards.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     */
    public static void assertScrollBackward(String message) {
        UiElementSelector scrollBackwardSelector = new UiElementSelector();

        scrollBackwardSelector.addSelectionAttribute(CssAttribute.TEXT, EXPECTED_SCROLL_TO_END);

        assertElementGone(message, scrollBackwardSelector);
    }

    /**
     * Asserts that the view is scrolled forward.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     */
    public static void assertScrollForward(String message) {
        UiElementSelector scrollForwardSelector = new UiElementSelector();
        scrollForwardSelector.addSelectionAttribute(CssAttribute.TEXT, EXPECTED_SCROLL_TO_BEGINNING);

        assertElementGone(message, scrollForwardSelector);
    }

    /**
     * Asserts that text field is cleared.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     */
    public static void assertTextIsCleared(String message) {
        UiElementSelector textFieldSelector = createSelectorByTextAndContentDescriptor(ContentDescriptor.CLEAR_TEXT_BOX,
                                                                                       "");

        assertElementExists(message, textFieldSelector);
    }

    /**
     * Asserts that element with given selector exist on the screen.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param selector
     *        - the selector of the given UI element.
     */
    public static void assertElementExists(String message, UiElementSelector selector) {
        assertTrue(message, screen.waitForElementExists(selector, ASSERT_TIMEOUT));
    }

    /**
     * Asserts that element with given selector doesn't exist on the screen.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param selector
     *        - the selector of the given UI element.
     */
    public static void assertElementGone(String message, UiElementSelector selector) {
        assertTrue(message, screen.waitUntilElementGone(selector, ASSERT_TIMEOUT));
    }

    /**
     * Creates selector by given text and content descriptor.
     * 
     * @param contentDescriptor
     *        - content descriptor.
     * @param text
     *        - text.
     * @return - selector with the given attributes.
     */
    public static UiElementSelector createSelectorByTextAndContentDescriptor(ContentDescriptor contentDescriptor,
                                                                             String text) {
        UiElementSelector textSelector = new UiElementSelector();

        textSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, contentDescriptor.toString());
        textSelector.addSelectionAttribute(CssAttribute.TEXT, text);

        return textSelector;
    }

    /**
     * Creates selector using given text and package name.
     * 
     * @param text
     *        - text.
     * @return - selector with the given attributes.
     */
    public static UiElementSelector createSelectorByPhonePackage(String text) {
        UiElementSelector selector = new UiElementSelector();

        selector.addSelectionAttribute(CssAttribute.PACKAGE_NAME, PHONE_PACKAGE_NAME);
        selector.addSelectionAttribute(CssAttribute.TEXT, text);

        return selector;
    }

    /**
     * Gets the current test device.
     * 
     * @return - the current test device.
     */
    public static Device getDevice() {
        return device;
    }

    /**
     * Gets UI element by given CSS class.
     * 
     * @param cssClass
     *        - CSS class to search for.
     * @return - UI element with the given class.
     * @throws UiElementFetchingException
     * @throws InvalidCssQueryException
     * @throws XPathExpressionException
     */
    public static UiElement getElementByClass(String cssClass)
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        Screen activeScreen = device.getActiveScreen();
        activeScreen.updateScreen();
        final String query = String.format(CSS_CLASS, cssClass);
        return activeScreen.getElementByCSS(query);
    }

    /**
     * Gets UI element by given content descriptor.
     * 
     * @param contentDescriptor
     *        - content descriptor to search for.
     * @return - UI element with the given content descriptor.
     * @throws UiElementFetchingException
     * @throws InvalidCssQueryException
     * @throws XPathExpressionException
     */
    public static UiElement getElementByContentDescriptor(String contentDescriptor)
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        Screen activeScreen = device.getActiveScreen();
        activeScreen.updateScreen();
        final String query = String.format(CONTENT_DESCRIPTOR, contentDescriptor);
        return activeScreen.getElementByCSS(query);
    }

    /**
     * Gets list of UI elements by given CSS package.
     * 
     * @param packageName
     *        - package name to search for.
     * @return - UI element list with the given package.
     * @throws UiElementFetchingException
     * @throws InvalidCssQueryException
     * @throws XPathExpressionException
     */
    public static List<UiElement> getElementsByPackage(String packageName)
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        Screen activeScreen = device.getActiveScreen();
        activeScreen.updateScreen();
        final String query = String.format(CSS_PACKAGE, packageName);
        return activeScreen.getAllElementsByCSS(query);
    }

    /**
     * Sets the test device, on which the OnDeviceValidator will be installed and UI elements will be fetched.
     * 
     * @param device
     *        - the test device to be set.
     */
    public static void setTestDevice(Device device) {
        OnDeviceValidatorAssert.device = device;
        OnDeviceValidatorAssert.screen = device.getActiveScreen();
    }

    /**
     * Setups the OnDeviceValidator on the test device and starts its acceleration activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void setupAndStartAccelerationActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        setupOndeviceValidator();
        startAccelerationActivity();
    }

    /**
     * Setups the OnDeviceValidator on the test device and starts its gesture activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void setupAndStartGestureActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        setupOndeviceValidator();
        startGestureActivity();
    }

    /**
     * Setups the OnDeviceValidator on the test device and starts its main activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void setupAndStartMainActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        setupOndeviceValidator();
        startMainActivity();
    }

    /**
     * Setups the OnDeviceValidator on the test device and starts its wait test activity.
     * 
     * @throws UiElementFetchingException
     * @throws InterruptedException
     * @throws ActivityStartingException
     */
    public static void setupAndStartWaitTestActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        setupOndeviceValidator();
        startWaitTestActivity();
    }

    /**
     * Setups the OnDeviceValidator on the test device and starts its orientation activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void setupAndStartOrientationActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        setupOndeviceValidator();
        startOrientationActivity();
    }

    /**
     * Setups the OnDeviceValidator on the test device and starts its magnetic field activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void setupAndStartMagneticFieldActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        setupOndeviceValidator();
        startMagneticFieldActivity();
    }

    /**
     * Installs the OnDeviceValidator on the test device.
     */
    public static void setupOndeviceValidator() {
        device.installAPK(PATH_TO_APK, true);
    }

    /**
     * Starts OnDeviceValidator's acceleration activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void startAccelerationActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_ACCELERATION_ACTIVITY);
    }

    /**
     * Starts OnDeviceValidator's wait test activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void startWaitTestActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_WAIT_TEST_ACTIVITY);
    }

    public static void startServiceActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_SERVICE_ACTIVITY);
    }

    /**
     * Starts an activity on the test device by given application package and activity name.
     * 
     * @param appPackage
     *        - application package.
     * @param appActivity
     *        - application activity name.
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void startActivity(String appPackage, String appActivity)
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        device.setLocked(false);
        device.startActivity(appPackage, appActivity);
        screen.waitForWindowUpdate(appPackage, APP_STARTUP_WAIT_TIME);

        assertValidatorIsStarted();
    }

    /**
     * Starts OnDeviceValidator's gesture activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void startGestureActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_GESTURE_ACTIVITY);
    }

    /**
     * Starts OnDeviceValidator's main activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void startMainActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_MAIN_ACTIVITY);
    }

    /**
     * Starts OnDeviceValidator's orientation activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void startOrientationActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_ORIENTATION_ACTIVITY);
    }

    public static void startScrollActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_SCROLL_ACTIVITY);
    }

    public static void startHorizontalScrollActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_HORIZONTAL_SCROLL_ACTIVITY);
    }

    public static void startMagneticFieldActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_MAGNETIC_FIELD_ACTIVITY);
    }

    /**
     * Setups the OnDeviceValidator on the test device and starts its scroll activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void setupAndStartScrollActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        setupOndeviceValidator();
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_SCROLL_ACTIVITY);
    }

    /**
     * Setups the OnDeviceValidator on the test device and starts its horizontal scroll activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void setupAndStartHorizontalScrollActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        setupOndeviceValidator();
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_HORIZONTAL_SCROLL_ACTIVITY);
    }

    /**
     * Setups and starts the Activity that starts a simple service
     * 
     * @throws InterruptedException
     * @throws ActivityStartingException
     * @throws UiElementFetchingException
     */
    public static void setupAndStartServiceActivity()
        throws InterruptedException,
            ActivityStartingException,
            UiElementFetchingException {
        setupOndeviceValidator();
        startActivity(VALIDATOR_APP_PACKAGE, START_SERVICE_ACTIVITY);
    }

    /**
     * Returns the requested ScrollableView.
     * 
     * @param contentDescription
     *        - content description by which the ScrollableView is selected
     * @return the requested ScrollableView
     * @throws UiElementFetchingException
     * @throws InvalidCssQueryException
     * @throws XPathExpressionException
     */
    public static ScrollableView getScrollableView(String contentDescription)
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        Screen activeScreen = device.getActiveScreen();
        activeScreen.updateScreen();
        UiElementSelector selector = new UiElementSelector();

        selector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, contentDescription);

        return activeScreen.getScrollableView(selector);
    }

    /**
     * Starts picker view activity.
     * 
     * @throws ActivityStartingException
     * @throws InterruptedException
     * @throws UiElementFetchingException
     */
    public static void startPickerActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_PICKER_ACTIVITY);
    }

    /**
     * Starts the notification test activity.
     * 
     * @throws ActivityStartingException
     *         - if the activity was not started successfully
     * @throws InterruptedException
     *         - if the starting of the activity was interrupted
     * @throws UiElementFetchingException
     *         - if the UI element could not be found
     */
    public static void startNotificationTestActivity()
        throws ActivityStartingException,
            InterruptedException,
            UiElementFetchingException {
        startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_NOTIFICATION_TEST_ACTIVITY);
    }
}