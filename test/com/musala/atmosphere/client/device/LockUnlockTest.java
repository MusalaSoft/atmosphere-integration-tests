package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setupAndStartMainActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

/**
 * 
 * @author georgi.gaydarov
 * 
 */
public class LockUnlockTest extends BaseIntegrationTest {
    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());

        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void testUnlockLockDevice() throws Exception {
        assertTrue("Unlocking the device returned false.", testDevice.setLocked(false));

        assertFalse("Device shouldn't be locked after .unlock().", testDevice.isLocked());
        assertTrue("Device should be awake after .unlock().", testDevice.isAwake());

        setupAndStartMainActivity();

        assertValidatorIsStarted();

        assertTrue("Locking the device returned false.", testDevice.setLocked(true));
        try {
            assertValidatorIsStarted();
            fail("The validation element should not be available when the device is locked.");
        } catch (UiElementFetchingException e) {
            // this should be thrown.
        }

        assertTrue("Device should be locked after .lock().", testDevice.isLocked());
        assertFalse("Device shouldn't be awake after .lock().", testDevice.isAwake());
    }
}