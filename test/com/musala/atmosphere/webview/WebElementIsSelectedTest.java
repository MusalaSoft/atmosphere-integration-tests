package com.musala.atmosphere.webview;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWebViewActivity;
import static org.junit.Assert.assertFalse;
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
public class WebElementIsSelectedTest extends BaseIntegrationTest {

    private static final String SELECTED_WEB_ELEMENT_ID = "male";

    private static final String NOT_SELECTED_WEB_ELEMENT_ID = "female";

    private static final String NOT_SELECTABLE_ELEMENT_ID = "fname";

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
    public void testIsWebElementSelected() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, SELECTED_WEB_ELEMENT_ID);
        assertTrue("The selected element was registered as not selected element.", webElement.isSelected());
    }

    @Test
    public void testIsWebElementNotSelected() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, NOT_SELECTED_WEB_ELEMENT_ID);
        assertFalse("The not selected element was registered as selected element.", webElement.isSelected());
    }

    @Test
    public void testIsNotSelectableWebElementSelected() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, NOT_SELECTABLE_ELEMENT_ID);
        assertFalse("The not selectable element was registered as selected element.", webElement.isSelected());
    }
}
