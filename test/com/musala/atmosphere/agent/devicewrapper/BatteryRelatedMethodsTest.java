package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryLevel;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryNotLow;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryState;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertNotPowerConnected;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertPowerConnected;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.PowerProperties;
import com.musala.atmosphere.commons.beans.BatteryLevel;
import com.musala.atmosphere.commons.beans.BatteryState;
import com.musala.atmosphere.commons.beans.PowerSource;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

/**
 * 
 * @author valyo.yolovski
 * 
 */
public class BatteryRelatedMethodsTest extends BaseIntegrationTest {
    private static final String BATTERY_STATUS_MESSAGE = "Battery status not set to the expected value.";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);

        setTestDevice(testDevice);

        startMainActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testSetBatteryLevel() throws Exception {
        // set battery level to 75
        final int batteryLevel = 75;
        PowerProperties properties = new PowerProperties();
        properties.setBatteryLevel(new BatteryLevel(batteryLevel));
        assertTrue("Setting battery level returned false.", testDevice.setPowerProperties(properties));

        Thread.sleep(2000);
        assertBatteryNotLow("Battery low flag not set as expected.");
        assertBatteryLevel("Battery level is not as expected.", batteryLevel);
        assertEquals("Battery level is not as expected.", batteryLevel, (testDevice.getPowerProperties()
                                                                                   .getBatteryLevel().getLevel()));
    }

    @Test
    public void testSetBatteryState() throws Exception {
        PowerProperties properties = new PowerProperties();

        // battery state unknown
        BatteryState batteryState = BatteryState.UNKNOWN;
        properties.setBatteryState(batteryState);
        assertTrue("Setting battery state returned false.", testDevice.setPowerProperties(properties));
        // Sleep required for proper UI refreshing
        Thread.sleep(2000);
        assertBatteryState(BATTERY_STATUS_MESSAGE, batteryState);

        // battery state charging
        batteryState = BatteryState.CHARGING;
        properties.setBatteryState(batteryState);
        assertTrue("Setting battery state returned false.", testDevice.setPowerProperties(properties));
        // Sleep required for proper UI refreshing
        Thread.sleep(2000);
        assertBatteryState(BATTERY_STATUS_MESSAGE, batteryState);

        // battery state discharging
        batteryState = BatteryState.DISCHARGING;
        properties.setBatteryState(batteryState);
        assertTrue("Setting battery state returned false.", testDevice.setPowerProperties(properties));
        // Sleep required for proper UI refreshing
        Thread.sleep(2000);
        assertBatteryState(BATTERY_STATUS_MESSAGE, batteryState);

        // battery state not_charging
        batteryState = BatteryState.NOT_CHARGING;
        properties.setBatteryState(batteryState);
        assertTrue("Setting battery state returned false.", testDevice.setPowerProperties(properties));
        Thread.sleep(2000);
        assertBatteryState(BATTERY_STATUS_MESSAGE, batteryState);

        // battery state full
        batteryState = BatteryState.FULL;
        properties.setBatteryState(batteryState);
        assertTrue("Setting battery state returned false.", testDevice.setPowerProperties(properties));
        // Sleep required for proper UI refreshing
        Thread.sleep(2000);
        assertBatteryState(BATTERY_STATUS_MESSAGE, batteryState);
    }

    @Test
    public void testSetPowerState() throws Exception {
        // set device power connection off
        PowerProperties properties = new PowerProperties();
        properties.setPowerSource(PowerSource.UNPLUGGED);
        assertTrue("Setting power state returned false.", testDevice.setPowerProperties(properties));
        Thread.sleep(1000);
        assertNotPowerConnected("Power state not set to the expected value.");
        assertEquals("Power state not set to the expected value",
                     testDevice.getPowerProperties().getPowerSource(),
                     PowerSource.UNPLUGGED);

        // set device power connection on
        properties.setPowerSource(PowerSource.PLUGGED_AC);
        assertTrue("Setting power state returned false.", testDevice.setPowerProperties(properties));
        Thread.sleep(1000);
        assertPowerConnected("Power state not set to the expected value.");
        assertEquals("Power state not set to the expected value.",
                     testDevice.getPowerProperties().getPowerSource(),
                     PowerSource.PLUGGED_AC);
    }

    // @Test
    public void testGetAndSetBatteryState() throws Exception {
        for (BatteryState batteryState : BatteryState.values()) {
            PowerProperties properties = new PowerProperties();
            properties.setBatteryState(batteryState);
            assertTrue("Setting battery state returned false.", testDevice.setPowerProperties(properties));
            Thread.sleep(2000);
            assertEquals("The returned battery state value did not match the one thata was set.",
                         batteryState,
                         testDevice.getPowerProperties().getBatteryState());
        }
    }
}
