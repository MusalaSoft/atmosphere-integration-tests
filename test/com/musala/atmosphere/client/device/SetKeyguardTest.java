package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.commons.sa.exceptions.NoAvailableDeviceFoundException;

/**
 * 
 * @author denis.bialev
 * 
 */
public class SetKeyguardTest extends BaseIntegrationTest {

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_ONLY);
        try {
            initTestDevice(testDeviceParams);
        } catch (NoAvailableDeviceFoundException e) {
        }

        assumeNotNull(testDevice);

        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void testSettingKeyguard() throws Exception {
        testDevice.setLocked(true);
        testDevice.setKeyguard(false);
        assertFalse("The keyguard is present when it should be removed.", testDevice.isLocked());
        testDevice.setKeyguard(true);
        testDevice.setLocked(true);
        assertTrue("The Keyguard is not present when is re enabled.", testDevice.isLocked());
    }
}
