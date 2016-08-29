package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsNotStarted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;

/**
 *
 * @author georgi.gaydarov
 *
 */
public class LockUnlockTest extends BaseIntegrationTest {

    private static final int WAIT_FOR_LOCK_TIMEOUT = 2000;

    private static final int WAIT_FOR_LOCK_INTERVAL = 100;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        try {
            initTestDevice(testDeviceSelector);
            setTestDevice(testDevice);
        } catch (NoAvailableDeviceFoundException e) {
            // Nothing to do here
        }
    }

    @Before
    public void setUpTest() throws Exception {
        if (testDevice != null) {
            startMainActivity();
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (testDevice != null) {
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        }
        releaseDevice();
    }

    @Test
    public void testUnlockingDeviceWhenItIsInAsleepState() throws Exception {
        assumeNotNull(testDevice);
        if (testDevice.isAwake()) {
            testDevice.pressButton(HardwareButton.POWER);
        }

        waitForSetLocked(WAIT_FOR_LOCK_TIMEOUT);
        assertUnlockDevice();
    }

    @Test
    public void testLockUnlockWithStartedApplication() throws Exception {
        assumeNotNull(testDevice);
        assertLockDevice();
        assertValidatorIsNotStarted("The validation element should not be available when the device is locked.");

        assertUnlockDevice();
        assertValidatorIsStarted();
    }

    @Test
    public void testUnlockAndLockDevice() throws Exception {
        assumeNotNull(testDevice);
        assertLockDevice();
        assertValidatorIsNotStarted("The validation element should not be available when the device is locked.");

        assertUnlockDevice();
        assertValidatorIsStarted("The validator was not avaible after unlocking the device.");

        assertLockDevice();
        assertFalse("Device shouldn't be awake after it has been locked.", testDevice.isAwake());
        assertValidatorIsNotStarted("The validation element should not be available when the device is locked.");
    }

    @Test
    public void testLockingAlreadyLockedDevice() throws Exception {
        assumeNotNull(testDevice);
        assertLockDevice();
        assertLockDevice();
        assertValidatorIsNotStarted("The validation element should not be available when the device is locked.");
    }

    @Test
    public void testUnlockingAlreadyUnlockedDevice() throws Exception {
        assumeNotNull(testDevice);
        assertLockDevice();

        assertUnlockDevice();
        assertValidatorIsStarted("The validator was not avaible after unlocking the device.");
        assertUnlockDevice();
        assertValidatorIsStarted("The validator was not avaible after unlocking the device.");
    }

    private void waitForSetLocked(int timeout) throws InterruptedException {
        int counter = 0;
        while (counter < WAIT_FOR_LOCK_TIMEOUT && !testDevice.isLocked()) {
            Thread.sleep(WAIT_FOR_LOCK_INTERVAL);
            counter += WAIT_FOR_LOCK_INTERVAL;
        }
    }

    private void assertLockDevice() throws Exception {
        assertTrue("The device was not locked.", testDevice.lock());
        waitForSetLocked(WAIT_FOR_LOCK_TIMEOUT);

        assertTrue("Device was not successfuly locked.", testDevice.isLocked());
    }

    private void assertUnlockDevice() {
        assertTrue("Unlocking the device returned false.", testDevice.unlock());

        assertFalse("Device was not successfuly unlocked.", testDevice.isLocked());
        assertTrue("Device is awake after unlock.", testDevice.isAwake());
    }
}