package com.musala.atmosphere.webview;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWebViewActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.client.WebElement;
import com.musala.atmosphere.client.WebView;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.webelement.exception.WebElementNotPresentException;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 * Tests the implicit wait functionality for the {@link WebElement}
 * web elements.
 * 
 * @author dimcho.nedev
 *
 */
public class WebElementImplicitWaitTest extends BaseWebViewIntegrationTest {
	private static final String CREATE_ELEMENT_BUTTON_ID = "create";

	private static final String TEST_ELEMENT_ID = "present";

	private static final int IMPLICIT_TIMEOUT = 5000;

	private static final int EXPLICIT_WAIT_TIMEOUT = 5000;

	private static Screen screen;

	private static WebView webView;

	@BeforeClass
	public static void setUp() throws Exception {
		DeviceSelectorBuilder deviceSelectorBuilder = new DeviceSelectorBuilder().minApi(19);
		try {
			DeviceSelector deviceSelector = deviceSelectorBuilder.deviceType(DeviceType.DEVICE_PREFERRED).build();
			initTestDevice(deviceSelector);
			setTestDevice(testDevice);
			screen = testDevice.getActiveScreen();
		} catch (NoAvailableDeviceFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	@Before
	public void setUpTest() throws Exception {
		Assume.assumeNotNull(BaseIntegrationTest.testDevice);
		startWebViewActivity();

		webView = screen.getWebView(VALIDATOR_APP_PACKAGE);
	}

	@After
	public void tearDownTest() {
		if (testDevice != null) {
			testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		Assume.assumeNotNull(BaseIntegrationTest.testDevice);
		releaseDevice();
	}

	@Test
	public void testImplicitWaitForExistingElement() {
		webView.setImplicitWaitTimeout(IMPLICIT_TIMEOUT);

		tapCreateElementButton();

		WebElement element = webView.findElement(WebElementSelectionCriterion.ID, TEST_ELEMENT_ID);

		Assert.assertNotNull("The method returned false, but the element existed on the screen.", element);
	}

	@Test(expected = WebElementNotPresentException.class)
	public void testWithZeroImplicitWaitForExistingElement() {
		webView.setImplicitWaitTimeout(0);

		tapCreateElementButton();

		webView.findElement(WebElementSelectionCriterion.ID, TEST_ELEMENT_ID);
	}

	@Test(expected = WebElementNotPresentException.class)
	public void testImplicitWaitForUnexistingElement() {
		webView.setImplicitWaitTimeout(IMPLICIT_TIMEOUT);

		webView.findElement(WebElementSelectionCriterion.ID, TEST_ELEMENT_ID);
	}

	@Test
	public void testExplicitWaitShouldOverrideImplicitWaitForExistingElement() {
		webView.setImplicitWaitTimeout(0);

		tapCreateElementButton();

		assertTrue("The method returned true, but the element didn't exist on the screen.",
				webView.waitForElementExists(WebElementSelectionCriterion.ID, 
						TEST_ELEMENT_ID, 
						EXPLICIT_WAIT_TIMEOUT));
	}

	/**
	 * Tests whether the explicit wait should override the implicit wait for
	 * nonexistent element. The test may fail on some machines but can be useful
	 * in some cases.
	 */
	@Ignore
	@Test(timeout = IMPLICIT_TIMEOUT * 2)
	public void testExplicitWaitShouldOverrideImplicitWaitForUnexistingElement() {
		webView.setImplicitWaitTimeout(IMPLICIT_TIMEOUT);

		assertFalse("The method returned true, but the element didn't exist on the screen.",
				webView.waitForElementExists(WebElementSelectionCriterion.ID, 
						TEST_ELEMENT_ID, 
						EXPLICIT_WAIT_TIMEOUT));
	}

	private void tapCreateElementButton() {
		UiWebElement createElementButton = webView.findElement(WebElementSelectionCriterion.ID,
				CREATE_ELEMENT_BUTTON_ID);
		createElementButton.tap();
	}

}
