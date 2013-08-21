package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryNotLow;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryState;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertNotPowerConnected;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.BatteryState;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

/**
 * 
 * @author valyo.yolovski
 * 
 */
public class BatteryRelatedMethodsTest extends BaseIntegrationTest
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		initTestDevice(new DeviceParameters());
		installValidatorApp();
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(2000);
	}

	@Test
	public void testSetBatteryLevel() throws Exception
	{
		// set battery level to 75
		final int batteryLevel = 75;
		testDevice.setBatteryLevel(batteryLevel);
		assertBatteryNotLow("Battery low flag not set as expected.");
	}

	@Test
	public void testSetBatteryState() throws Exception
	{
		BatteryState batteryState;

		// set battery state unknown
		batteryState = BatteryState.UNKNOWN;
		testDevice.setBatteryState(batteryState);
		assertBatteryState("Battery status not set to the expected value.", BatteryState.UNKNOWN);

		// set battery state charging
		batteryState = BatteryState.CHARGING;
		testDevice.setBatteryState(batteryState);
		assertBatteryState("Battery status not set to the expected value.", BatteryState.CHARGING);

		// set battery state discharging
		batteryState = BatteryState.DISCHARGING;
		testDevice.setBatteryState(batteryState);
		assertBatteryState("Battery status not set to the expected value.", BatteryState.DISCHARGING);

		// set battery state not_charging
		batteryState = BatteryState.NOT_CHARGING;
		testDevice.setBatteryState(batteryState);
		assertBatteryState("Battery status not set to the expected value.", BatteryState.NOT_CHARGING);

		// set battery state full
		batteryState = BatteryState.FULL;
		testDevice.setBatteryState(batteryState);
		assertBatteryState("Battery status not set to the expected value.", BatteryState.FULL);
	}

	@Test
	public void testSetPowerState() throws Exception
	{
		// set device power connection off
		boolean powerState = false;
		testDevice.setPowerState(powerState);
		Thread.sleep(1000);
		assertNotPowerConnected("Power state not set to the expected value.");

		// set device power connection on
		// powerState = true;
		// testDevice.setPowerState(powerState);
		// assertPowerConnected("Power state not set to the expected value.");
	}
}
