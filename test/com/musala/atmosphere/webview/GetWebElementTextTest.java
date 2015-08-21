package com.musala.atmosphere.webview;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWebViewActivity;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.client.WebView;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 * 
 * @author denis.bialev
 *
 */
public class GetWebElementTextTest extends BaseIntegrationTest {
    private static final String WEB_ELEMENT_ID = "info";

    private static final String BUTTON_WEB_ELEMENT_ID = "btn";

    private static final String SUB_TEXT_WEB_ELEMENT_ID = "subtext";

    private static final String NO_TEXT_WEB_ELEMENT_ID = "fname";

    private static final String DIFFERENT_TEXT_ASSERT_MESSAGE = "The received text from the element is defferent than expected.";

    private static Screen screen;

    private static WebView webView;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder deviceSelectorBuilder = new DeviceSelectorBuilder();
        DeviceSelector deviceSelector = deviceSelectorBuilder.deviceType(DeviceType.DEVICE_PREFERRED).build();
        initTestDevice(deviceSelector);
        setTestDevice(testDevice);
        screen = testDevice.getActiveScreen();

        startWebViewActivity();

        webView = screen.getWebView(VALIDATOR_APP_PACKAGE);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);

        releaseDevice();
    }

    @Test
    public void testGetWebElementText() throws Exception {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, WEB_ELEMENT_ID);
        String expectedText = "Personal info";
        assertTrue(DIFFERENT_TEXT_ASSERT_MESSAGE, webElement.getText().equals(expectedText));
    }

    @Test
    public void testGetWebElementButtonText() throws Exception {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, BUTTON_WEB_ELEMENT_ID);
        String expectedText = "Submit";
        assertTrue(DIFFERENT_TEXT_ASSERT_MESSAGE, webElement.getText().equals(expectedText));
    }

    @Test
    public void testGetTextWithSubtext() throws Exception {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, SUB_TEXT_WEB_ELEMENT_ID);
        String expectedText = "This text contains subscript text.";
        assertTrue(DIFFERENT_TEXT_ASSERT_MESSAGE, webElement.getText().equals(expectedText));
    }

    @Test
    public void testGetTextNoText() throws Exception {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, NO_TEXT_WEB_ELEMENT_ID);
        String expectedText = "";
        assertTrue(DIFFERENT_TEXT_ASSERT_MESSAGE, webElement.getText().equals(expectedText));
    }
}
