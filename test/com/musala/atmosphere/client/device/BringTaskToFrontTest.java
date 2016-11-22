package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;

/**
 *
 * @author denis.bialev
 *
 */
public class BringTaskToFrontTest extends BaseIntegrationTest {

    private static final int MAX_RUNNING_TASKS_COUNT = 2;

    private static final int WAIT_FOR_TASK_TIMEOUT = 1000;

    private static final int EXPECTED_TASK_POSITION = 1;

    private static final int BRING_TASK_TO_FRONT_TIMEOUT = 5000;

    private static final int INVALID_TASK_ID = -1;

    private static final int INVALID_TASK_TIMEOUT = 100;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED)
                                                                           .maxApi(19);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        try {
            initTestDevice(testDeviceSelector);
            setTestDevice(testDevice);
        } catch (NoAvailableDeviceFoundException e) {
            // Nothing to do here
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
    public void testBringTaskToFront() throws Exception {
        assumeNotNull(testDevice);
        startMainActivity();
        int[] runningTasksIds = testDevice.getRunningTaskIds(MAX_RUNNING_TASKS_COUNT);
        int currentTopTaskId = runningTasksIds[0];
        testDevice.pressButton(HardwareButton.HOME);
        testDevice.waitForTasksUpdate(currentTopTaskId, EXPECTED_TASK_POSITION, WAIT_FOR_TASK_TIMEOUT);
        assertTrue("The given taskId is not present on the foreground.",
                   testDevice.bringTaskToFront(currentTopTaskId, BRING_TASK_TO_FRONT_TIMEOUT));

        int expectedTopTaskId = testDevice.getRunningTaskIds(MAX_RUNNING_TASKS_COUNT)[0];
        assertEquals("The given task hasn't been successfully brought to the foreground.",
                     currentTopTaskId,
                     expectedTopTaskId);
    }

    @Test
    public void testBringTaskToFrontInvalidTaskId() throws Exception {
        assumeNotNull(testDevice);
        assertFalse("Bring task to front returned true with given invalid task Id.",
                    testDevice.bringTaskToFront(INVALID_TASK_ID, INVALID_TASK_TIMEOUT));
    }

    @Test
    public void testBringTaskToFrontWhenOnFront() throws Exception {
        assumeNotNull(testDevice);
        startMainActivity();
        int[] runningTasksIds = testDevice.getRunningTaskIds(MAX_RUNNING_TASKS_COUNT);
        int currentTopTaskId = runningTasksIds[0];
        assertTrue("The given taskId is not present on the foreground.",
                   testDevice.bringTaskToFront(currentTopTaskId, BRING_TASK_TO_FRONT_TIMEOUT));

        int expectedTopTaskId = testDevice.getRunningTaskIds(MAX_RUNNING_TASKS_COUNT)[0];
        assertEquals("The given task hasn't been successfully brought to the foreground.",
                     currentTopTaskId,
                     expectedTopTaskId);
    }
}
