package com.musala.atmosphere.webview;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWebViewActivity;
import static org.junit.Assert.assertEquals;

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
 * @author yavor.stankov
 *
 */
public class WebElementGetCssValueTest extends BaseIntegrationTest {
    private static final String WEB_ELEMENT_CLASS_NAME = "absolute";

    private static final String ERROR_MESSAGE = "The received attribute value is not the same as expected.";

    private static final String EXPECTED_POSITION_VALUE = "absolute";

    private static final String EXPECTED_TOP_VALUE = "80px";

    private static final String EXPECTED_LEFT_VALUE = "20px";

    private static final String EXPECTED_WIDTH_VALUE = "230px";

    private static final String EXPECTED_HEIGHT_VALUE = "30px";

    private static final String EXPECTED_BORDER_VALUE = "1px solid rgb(0, 0, 0)";

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
    public void testGetCssValueTop() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_TOP_VALUE, element.getCssValue("top"));
    }

    @Test
    public void testGetCssValuePosition() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_POSITION_VALUE, element.getCssValue("position"));
    }

    @Test
    public void testGetCssValueLeft() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_LEFT_VALUE, element.getCssValue("left"));
    }

    @Test
    public void testGetCssValueWidth() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_WIDTH_VALUE, element.getCssValue("width"));
    }

    @Test
    public void testGetCssValueHeight() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_HEIGHT_VALUE, element.getCssValue("height"));
    }

    @Test
    public void testGetCssValueBorder() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_BORDER_VALUE, element.getCssValue("border"));
    }
}
