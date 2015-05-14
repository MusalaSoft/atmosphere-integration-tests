package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
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
public class SetKeyguardTest extends BaseIntegrationTest {

    private static final int WAIT_FOR_LOCK_TIMEOUT = 2000;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY)
                                                                           .maxApi(19);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        try {
            initTestDevice(testDeviceSelector);
        } catch (NoAvailableDeviceFoundException e) {
        }

        assumeNotNull(testDevice);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Test
    public void testSettingKeyguard() throws Exception {
        testDevice.setLocked(true);
        Thread.sleep(WAIT_FOR_LOCK_TIMEOUT);
        testDevice.setKeyguard(false);
        assertFalse("The keyguard is present when it should be removed.", testDevice.isLocked());
        testDevice.setKeyguard(true);
        testDevice.setLocked(true);
        Thread.sleep(WAIT_FOR_LOCK_TIMEOUT);
        assertTrue("The Keyguard is not present when is re-enabled.", testDevice.isLocked());
    }
}
