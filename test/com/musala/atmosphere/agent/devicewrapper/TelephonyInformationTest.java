package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class TelephonyInformationTest extends BaseIntegrationTest {
    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);
        Thread.sleep(1000);
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void testGetTelephonyInformation() throws Exception {
        assertNotNull("Getting telephony information returned null.", testDevice.getTelephonyInformation());
    }
}
