package com.musala.atmosphere.test.util.ondevicevalidator;

import static com.musala.atmosphere.test.util.ondevicevalidator.UIAssert.assertIsEnabled;
import static com.musala.atmosphere.test.util.ondevicevalidator.UIAssert.assertIsFocused;
import static com.musala.atmosphere.test.util.ondevicevalidator.UIAssert.assertNotEnabled;
import static com.musala.atmosphere.test.util.ondevicevalidator.UIAssert.assertText;

import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.BatteryState;
import com.musala.atmosphere.commons.ScreenOrientation;

/**
 * Class containing assertions and other useful methods implementing logic for the OnDeviceValidator.
 * 
 * @author yordan.petrov
 * 
 */
public class OnDeviceValidatorAssert
{
	private final static String CONTENT_DESCRIPTOR = "[content-desc=%s]";

	private final static String CSS_CLASS = "[class=%s]";

	private final static String PATH_TO_APK_DIR = "./";

	private final static String NAME_OF_APK_FILE = "OnDeviceValidator.apk";

	private final static String PATH_TO_APK = PATH_TO_APK_DIR + NAME_OF_APK_FILE;

	private final static String VALIDATOR_APP_PACKAGE = "com.musala.atmosphere.ondevice.validator";

	private final static String VALIDATOR_MAIN_ACTIVITY = "MainActivity";

	private final static String VALIDATOR_ORIENTATION_ACTIVITY = "OrientationActivity";

	private final static String VALIDATOR_ACCELERATION_ACTIVITY = "AccelerationActivity";

	private final static String VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC = "ATMOSPHEREValidator";

	private final static int APP_STARTUP_WAIT_TIME = 2000;

	private static Device device;

	/**
	 * Sets the test device, on which the OnDeviceValidator will be installed and UI elements will be fetched.
	 * 
	 * @param device
	 *        - the test device to be set.
	 */
	public static void setTestDevice(Device device)
	{
		OnDeviceValidatorAssert.device = device;
	}

	/**
	 * Gets the current test device.
	 * 
	 * @return - the current test device.
	 */
	public static Device getDevice()
	{
		return device;
	}

	/**
	 * Gets UI element by given content descriptor.
	 * 
	 * @param contentDescriptor
	 *        - content descriptor to search for.
	 * @return - UI element with the given content descriptor.
	 * @throws UiElementFetchingException
	 */
	public static UiElement getElementByContentDescriptor(String contentDescriptor) throws UiElementFetchingException
	{
		Screen activeScreen = device.getActiveScreen();
		final String query = String.format(CONTENT_DESCRIPTOR, contentDescriptor);
		return activeScreen.getElementCSS(query);
	}

	/**
	 * Gets UI element by given css class.
	 * 
	 * @param cssClass
	 *        - css class to search for.
	 * @return - UI element withe the given class.
	 * @throws UiElementFetchingException
	 */
	public static UiElement getElementByClass(String cssClass) throws UiElementFetchingException
	{
		Screen activeScreen = device.getActiveScreen();
		final String query = String.format(CSS_CLASS, cssClass);
		return activeScreen.getElementCSS(query);
	}

	/**
	 * Installs the OnDeviceValidator on the test device.
	 */
	public static void setupOndeviceValidator()
	{
		device.installAPK(PATH_TO_APK);
	}

