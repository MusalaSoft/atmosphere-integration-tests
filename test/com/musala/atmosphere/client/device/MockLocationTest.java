package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertLocation;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startLocationActivity;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
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

/**
 *
 * @author delyan.dimitrov
 *
 */
public class MockLocationTest extends BaseIntegrationTest {
    private static final String MONITORED_PROVIDER_NAME = "gps";

    private static final String NOT_MONITORED_PROVIDER_NAME = "test";

    private static final double MONITORED_PROVIDER_DEFAULT_LATITUDE = 0;

    private static final double MONITORED_PROVIDER_DEFAULT_LONGITUDE = 0;

    private static final double MONITORED_PROVIDER_DEFAULT_ALTITUDE = 0;

    private static final double SAN_FRANCISCO_LATITUDE = 37.767403;

    private static final double SAN_FRANCISCO_LONGITUDE = -122.429189;

    private static final double SAN_FRANCISCO_ALTITUDE = 1;

    private static final String SET_UP_MOCK_ERROR_MESSAGE = "Setting the monitored provider to its default location failed. " +
                                                            "Is mocking the device location enabled in Developer options?";

    private static final String FAILURE_INDICATED_ERROR_MESSAGE = "Mocking location provider indicated failure.";

    private static final String LOCATION_MOCK_UNSUCCESSFUL = "Location was not successfully mocked.";

    private static final String DEFAUL_GPS_COORDINATES = "0, 0, 0";

    private static final String EXPECTED_GPS_COORDINATES = "37,767, -122,429, 1";

    private static final int WAIT_FOR_EXPECTED_COORDINATES_TIMEOUT = 5_000;

    private static GeoLocation defaultMonitoredProviderLocation;

    private static Screen screen;

    @BeforeClass
    public static void setUp() throws Exception {
        defaultMonitoredProviderLocation = new GeoLocation(MONITORED_PROVIDER_DEFAULT_LATITUDE,
                                                           MONITORED_PROVIDER_DEFAULT_LONGITUDE,
                                                           MONITORED_PROVIDER_NAME);
        defaultMonitoredProviderLocation.setAltitude(MONITORED_PROVIDER_DEFAULT_ALTITUDE);

        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        startLocationActivity();

        screen = testDevice.getActiveScreen();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Before
    public void setUpTest() {
        // sets the monitored provider to its default location for the test
        boolean isMockSuccessful = testDevice.mockLocation(defaultMonitoredProviderLocation);
        assertTrue(SET_UP_MOCK_ERROR_MESSAGE, isMockSuccessful);
    }

    @Test
    public void testMockLocationMonitoredProvider() {
        assertLocation(LOCATION_MOCK_UNSUCCESSFUL, defaultMonitoredProviderLocation);
    }

    @Test
    public void testMockLocationNotMonitoredProvider() throws Exception {
        GeoLocation nonMonitoredProviderLocation = new GeoLocation(SAN_FRANCISCO_LATITUDE,
                                                                   SAN_FRANCISCO_LONGITUDE,
                                                                   NOT_MONITORED_PROVIDER_NAME);
        nonMonitoredProviderLocation.setAltitude(SAN_FRANCISCO_ALTITUDE);

        boolean isMockSuccessful = testDevice.mockLocation(nonMonitoredProviderLocation);
        assertTrue(FAILURE_INDICATED_ERROR_MESSAGE, isMockSuccessful);

        UiElementSelector coordinatesSelector = new UiElementSelector();
        coordinatesSelector.addSelectionAttribute(CssAttribute.TEXT, DEFAUL_GPS_COORDINATES);
        screen.waitForElementExists(coordinatesSelector, WAIT_FOR_EXPECTED_COORDINATES_TIMEOUT);

        assertLocation("Location changed from the monitored provider, when mocking another provider.",
                       defaultMonitoredProviderLocation);
    }

    @Test
    public void testMockLocationSameProviderSeveralTimes() throws Exception {
        assertLocation(LOCATION_MOCK_UNSUCCESSFUL, defaultMonitoredProviderLocation);

        GeoLocation secondMockLocation = new GeoLocation(SAN_FRANCISCO_LATITUDE,
                                                         SAN_FRANCISCO_LONGITUDE,
                                                         MONITORED_PROVIDER_NAME);
        secondMockLocation.setAltitude(SAN_FRANCISCO_ALTITUDE);

        boolean isMockSuccessful = testDevice.mockLocation(secondMockLocation);
        assertTrue(FAILURE_INDICATED_ERROR_MESSAGE, isMockSuccessful);

        UiElementSelector coordinatesSelector = new UiElementSelector();
        coordinatesSelector.addSelectionAttribute(CssAttribute.TEXT, EXPECTED_GPS_COORDINATES);
        screen.waitForElementExists(coordinatesSelector, WAIT_FOR_EXPECTED_COORDINATES_TIMEOUT);

        assertLocation(LOCATION_MOCK_UNSUCCESSFUL, secondMockLocation);
    }
}
