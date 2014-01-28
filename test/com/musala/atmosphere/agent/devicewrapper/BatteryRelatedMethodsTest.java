package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryLevel;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryNotLow;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryState;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertNotPowerConnected;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertPowerConnected;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.BatteryState;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

/**
 * 
 * @author valyo.yolovski
 * 
 */
public class BatteryRelatedMethodsTest extends BaseIntegrationTest
{
	private static final String BATTERY_STATUS_MESSAGE = "Battery status not set to the expected value.";

	@BeforeClass
	public static void setUp() throws Exception
	{
		initTestDevice(new DeviceParameters());
		installValidatorApplication();

		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(2000);
	}

	@Test
	public void testSetBatteryLevel() throws Exception
	{
		// set battery level to 75
		final Integer batteryLevel = 75;
		assertTrue("Setting battery level returned false.", testDevice.setBatteryLevel(batteryLevel));

		Thread.sleep(2000);
		assertBatteryNotLow("Battery low flag not set as expected.");
		assertBatteryLevel("Battery level is not as expected.", batteryLevel);
		assertEquals("Battery level is not as expected.", batteryLevel, testDevice.getBatteryLevel());
	}

	@Test
	public void testSetBatteryState() throws Exception
	{
		BatteryState batteryState;

		// battery state unknown
		batteryState = BatteryState.UNKNOWN;
		assertTrue("Setting battery state returned false.", testDevice.setBatteryState(batteryState));
		// Sleep required for proper UI refreshing
		Thread.sleep(2000);
		assertBatteryState(BATTERY_STATUS_MESSAGE, batteryState);

		// battery state charging
		batteryState = BatteryState.CHARGING;
		assertTrue("Setting battery state returned false.", testDevice.setBatteryState(batteryState));
		// Sleep required for proper UI refreshing
		Thread.sleep(2000);
		assertBatteryState(BATTERY_STATUS_MESSAGE, batteryState);

		// battery state discharging
		batteryState = BatteryState.DISCHARGING;
		assertTrue("Setting battery state returned false.", testDevice.setBatteryState(batteryState));
		// Sleep required for proper UI refreshing
		Thread.sleep(2000);
		assertBatteryState(BATTERY_STATUS_MESSAGE, batteryState);

		// battery state not_charging
		batteryState = BatteryState.NOT_CHARGING;
		assertTrue("Setting battery state returned false.", testDevice.setBatteryState(batteryState));
		Thread.sleep(2000);
		assertBatteryState(BATTERY_STATUS_MESSAGE, batteryState);

		// battery state full
		batteryState = BatteryState.FULL;
		assertTrue("Setting battery state returned false.", testDevice.setBatteryState(batteryState));
		// Sleep required for proper UI refreshing
		Thread.sleep(2000);
		assertBatteryState(BATTERY_STATUS_MESSAGE, batteryState);
	}

	@Test
	public void testSetPowerState() throws Exception
	{
		// set device power connection off
		assertTrue("Setting power state returned false.", testDevice.setPowerState(false));
		Thread.sleep(1000);
		assertNotPowerConnected("Power state not set to the expected value.");
		assertFalse("Power state not set to the expected value", testDevice.getPowerState());

		// set device power connection on
		assertTrue("Setting power state returned false.", testDevice.setPowerState(true));
		Thread.sleep(1000);
		assertPowerConnected("Power state not set to the expected value.");
		assertTrue("Power state not set to the expected value.", testDevice.getPowerState());
	}

	@Test
	public void testGetAndSetBatteryState() throws Exception
	{
		for (BatteryState batteryState : BatteryState.values())
		{
			assertTrue("Setting battery state returned false.", testDevice.setBatteryState(batteryState));
			Thread.sleep(2000);
			assertEquals(	"The returned battery state value did not match the one thata was set.",
							batteryState,
							testDevice.getBatteryState());
		}
	}
}
