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
 * @author konstantin.ivanov
 *
 */
public class SubmitWebElementFormTest extends BaseIntegrationTest {
    private static Screen screen;

    private static WebView webView;

    private static final String WEB_ELEMENT_BUTTON_ID = "btn";

    private static final String WEB_ELEMENT_ID = "form";

    private static final String TEXT_TO_INPUT = "Text for Submission";

    private static final String DYNAMIC_ATTRIBUTE_NAME = "testAttribute";

    private static final String SUBMIT_BUTTON_DYNAMIC_ATTRIBUTE_VALUE = "tappedSubmit";

    private static final String EXPECTED_RESULT = null;

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
    public void testSubmitForm() throws InterruptedException {
        UiWebElement btn = webView.findElement(WebElementSelectionCriterion.ID, WEB_ELEMENT_BUTTON_ID);
        btn.tap();
        assertEquals("The button wasn't tapped",
                     SUBMIT_BUTTON_DYNAMIC_ATTRIBUTE_VALUE,
                     (String) btn.getAttribute(DYNAMIC_ATTRIBUTE_NAME));

        UiWebElement textField = webView.findElement(WebElementSelectionCriterion.ID, WEB_ELEMENT_ID);
        textField.inputText(TEXT_TO_INPUT);
        textField.submitForm();

        assertEquals("The form wasn't submitted", EXPECTED_RESULT, (String) btn.getAttribute(DYNAMIC_ATTRIBUTE_NAME));
    }
}