	/**
	 * Asserts the the OnDeviceValidator has been started.
	 * 
	 * @throws UiElementFetchingException
	 */
	public static void assertValidatorIsStarted() throws UiElementFetchingException
	{
		// If the validator app activity is not started, this element fetching will fail.
		UiElement validationView = getElementByContentDescriptor(VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC);
		assertIsEnabled("ATMOSPHERE Validator has not beeen started.", validationView);

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
			UiElementFetchingException
	{
		device.unlock();
		device.startActivity(appPackage, appActivity);
		Thread.sleep(APP_STARTUP_WAIT_TIME);

		assertValidatorIsStarted();
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
			UiElementFetchingException
	{
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
			UiElementFetchingException
	{
		startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_ORIENTATION_ACTIVITY);
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
			UiElementFetchingException
	{
		startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_ACCELERATION_ACTIVITY);
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
			UiElementFetchingException
	{
		setupOndeviceValidator();
		startMainActivity();
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
			UiElementFetchingException
	{
		setupOndeviceValidator();
		startOrientationActivity();
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
			UiElementFetchingException
	{
		setupOndeviceValidator();
		startAccelerationActivity();
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
	public static void assertBatteryLevel(String message, int expectedBatteryLevel) throws UiElementFetchingException
	{
		UiElement batteryLevelBox = getElementByContentDescriptor(ContentDescriptor.BATTERY_LEVEL_BOX.toString());

		assertText(message, batteryLevelBox, String.valueOf(expectedBatteryLevel));
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
		throws UiElementFetchingException
	{
		UiElement batteryStateBox = getElementByContentDescriptor(ContentDescriptor.BATTERY_STATUS_BOX.toString());

		assertText(message, batteryStateBox, expectedBatteryState.toString());
	}

	/**
	 * Asserts that the test device is connected to power.
	 * 
	 * @param message
	 *        - message to be displayed if assertion fails.
	 * @throws UiElementFetchingException
	 */
	public static void assertPowerConnected(String message) throws UiElementFetchingException
	{
		UiElement powerConnectedFlag = getElementByContentDescriptor(ContentDescriptor.POWER_CONNECTED_FLAG.toString());

		assertIsEnabled(message, powerConnectedFlag);
	}

	/**
	 * Asserts that the test device is not connected to power.
	 * 
	 * @param message
	 *        - message to be displayed if assertion fails.
	 * @throws UiElementFetchingException
	 */
	public static void assertNotPowerConnected(String message) throws UiElementFetchingException
	{
		UiElement powerConnectedFlag = getElementByContentDescriptor(ContentDescriptor.POWER_CONNECTED_FLAG.toString());

		assertNotEnabled(message, powerConnectedFlag);
	}

	/**
	 * Asserts that the battery of the test device is low.
	 * 
	 * @param message
	 *        - message to be displayed if the assertion fails.
	 * @throws UiElementFetchingException
	 */
	public static void assertBatteryLow(String message) throws UiElementFetchingException
	{
		UiElement batteryLowFlag = getElementByContentDescriptor(ContentDescriptor.BATTERY_LOW_FLAG.toString());

		assertIsEnabled(message, batteryLowFlag);
	}

	/**
	 * Asserts that the battery of the test device is not low.
	 * 
	 * @param message
	 *        - message to be displayed if the assertion fails.
	 * @throws UiElementFetchingException
	 */
	public static void assertBatteryNotLow(String message) throws UiElementFetchingException
	{
		UiElement batteryLowFlag = getElementByContentDescriptor(ContentDescriptor.BATTERY_LOW_FLAG.toString());

		assertNotEnabled(message, batteryLowFlag);
	}

	/**
	 * Asserts that the the test device is in airplane mode.
	 * 
	 * @param message
	 *        - message to be displayed if assertion fails.
	 * @throws UiElementFetchingException
	 */
	public static void assertInAirplaneMode(String message) throws UiElementFetchingException
	{
		UiElement airplaneModeFlag = getElementByContentDescriptor(ContentDescriptor.AIRPLANE_MODE_FLAG.toString());

		assertIsEnabled(message, airplaneModeFlag);
	}

	/**
	 * Asserts that the the test device is not in airplane mode.
	 * 
	 * @param message
	 *        - message to be displayed if assertion fails.
	 * @throws UiElementFetchingException
	 */
	public static void assertNotInAirplaneMode(String message) throws UiElementFetchingException
	{
		UiElement airplaneModeFlag = getElementByContentDescriptor(ContentDescriptor.AIRPLANE_MODE_FLAG.toString());

		assertNotEnabled(message, airplaneModeFlag);
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
	public static void assertInputText(String message, String expected) throws UiElementFetchingException
	{
		UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.INPUT_TEXT_BOX.toString());

		assertText(message, inputTextBox, expected);
	}

	/**
	 * Asserts that the battery status box is focused.
	 * 
	 * @param message
	 *        - message to be displayed if assertion fails.
	 * @throws UiElementFetchingException
	 */
	public static void assertInputTextBoxIsFocused(String message) throws UiElementFetchingException
	{
		UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.INPUT_TEXT_BOX.toString());

		assertIsFocused(message, inputTextBox);
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
	public static void assertOrientationRoll(String message, float expected) throws UiElementFetchingException
	{
		UiElement orientationRollBox = getElementByContentDescriptor(ContentDescriptor.ORIENTATION_ROLL_BOX.toString());

		assertText(message, orientationRollBox, String.valueOf(expected));
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
	public static void assertOrientationPitch(String message, float expected) throws UiElementFetchingException
	{
		UiElement orientationPitchBox = getElementByContentDescriptor(ContentDescriptor.ORIENTATION_PITCH_BOX.toString());

		assertText(message, orientationPitchBox, String.valueOf(expected));
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
	public static void assertOrientationAzimuth(String message, float expected) throws UiElementFetchingException
	{
		UiElement orientationAzimuthBox = getElementByContentDescriptor(ContentDescriptor.ORIENTATION_AZIMUTH_BOX.toString());

		assertText(message, orientationAzimuthBox, String.valueOf(expected));
	}

	/**
	 * Asserts that the acceleration on the x axis of the test device has been set to the expected value.
	 * 
	 * @param message
	 *        - message to be displayed if assertion fails.
	 * @param expected
	 *        - the expected acceleration on the x axis.
	 * @throws UiElementFetchingException
	 */
	public static void assertAccelerationX(String message, float expected) throws UiElementFetchingException
	{
		UiElement accelerationXBox = getElementByContentDescriptor(ContentDescriptor.ACCELERATION_X_BOX.toString());

		assertText(message, accelerationXBox, String.valueOf(expected));
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
	public static void assertAccelerationY(String message, float expected) throws UiElementFetchingException
	{
		UiElement accelerationYBox = getElementByContentDescriptor(ContentDescriptor.ACCELERATION_Y_BOX.toString());

		assertText(message, accelerationYBox, String.valueOf(expected));
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
	public static void assertAccelerationZ(String message, float expected) throws UiElementFetchingException
	{
		UiElement accelerationZBox = getElementByContentDescriptor(ContentDescriptor.ACCELERATION_Z_BOX.toString());

		assertText(message, accelerationZBox, String.valueOf(expected));
	}

	/**
	 * Asserts that the auto rotation of the test device is turned on.
	 * 
	 * @param message
	 *        - message to be displayed if assertion fails.
	 * @throws UiElementFetchingException
	 */
	public static void assertAutoRotationOn(String message) throws UiElementFetchingException
	{
		UiElement autoRotationFlag = getElementByContentDescriptor(ContentDescriptor.AUTO_ROTATION_BUTTON.toString());

		assertIsEnabled(message, autoRotationFlag);
	}

	/**
	 * Asserts that the auto rotation of the test device is turned off.
	 * 
	 * @param message
	 *        - message to be displayed if assertion fails.
	 * @throws UiElementFetchingException
	 */
	public static void assertAutoRotationOff(String message) throws UiElementFetchingException
	{
		UiElement autoRotationFlag = getElementByContentDescriptor(ContentDescriptor.AUTO_ROTATION_BUTTON.toString());

		assertNotEnabled(message, autoRotationFlag);
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
		throws UiElementFetchingException
	{
		UiElement screenOrientationBox = getElementByContentDescriptor(ContentDescriptor.SCREEN_ORIENTATION_BOX.toString());

		assertText(message, screenOrientationBox, String.valueOf(expected.getOrientationNumber()));
	}
}