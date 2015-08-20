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
public class WebElementIsEnabledTest extends BaseIntegrationTest {
    private static final String ENABLED_ELEMENT_ID = "fname";

    private static final String DISABLED_ELEMENT_ID = "lname";

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
    public void testIsWebElementEnabled() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, ENABLED_ELEMENT_ID);
        assertTrue("The enabled element was registered as disabled element.", webElement.isEnabled());
    }

    @Test
    public void testIsWebElementDisabled() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, DISABLED_ELEMENT_ID);
        assertFalse("The disabled element was registered as enabled element.", webElement.isEnabled());
    }
}
