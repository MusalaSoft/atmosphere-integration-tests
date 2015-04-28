package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startLocationActivity;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;
import com.musala.atmosphere.commons.util.GeoLocation;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author delyan.dimitrov
 * 
 */
public class DisableMockLocationTest extends BaseIntegrationTest {
    private static final long PROVIDER_CONNECT_TIMEOUT = 10000;

    private static final int DISABLE_WAIT_TIMEOUT = 15000;

    private static final String DISABLED_TEXT = "Disabled!";

    private static final String TEST_PROVIDER = "AtmosphereTestProvider";

    private static final double DEFAULT_LATITUDE = 0;

    private static final double DEFAULT_LONGITUDE = 0;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testDisableMockLocation() throws Exception {
        GeoLocation mockLocation = new GeoLocation(DEFAULT_LATITUDE, DEFAULT_LONGITUDE, TEST_PROVIDER);
        testDevice.mockLocation(mockLocation);
        startLocationActivity();
        Thread.sleep(PROVIDER_CONNECT_TIMEOUT);

        testDevice.disableMockLocation(TEST_PROVIDER);
        UiElementSelector disabledProviderIndicatorSelector = new UiElementSelector();
        disabledProviderIndicatorSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                                ContentDescriptor.PROVIDER_DISCONNECT_MONITOR.toString());
        disabledProviderIndicatorSelector.addSelectionAttribute(CssAttribute.TEXT, DISABLED_TEXT);

        Screen deviceActiveScreen = testDevice.getActiveScreen();
        boolean isProviderDisalbed = deviceActiveScreen.waitForElementExists(disabledProviderIndicatorSelector,
                                                                             DISABLE_WAIT_TIMEOUT);
        assertTrue("Provider monitor did not indicate that the test provider is disabled.", isProviderDisalbed);
    }
}
