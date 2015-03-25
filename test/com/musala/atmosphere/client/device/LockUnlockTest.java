package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsNotStarted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

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
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        initTestDevice(testDeviceParams);

        setTestDevice(testDevice);
    }

    @Before
    public void setUpTest() throws Exception {
        startMainActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testSetLockedUnlockingDeviceWhenItIsInAsleepState() throws Exception {
        if (testDevice.isAwake()) {
            testDevice.pressButton(HardwareButton.POWER);
        }

        waitForSetLocked(WAIT_FOR_LOCK_TIMEOUT);
        assertUnlockDevice();
    }

    @Test
    public void testSetLockedLockUnlockWithStartedApplication() throws Exception {
        assertLockDevice();
        assertValidatorIsNotStarted("The validation element should not be available when the device is locked.");

        assertUnlockDevice();
        assertValidatorIsStarted();
    }

    @Test
    public void testSetLockedUnlockAndLockDevice() throws Exception {
        assertLockDevice();
        assertValidatorIsNotStarted("The validation element should not be available when the device is locked.");

        assertUnlockDevice();
        assertValidatorIsStarted("The validator was not avaible after unlocking the device.");

        assertLockDevice();
        assertFalse("Device shouldn't be awake after it has been locked.", testDevice.isAwake());
        assertValidatorIsNotStarted("The validation element should not be available when the device is locked.");
    }

    @Test
    public void testSetLockedLockingAlreadyLockedDevice() throws Exception {
        assertLockDevice();
        assertLockDevice();
        assertValidatorIsNotStarted("The validation element should not be available when the device is locked.");
    }

    @Test
    public void testSetLockedUnlockingAlreadyUnlockedDevice() throws Exception {
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
        assertTrue("The device was not locked.", testDevice.setLocked(true));
        waitForSetLocked(WAIT_FOR_LOCK_TIMEOUT);

        assertTrue("Device was not successfuly locked.", testDevice.isLocked());
    }

    private void assertUnlockDevice() {
        assertTrue("Unlocking the device returned false.", testDevice.setLocked(false));

        assertFalse("Device was not successfuly unlocked.", testDevice.isLocked());
        assertTrue("Device is awake after unlock.", testDevice.isAwake());
    }
}