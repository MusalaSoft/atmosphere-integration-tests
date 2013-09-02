package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInAirplaneMode;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertNotInAirplaneMode;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class AirplaneModeTest extends BaseIntegrationTest
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		initTestDevice(new DeviceParameters());
		installValidatorApp();
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY, true);
		Thread.sleep(1000);
	}

	@Test
	public void testSetAirplaneModeOn() throws Exception
	{
		// set airplane mode on
		testDevice.setAirplaneMode(true);
		assertInAirplaneMode("Airplane mode not set to the expected value.");
	}

	@Test
	public void testSetAirplaneModeOff() throws Exception
	{

		// set airplane mode off
		testDevice.setAirplaneMode(false);
		assertNotInAirplaneMode("Airplane mode not set to the expected value.");
	}
}
