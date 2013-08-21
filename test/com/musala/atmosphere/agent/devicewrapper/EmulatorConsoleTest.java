package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.commons.BatteryState;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.commons.sa.DeviceParameters;
import com.musala.atmosphere.testsuites.AtmosphereFailingIntegrationTestsSuite;
import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;

public class EmulatorConsoleTest extends BaseIntegrationTest
{
	private static AgentIntegrationEnvironmentCreator agentEnvironment = AtmosphereIntegrationTestsSuite.getAgentIntegrationEnvironmentCreator();

	private static final int EMULATOR_CREATION_DPI = 120;

	private static final int EMULATOR_CREATION_RAM = 256;

	private static final int EMULATOR_CREATION_RESOLUTION_H = 240;

	private static final int EMULATOR_CREATION_RESOLUTION_W = 360;

	@BeforeClass
	public static void setUp() throws Exception
	{
		if (!agentEnvironment.isAnyEmulatorPresent())
		{
			DeviceParameters emulatorCreationParameters = new DeviceParameters();
			emulatorCreationParameters.setDpi(EMULATOR_CREATION_DPI);
			emulatorCreationParameters.setRam(EMULATOR_CREATION_RAM);
			emulatorCreationParameters.setResolution(new Pair<Integer, Integer>(EMULATOR_CREATION_RESOLUTION_H,
																				EMULATOR_CREATION_RESOLUTION_W));
			AtmosphereFailingIntegrationTestsSuite.createAndPublishEmulator(emulatorCreationParameters);
		}

		com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters emulatorInformation = new com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters();
		emulatorInformation.setDeviceType(DeviceType.EMULATOR_ONLY);
		initTestDevice(emulatorInformation);
	}

	@Test
	public void testSetBatteryLevel() throws Exception
	{
		final int initialBatteryLevel = 20;
		final int batteryLevel = 80;

		testDevice.setBatteryLevel(initialBatteryLevel);
		int newBatteryLevel = testDevice.getBatteryLevel();
		assertEquals("Battery level doesn't match.", initialBatteryLevel, newBatteryLevel);

		testDevice.setBatteryLevel(batteryLevel);
		int newBatteryLevel2 = testDevice.getBatteryLevel();
		assertEquals("Battery level doesn't match.", batteryLevel, newBatteryLevel2);
	}

	@Test
	public void testSetBatteryState()
	{
		BatteryState batteryState = BatteryState.NOT_CHARGING;
		testDevice.setBatteryState(batteryState);
		BatteryState actualBatteryState = testDevice.getBatteryState();
		assertEquals("Failed setting battery state.", batteryState, actualBatteryState);
	}

	@Test
	public void testSetPowerState() throws Exception
	{
		boolean state = false; // Connected.
		testDevice.setPowerState(state);
		boolean newState = testDevice.getPowerState();
		assertEquals("Power state doesn't match.", state, newState);
	}
}
