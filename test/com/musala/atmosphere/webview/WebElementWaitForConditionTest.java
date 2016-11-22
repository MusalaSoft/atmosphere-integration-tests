package com.musala.atmosphere.webview;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWebViewActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.client.WebView;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 *
 * @author yavor.stankov
 *
 */
public class WebElementWaitForConditionTest extends BaseWebViewIntegrationTest {
    private static final String CREATE_ELEMENT_BUTTON_ID = "create";

    private static final String TEST_ELEMENT_ID = "present";

    private static final int WAIT_FOR_CONDITION_TIMEOUT = 5000;

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

    @AfterClass
    public static void tearDown() throws Exception {
        Assume.assumeNotNull(BaseIntegrationTest.testDevice);
        releaseDevice();
    }

    @After
    public void tearDownTest() {
        if (testDevice != null) {
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        }
    }

    @Test
    public void testWaitForExistingElement() {
        UiWebElement createElementButton = webView.findElement(WebElementSelectionCriterion.ID,
                                                               CREATE_ELEMENT_BUTTON_ID);
        createElementButton.tap();

        assertTrue("The method returned false, but the element existed on the screen.",
                   webView.waitForElementExists(WebElementSelectionCriterion.ID,
                                                TEST_ELEMENT_ID,
                                                WAIT_FOR_CONDITION_TIMEOUT));
    }

    @Test
    public void testWaitForUnexistingElement() {
        assertFalse("The method returned true, but the element didn't exist on the screen.",
                    webView.waitForElementExists(WebElementSelectionCriterion.ID,
                                                 TEST_ELEMENT_ID,
                                                 WAIT_FOR_CONDITION_TIMEOUT));
    }
}
