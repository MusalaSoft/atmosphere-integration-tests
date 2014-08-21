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
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
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

    private static final String FAILURE_INDICATED_ERROR_MESSAGE = "Mocking location provider indicated failure.";

    private static final String LOCATION_MOCK_UNSUCCESSFUL = "Location was not successfully mocked.";

    private static GeoLocation defaultMonitoredProviderLocation;

    @BeforeClass
    public static void setUp() throws ActivityStartingException, InterruptedException, UiElementFetchingException {
        defaultMonitoredProviderLocation = new GeoLocation(MONITORED_PROVIDER_DEFAULT_LATITUDE,
                                                           MONITORED_PROVIDER_DEFAULT_LONGITUDE,
                                                           MONITORED_PROVIDER_NAME);
        defaultMonitoredProviderLocation.setAltitude(MONITORED_PROVIDER_DEFAULT_ALTITUDE);

        DeviceParameters testDeviceParameters = new DeviceParameters();
        testDeviceParameters.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParameters);
        setTestDevice(testDevice);

        startLocationActivity();
    }

    @AfterClass
    public static void tearDown() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Before
    public void setUpTest() {
        // set the monitored provider to its default location for the test
        boolean isMockSuccessful = testDevice.mockLocation(defaultMonitoredProviderLocation);
        assertTrue("Setting the monitored provider to its default location indicated failure.", isMockSuccessful);
    }

    @Test
    public void testMockLocationMonitoredProvider() {
        assertLocation(LOCATION_MOCK_UNSUCCESSFUL, defaultMonitoredProviderLocation);
    }

    @Test
    public void testMockLocationNotMonitoredProvider() {
        GeoLocation nonMonitoredProviderLocation = new GeoLocation(SAN_FRANCISCO_LATITUDE,
                                                                   SAN_FRANCISCO_LONGITUDE,
                                                                   NOT_MONITORED_PROVIDER_NAME);
        nonMonitoredProviderLocation.setAltitude(SAN_FRANCISCO_ALTITUDE);

        boolean isMockSuccessful = testDevice.mockLocation(nonMonitoredProviderLocation);
        assertTrue(FAILURE_INDICATED_ERROR_MESSAGE, isMockSuccessful);
        assertLocation("Location changed from the monitored provider, when mocking another provider.",
                       defaultMonitoredProviderLocation);
    }

    @Test
    public void testMockLocationSameProviderSeveralTimes() {
        assertLocation(LOCATION_MOCK_UNSUCCESSFUL, defaultMonitoredProviderLocation);

        GeoLocation secondMockLocation = new GeoLocation(SAN_FRANCISCO_LATITUDE,
                                                         SAN_FRANCISCO_LONGITUDE,
                                                         MONITORED_PROVIDER_NAME);
        secondMockLocation.setAltitude(SAN_FRANCISCO_ALTITUDE);

        boolean isMockSuccessful = testDevice.mockLocation(secondMockLocation);
        assertTrue(FAILURE_INDICATED_ERROR_MESSAGE, isMockSuccessful);
        assertLocation(LOCATION_MOCK_UNSUCCESSFUL, secondMockLocation);
    }
}
