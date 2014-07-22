package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

public class ProcessRunningTest extends BaseIntegrationTest {

    private static final String PROCESS_FAKE_PACKAGE = "com.nonexisten.app";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);
        setTestDevice(testDevice);
        startMainActivity();
    }

    @AfterClass
    public static void tearDown() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testIsProcessRunning() throws Exception {
        boolean result = testDevice.isProcessRunning(VALIDATOR_APP_PACKAGE);
        assertTrue("RunningProcess returned false.", result);
    }

    @Test
    public void testIsProcessNotRunning() throws Exception {
        boolean result = testDevice.isProcessRunning(PROCESS_FAKE_PACKAGE);
        assertFalse("NotRunning Process returned true", result);
    }
}
