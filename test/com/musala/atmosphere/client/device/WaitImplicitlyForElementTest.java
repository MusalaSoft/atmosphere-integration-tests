package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWaitTestActivity;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.MultipleElementsFoundException;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

/**
 * 
 * @author dimcho.nedev
 * 
 * Tests the implicit wait functionality for the UI elements.
 *
 */
public class WaitImplicitlyForElementTest extends BaseIntegrationTest {
	private static final int IMPLICIT_WAIT_TIMEOUT = 5000;
	
	private static final int EXPLICIT_WAIT_TIMEOUT = 5000;
	
	private static final String FAILD_TO_TAP_MESSAGE = "Failed to tap selected button";

	private static final String MAIN_BUTTON_DESCRIPTOR = "MainActivityButton";

	private static final String MAIN_BUTTON_TEXT = "View Main";

	private static Screen activeScreen;

	@BeforeClass
	public static void setUp() throws Exception {
		DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
		DeviceSelector testDeviceSelector = selectorBuilder.build();
		initTestDevice(testDeviceSelector);
		setTestDevice(testDevice);

		activeScreen = testDevice.getActiveScreen();
	}

	@Before
	public void setUpTest() throws ActivityStartingException, InterruptedException, UiElementFetchingException {
		startWaitTestActivity();
	}

	@After
	public void tearDowntest() {
		testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		releaseDevice();
	}

	@Test
	public void testWaitForElementWithImplicitWait() throws Exception {
		activeScreen.setImplicitWaitTimeout(IMPLICIT_WAIT_TIMEOUT);
		tapMainActivityButton();

		UiElementSelector orientationActivityButtonSelectror = getOrintationActivityButtonSelector();

		UiElement orientationActivityButton = activeScreen.getElement(orientationActivityButtonSelectror);
		boolean isCreateDataButtonTapped = orientationActivityButton.tap();

		assertTrue(FAILD_TO_TAP_MESSAGE, isCreateDataButtonTapped);
	}

	@Test(expected = UiElementFetchingException.class)
	public void testWaitForElementNoImplicitWait() throws Exception {
		activeScreen.setImplicitWaitTimeout(0);

		tapMainActivityButton();

		UiElementSelector orientationActivityButtonSelectror = getOrintationActivityButtonSelector();

		activeScreen.getElement(orientationActivityButtonSelectror);
	}

	@Test
	public void testExplicitWaitShouldOverrideImplicitWait() throws Exception {
		activeScreen.setImplicitWaitTimeout(0);
		
		tapMainActivityButton();

		UiElementSelector orientationActivityButtonSelectror = getOrintationActivityButtonSelector();

		boolean isOrientationActivityButtonExist = activeScreen.waitForElementExists(
				orientationActivityButtonSelectror,
				EXPLICIT_WAIT_TIMEOUT);

		assertTrue("Wait for element exists returned false.", isOrientationActivityButtonExist);
	}

	private void tapMainActivityButton() throws MultipleElementsFoundException, UiElementFetchingException {
		UiElementSelector selector = new UiElementSelector();
		selector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, MAIN_BUTTON_DESCRIPTOR);
		selector.addSelectionAttribute(CssAttribute.TEXT, MAIN_BUTTON_TEXT);

		UiElement mainButton = activeScreen.getElement(selector);
		boolean isMainActivityButtonTapped = mainButton.tap();

		Assume.assumeTrue(FAILD_TO_TAP_MESSAGE, isMainActivityButtonTapped);
	}

	private UiElementSelector getOrintationActivityButtonSelector() {
		UiElementSelector orientationActivityButtonSelectror = new UiElementSelector();
		orientationActivityButtonSelectror.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
				"OrientationActivityButton");
		orientationActivityButtonSelectror.addSelectionAttribute(CssAttribute.TEXT, "View Orientation");

		return orientationActivityButtonSelectror;
	}

}
