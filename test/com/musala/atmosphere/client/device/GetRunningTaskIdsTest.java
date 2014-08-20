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
public class GetRunningTaskIdsTest extends BaseIntegrationTest {

    private static final int MAX_RUNNING_TASKS_COUNT = 2;

    private static final int WAIT_FOR_TASK_TIMEOUT = 1000;

    private static final int TASK_POSITION = 1;

    private static final int WAIT_FOR_TASK_UPDATE_TIMEOUT = 500;

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
    public void testGetRunningTaskIds() throws Exception {
        startMainActivity();
        int[] currentRunningTasksIds = testDevice.getRunningTaskIds(MAX_RUNNING_TASKS_COUNT);
        int monitoredTaskId = currentRunningTasksIds[0];
        testDevice.pressButton(HardwareButton.HOME);
        Thread.sleep(WAIT_FOR_TASK_UPDATE_TIMEOUT);

        currentRunningTasksIds = testDevice.getRunningTaskIds(MAX_RUNNING_TASKS_COUNT);
        int expectedTaskId = currentRunningTasksIds[1];
        assertEquals("The running task Id is different than expected. ", monitoredTaskId, expectedTaskId);
    }
}