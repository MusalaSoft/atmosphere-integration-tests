package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInAirplaneMode;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertNotInAirplaneMode;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class AirplaneModeTest extends BaseIntegrationTest {
    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());

        setTestDevice(testDevice);

        startMainActivity();
    }

    @AfterClass
    public static void tearDown() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testSetAirplaneModeOn() throws Exception {
        // set airplane mode on
        assumeTrue(!testDevice.getInformation().isEmulator());
        assertTrue("Setting airplane mode returned false.", testDevice.setAirplaneMode(true));
        assertInAirplaneMode("Airplane mode not set to the expected value.");
    }

    @Test
    public void testSetAirplaneModeOff() throws Exception {
        // set airplane mode off
        assumeTrue(!testDevice.getInformation().isEmulator());
        assertTrue("Setting airplane mode returned false.", testDevice.setAirplaneMode(false));
        assertNotInAirplaneMode("Airplane mode not set to the expected value.");
    }

    @Test
    public void testSetAirplaneModeOnEmulator() throws Exception {
        assumeTrue(testDevice.getInformation().isEmulator());
        assertFalse("Setting airplane mode returned true.", testDevice.setAirplaneMode(true));
        assertFalse("Setting airplane mode returned true.", testDevice.setAirplaneMode(false));
    }

    @Test
    public void testGetAirplaneModeOn() throws Exception {
        // get airplane mode on
        assumeTrue(!testDevice.getInformation().isEmulator());
        assertTrue("Setting airplane mode returned false.", testDevice.setAirplaneMode(true));
        assertTrue("Getting airplane mode returned false.", testDevice.getAirplaneMode());
    }

    @Test
    public void testGetAirplaneModeOff() throws Exception {
        // get airplane mode off
        assumeTrue(!testDevice.getInformation().isEmulator());
        assertTrue("Setting airplane mode returned false.", testDevice.setAirplaneMode(false));
        assertFalse("Getting airplane mode returned false.", testDevice.getAirplaneMode());
    }

}
