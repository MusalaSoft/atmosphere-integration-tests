package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertEquals;
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
 * @author denis.bialev
 * 
 */
public class WaitForTaskUpdateTest extends BaseIntegrationTest {

    private static final int MAX_RUNNING_TASKS_COUNT = 2;

    private static final int WAIT_FOR_TASK_TIMEOUT = 1000;

    private static final int TASK_POSITION = 1;

    private static final int NONEXISTENT_TASK_ID = -1;

    private static final int WAIT_FOR_NONEXISTENT_TASK_TIMEOUT = 500;

    private static final String ANDROID_SETTINGS_PACKAGE = "com.android.settings";

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
    public void testWaitForTaskUpdate() throws Exception {
        startMainActivity();
        int[] currentlyRunningTasksIds = testDevice.getRunningTaskIds(MAX_RUNNING_TASKS_COUNT);
        int monitoredTaskId = currentlyRunningTasksIds[0];
        testDevice.pressButton(HardwareButton.HOME);
        assertTrue("The given task hasn't been updated and the timeout has run out.",
                   testDevice.waitForTasksUpdate(monitoredTaskId, TASK_POSITION, WAIT_FOR_TASK_TIMEOUT));

        currentlyRunningTasksIds = testDevice.getRunningTaskIds(MAX_RUNNING_TASKS_COUNT);
        int expectedTaskId = currentlyRunningTasksIds[1];
        assertEquals("Position of the task should've been updated", monitoredTaskId, expectedTaskId);
    }

    @Test
    public void testWaitForTaskUpdateInvalidId() throws Exception {
        assertFalse("Wait for task update succeeded when given invalid task ID. ",
                    testDevice.waitForTasksUpdate(NONEXISTENT_TASK_ID, TASK_POSITION, WAIT_FOR_NONEXISTENT_TASK_TIMEOUT));
    }

    @Test
    public void testWaitForTaskUpdateNoChanges() throws Exception {
        startMainActivity();
        int[] runningTasksIds = testDevice.getRunningTaskIds(MAX_RUNNING_TASKS_COUNT);
        int monitoredTaskId = runningTasksIds[0];
        assertFalse("The given task changed its position with no changes occurring.",
                    testDevice.waitForTasksUpdate(monitoredTaskId, TASK_POSITION, WAIT_FOR_TASK_TIMEOUT));
    }

    @Test
    public void testWaitForTaskUpdateWrongPosition() throws Exception {
        startMainActivity();
        int[] runningTasksIds = testDevice.getRunningTaskIds(MAX_RUNNING_TASKS_COUNT);
        int monitoredTaskId = runningTasksIds[0];
        testDevice.startApplication(ANDROID_SETTINGS_PACKAGE);
        testDevice.pressButton(HardwareButton.HOME);

        assertFalse("The given task return true when given wrong position.",
                    testDevice.waitForTasksUpdate(monitoredTaskId, TASK_POSITION, WAIT_FOR_TASK_TIMEOUT));

    }
}
