package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setupOndeviceValidator;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

public class UninstallApplicationTest extends BaseIntegrationTest {
    private static final String RANDOM_APP_PACKAGE = "com.musala.atmosphere.random";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        setupOndeviceValidator();
        releaseDevice();
    }

    @Test(expected = ActivityStartingException.class)
    public void testUninstallExistingApplication() throws Exception {
        Boolean result = testDevice.uninstallApplication(VALIDATOR_APP_PACKAGE);

        assertTrue("The uninstallation of the validator was not successful.", result);

        testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
    }

    @Test
    public void testUninstallUnexistingApplication() {
        Boolean result = testDevice.uninstallApplication(RANDOM_APP_PACKAGE);

        assertTrue("The uninstallation of the unexisting application was not successful.", result);
    }
}
