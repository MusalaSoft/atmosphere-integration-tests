package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsNotStarted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
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

/**
 * 
 * @author georgi.gaydarov
 * 
 */
public class LockUnlockTest extends BaseIntegrationTest {
    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);

        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testUnlockLockDevice() throws Exception {
        assertTrue("Unlocking the device returned false.", testDevice.setLocked(false));

        assertFalse("Device shouldn't be locked after .unlock().", testDevice.isLocked());
        assertTrue("Device should be awake after .unlock().", testDevice.isAwake());

        startMainActivity();

        assertValidatorIsStarted();

        assertTrue("Locking the device returned false.", testDevice.setLocked(true));

        assertValidatorIsNotStarted("The validation element should not be available when the device is locked.");

        assertTrue("Device should be locked after .lock().", testDevice.isLocked());
        assertFalse("Device shouldn't be awake after .lock().", testDevice.isAwake());
    }
}