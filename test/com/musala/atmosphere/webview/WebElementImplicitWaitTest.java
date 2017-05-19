/*package com.musala.atmosphere.webview;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWaitTestActivity;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWebViewActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.client.WebElement;
import com.musala.atmosphere.client.WebView;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.MultipleElementsFoundException;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.cs.exception.DeviceNotFoundException;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;
import com.musala.atmosphere.commons.webelement.exception.WebElementNotPresentException;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

*//**
 * Tests the implicit wait functionality for the {@link WebElement}
 * web elements.
 *
 * @author dimcho.nedev
 *
 *//*
public class WebElementImplicitWaitTest extends BaseWebViewIntegrationTest {
    private static final String CONFIG_FILE_NAME = "config.properties";

    private static final String IMPLICIT_WAIT_PROPERTY_NAME = "implicit.wait.timeout";

	private static final String CREATE_ELEMENT_BUTTON_ID = "create";

	private static final String TEST_ELEMENT_ID = "present";

	private static final int IMPLICIT_WAIT_TIMEOUT = 5000;

	private static final int EXPLICIT_WAIT_TIMEOUT = 5000;

	private static Screen screen;

	private static WebView webView;

	@BeforeClass
	public static void setUp() throws Exception {
		initDevice();
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
		testDevice.setImplicitWaitTimeout(IMPLICIT_WAIT_TIMEOUT);

		tapCreateElementButton();

		WebElement element = webView.findElement(WebElementSelectionCriterion.ID, TEST_ELEMENT_ID);

		Assert.assertNotNull("The method returned false, but the element existed on the screen.", element);
	}

	@Test(expected = WebElementNotPresentException.class)
	public void testWithZeroImplicitWaitForExistingElement() {
	    testDevice.setImplicitWaitTimeout(0);

		tapCreateElementButton();

		webView.findElement(WebElementSelectionCriterion.ID, TEST_ELEMENT_ID);
	}

	@Test(expected = WebElementNotPresentException.class)
	public void testImplicitWaitForUnexistingElement() {
	    testDevice.setImplicitWaitTimeout(IMPLICIT_WAIT_TIMEOUT);

		webView.findElement(WebElementSelectionCriterion.ID, TEST_ELEMENT_ID);
	}

	@Test
	public void testExplicitWaitShouldOverrideImplicitWaitForExistingElement() {
	    testDevice.setImplicitWaitTimeout(0);

		tapCreateElementButton();

		assertTrue("The method returned true, but the element didn't exist on the screen.",
				webView.waitForElementExists(WebElementSelectionCriterion.ID,
						TEST_ELEMENT_ID,
						EXPLICIT_WAIT_TIMEOUT));
	}

	@Test
    public void testSetImplicitWaitFromConfigFile()
        throws DeviceNotFoundException,
            MultipleElementsFoundException,
            UiElementFetchingException,
            ActivityStartingException,
            InterruptedException {

        releaseDevice();

        // creates a configuration file and store an implicit wait property key and value
        File configFile = new File(CONFIG_FILE_NAME);
        try (FileWriter fw = new FileWriter(CONFIG_FILE_NAME); BufferedWriter bw = new BufferedWriter(fw);) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bw.write(String.format("%s = %s", IMPLICIT_WAIT_PROPERTY_NAME, IMPLICIT_WAIT_TIMEOUT));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        initDevice();
        startWebViewActivity();
        webView = screen.getWebView(VALIDATOR_APP_PACKAGE);

        tapCreateElementButton();
        WebElement element = webView.findElement(WebElementSelectionCriterion.ID, TEST_ELEMENT_ID);

        Assert.assertNotNull("The method returned false, but the element existed on the screen.", element);

        // clear the configuration file
        configFile.deleteOnExit();
    }

	private static void initDevice() throws DeviceNotFoundException {
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

	*//**
	 * Tests whether the explicit wait should override the implicit wait for
	 * nonexistent element. The test may fail on some machines but can be useful
	 * in some cases.
	 *//*
	@Ignore
	@Test(timeout = IMPLICIT_WAIT_TIMEOUT * 3)
	public void testExplicitWaitShouldOverrideImplicitWaitForUnexistingElement() {
	    testDevice.setImplicitWaitTimeout(IMPLICIT_WAIT_TIMEOUT);

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
*/