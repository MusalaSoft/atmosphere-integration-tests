package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWifiConnectionTestActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.MultipleElementsFoundException;
import com.musala.atmosphere.commons.connectivity.WifiConfigurationPropertiesBuilder;
import com.musala.atmosphere.commons.connectivity.WifiConnectionProperties;
import com.musala.atmosphere.commons.connectivity.WifiConnectionQuality;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

/**
 * @author konstantin.ivanov
 */
public class WifiConnectionQualityTest extends BaseIntegrationTest {

    private static final String TEST_CONNECTION_BUTTON_ID = "com.musala.atmosphere.ondevice.validator:id/test_btn";

    private static final String PROGRESS_BAR_ID = "com.musala.atmosphere.ondevice.validator:id/runningBar";

    private static final String POOR_TYPE = "POOR";

    private static final String MODERATE_TYPE = "MODERATE";

    private static final String GOOD_TYPE = "GOOD";

    private static final String EXCELLENT_TYPE = "EXCELLENT";

    private static final String RESPONSE_CONNECTION_TYPE_FAIL = "The connection type was different from the expected";

    private static final String RESPONSE_INVALID_LOSS_ARGUMENTS = "The response was successful, despite the invalid loss arguments";

    private static final String RESPONSE_INVALID_RATE_ARGUMENTS = "The response was successful, despite the invalid rate arguments";

    private static final String RESPONSE_INVALID_LATENCY_ARGUMENTS = "The response was successful, despite the invalid latency arguments";

    private static final String RESPONSE_RESTORE_PROPERTIES_FAIL = "Failed to restore the Wifi Properties";

    private static final String RESPONSE_SET_PROPERTIES_FAIL = "Failed to set the Wifi Properties";

    private static String connectionType;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);

        setTestDevice(testDevice);
        testDevice.restoreWifiConnectionProperties();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Before
    public void setUpTest() throws Exception {
        startWifiConnectionTestActivity();
    }

    @After
    public void tearDownTest() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        testDevice.restoreWifiConnectionProperties();
    }

    @Test
    public void testPoorByRate() throws MultipleElementsFoundException, UiElementFetchingException {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.rateMin(10).rateMax(149);

        assertTrue(RESPONSE_CONNECTION_TYPE_FAIL, setWifiProperties(wifiBuilder, POOR_TYPE));
    }

    @Test
    public void testModeratebyRate() throws MultipleElementsFoundException, UiElementFetchingException {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.rateMin(200).rateMax(550);

        assertTrue(RESPONSE_CONNECTION_TYPE_FAIL, setWifiProperties(wifiBuilder, MODERATE_TYPE));
    }

    @Test
    public void testGoodByRate() throws MultipleElementsFoundException, UiElementFetchingException {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.rateMin(700).rateMax(1000);

        assertTrue(RESPONSE_CONNECTION_TYPE_FAIL, setWifiProperties(wifiBuilder, GOOD_TYPE));
    }

    @Test
    public void testPoorByRateMax() throws MultipleElementsFoundException, UiElementFetchingException {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.rateMin(10).rateMax(149);

        assertTrue(RESPONSE_CONNECTION_TYPE_FAIL, setWifiProperties(wifiBuilder, POOR_TYPE));
    }

    @Test
    public void testGoodByType() throws MultipleElementsFoundException, UiElementFetchingException {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.quality(WifiConnectionQuality.GOOD);

        assertTrue(RESPONSE_CONNECTION_TYPE_FAIL, setWifiProperties(wifiBuilder, GOOD_TYPE));
    }

    @Test
    public void testModerateByType() throws MultipleElementsFoundException, UiElementFetchingException {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.quality(WifiConnectionQuality.MODERATE);

        assertTrue(RESPONSE_CONNECTION_TYPE_FAIL, setWifiProperties(wifiBuilder, MODERATE_TYPE));
    }

    @Test
    public void testPoorByType() throws MultipleElementsFoundException, UiElementFetchingException {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.quality(WifiConnectionQuality.POOR);

        assertTrue(RESPONSE_CONNECTION_TYPE_FAIL, setWifiProperties(wifiBuilder, POOR_TYPE));
    }

    @Test
    public void testInvalidRateRange() throws MultipleElementsFoundException, UiElementFetchingException {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.rateMin(-10).rateMax(50);
        WifiConnectionProperties wifiProperties = wifiBuilder.build();

        boolean isSuccessful = testDevice.setWifiConnectionProperties(wifiProperties);
        assertFalse(RESPONSE_INVALID_RATE_ARGUMENTS, isSuccessful);
    }

    @Test
    public void testInvalidLossRange() {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.lossMin(-10).lossMax(50);
        WifiConnectionProperties wifiProperties = wifiBuilder.build();

        boolean isSuccessful = testDevice.setWifiConnectionProperties(wifiProperties);
        assertFalse(RESPONSE_INVALID_LOSS_ARGUMENTS, isSuccessful);

        wifiBuilder.lossMin(101).lossMax(150);
        wifiProperties = wifiBuilder.build();

        isSuccessful = testDevice.setWifiConnectionProperties(wifiProperties);
        assertFalse(RESPONSE_INVALID_LOSS_ARGUMENTS, isSuccessful);
    }

    @Test
    public void testInvalidLatencyRange() {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.latencyMin(-10).latencyMax(50);
        WifiConnectionProperties wifiProperties = wifiBuilder.build();

        boolean isSuccessful = testDevice.setWifiConnectionProperties(wifiProperties);
        assertFalse(RESPONSE_INVALID_LATENCY_ARGUMENTS, isSuccessful);
    }

    @Test
    public void testRestoreWifiSetting() {
        WifiConfigurationPropertiesBuilder wifiBuilder = new WifiConfigurationPropertiesBuilder();
        wifiBuilder.rateMin(100).rateMax(150);
        WifiConnectionProperties wifiProperties = wifiBuilder.build();

        boolean isSuccesful = testDevice.setWifiConnectionProperties(wifiProperties);
        assertTrue(RESPONSE_CONNECTION_TYPE_FAIL, isSuccesful);

        isSuccesful = testDevice.restoreWifiConnectionProperties();
        assertTrue(RESPONSE_RESTORE_PROPERTIES_FAIL, isSuccesful);
    }

    public static boolean setWifiProperties(WifiConfigurationPropertiesBuilder wifiBuilder, String connectionType)
        throws MultipleElementsFoundException,
            UiElementFetchingException {
        Screen screen = testDevice.getActiveScreen();

        WifiConnectionProperties wifiProperties = wifiBuilder.build();

        boolean isSuccessful = testDevice.setWifiConnectionProperties(wifiProperties);
        assertTrue(RESPONSE_SET_PROPERTIES_FAIL, isSuccessful);

        UiElementSelector btnSelector = new UiElementSelector();
        btnSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, TEST_CONNECTION_BUTTON_ID);
        UiElement btnTest = screen.getElement(btnSelector);

        btnTest.tap();

        UiElementSelector progressBarSelector = new UiElementSelector();
        progressBarSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, PROGRESS_BAR_ID);

        screen.waitUntilElementGone(progressBarSelector, 60000);

        UiElementSelector resultTextSelector = new UiElementSelector();

        resultTextSelector.addSelectionAttribute(CssAttribute.TEXT, connectionType);

        boolean isFound = screen.waitForElementExists(resultTextSelector, 30000);
        return isFound;
    }

}
