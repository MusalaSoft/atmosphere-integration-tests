package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.sa.exceptions.NoAvailableDeviceFoundException;

/**
 * 
 * @author denis.bialev
 * 
 */
public class GetRunningTaskIdsTest extends BaseIntegrationTest {

    private static final int MAX_RUNNING_TASKS_COUNT = 2;

    private static final int WAIT_FOR_TASK_UPDATE_TIMEOUT = 500;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);
        testDeviceParams.setMaxApiLevel(19);
        try {
            initTestDevice(testDeviceParams);
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
    public void testGetRunningTaskIds() throws Exception {
        assumeNotNull(testDevice);
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