package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertLocation;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startLocationActivity;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
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

    private static final double TEST_LATITUDE = -43.12;

    private static final double TEST_LONGITUDE = 123.32;

    private static final String FAILURE_INDICATED_ERROR_MESSAGE = "Mocking location provider indicated failure.";

    private static final String LOCATION_MOCK_UNSUCCESSFUL = "Location was not successfully mocked.";

    @BeforeClass
    public static void setUp() throws ActivityStartingException, InterruptedException, UiElementFetchingException {
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

    @Test
    public void testMockLocationMonitoredProvider() {
        GeoLocation mockLocation = new GeoLocation(MONITORED_PROVIDER_DEFAULT_LATITUDE,
                                                   MONITORED_PROVIDER_DEFAULT_LONGITUDE,
                                                   MONITORED_PROVIDER_NAME);
        assertTrue(FAILURE_INDICATED_ERROR_MESSAGE, testDevice.mockLocation(mockLocation));
        assertLocation(LOCATION_MOCK_UNSUCCESSFUL, mockLocation);
    }

    @Test
    public void testMockLocationNotMonitoredProvider() {
        // set the monitored provider's location, so we know its different from the one we set in the non-monitored one
        GeoLocation monitoredProviderLocation = new GeoLocation(MONITORED_PROVIDER_DEFAULT_LATITUDE,
                                                                MONITORED_PROVIDER_DEFAULT_LONGITUDE,
                                                                MONITORED_PROVIDER_NAME);
        assertTrue(FAILURE_INDICATED_ERROR_MESSAGE, testDevice.mockLocation(monitoredProviderLocation));

        GeoLocation nonMonitoredProviderLocation = new GeoLocation(TEST_LATITUDE,
                                                                   TEST_LONGITUDE,
                                                                   NOT_MONITORED_PROVIDER_NAME);
        assertTrue(FAILURE_INDICATED_ERROR_MESSAGE, testDevice.mockLocation(nonMonitoredProviderLocation));
        assertLocation("Location changed from the monitored provider, when mocking another provider.",
                       monitoredProviderLocation);
    }

    @Test
    public void testMockSameProviderSeveralTimes() {
        GeoLocation initialMockLocation = new GeoLocation(MONITORED_PROVIDER_DEFAULT_LATITUDE,
                                                          MONITORED_PROVIDER_DEFAULT_LONGITUDE,
                                                          MONITORED_PROVIDER_NAME);
        assertTrue(FAILURE_INDICATED_ERROR_MESSAGE, testDevice.mockLocation(initialMockLocation));
        assertLocation(LOCATION_MOCK_UNSUCCESSFUL, initialMockLocation);

        GeoLocation secondMockLocation = new GeoLocation(TEST_LATITUDE, TEST_LONGITUDE, MONITORED_PROVIDER_NAME);
        assertTrue(FAILURE_INDICATED_ERROR_MESSAGE, testDevice.mockLocation(secondMockLocation));
        assertLocation(LOCATION_MOCK_UNSUCCESSFUL, secondMockLocation);
    }
}
