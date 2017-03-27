package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
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

public class RuntimePermissionTest extends BaseIntegrationTest {

    private static final String ONDEVICE_VALIDATOR_PACKAGE = "com.musala.atmosphere.ondevice.validator";

    private static final String RECEIVE_SMS_PERMISSION = "android.permission.RECEIVE_SMS";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED).minApi(23);
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
            testDevice.grantApplicationPermission(ONDEVICE_VALIDATOR_PACKAGE, RECEIVE_SMS_PERMISSION);
        }
        releaseDevice();
    }

    @Test
    public void testRevokeRuntimePermission() throws Exception {
        assumeNotNull(testDevice);
        assertTrue("Failed to revoke runtime permission " + RECEIVE_SMS_PERMISSION,
                   testDevice.revokeApplicationPermission(ONDEVICE_VALIDATOR_PACKAGE, RECEIVE_SMS_PERMISSION));
    }

    @Test
    public void testGrantRuntimePermission() throws Exception {
        assumeNotNull(testDevice);
        assertTrue("Failed to grant runtime permission " + RECEIVE_SMS_PERMISSION,
                   testDevice.grantApplicationPermission(ONDEVICE_VALIDATOR_PACKAGE, RECEIVE_SMS_PERMISSION));
    }
}
