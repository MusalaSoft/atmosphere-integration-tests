package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.commons.CommandFailedException;
import com.musala.atmosphere.commons.DeviceInformation;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.sa.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;

public class EmulatorConsoleTest
{
	private static final int RMI_PORT = 1989;

	private AgentIntegrationEnvironmentCreator environment;

	private static final int EMULATOR_CREATION_DPI = 120;

	private static final int EMULATOR_CREATION_RAM = 256;

	private static final int EMULATOR_CREATION_RESOLUTION_H = 240;

	private static final int EMULATOR_CREATION_RESOLUTION_W = 360;

	@Before
	public void setUp() throws Exception
	{
		environment = new AgentIntegrationEnvironmentCreator(RMI_PORT);

		if (environment.isAnyEmulatorPresent() == false)
		{
			DeviceParameters emulatorCreationParameters = new DeviceParameters();
			emulatorCreationParameters.setDpi(EMULATOR_CREATION_DPI);
			emulatorCreationParameters.setRam(EMULATOR_CREATION_RAM);
			emulatorCreationParameters.setResolution(new Pair<Integer, Integer>(EMULATOR_CREATION_RESOLUTION_H,
																				EMULATOR_CREATION_RESOLUTION_W));

			environment.startEmulator(emulatorCreationParameters);
		}
	}

	@After
	public void tearDown() throws Exception
	{
		environment.close();
	}

	@Test
	public void testSetNetworkSpeed()
	{
		// TODO method setNetworkSpeed() is not implemented for devices
		fail("Not yet implemented");
	}

	@Test
	public void testSetBatteryLevel()
		throws RemoteException,
			IllegalStateException,
			NotBoundException,
			CommandFailedException,
			InterruptedException
	{
		final int initialBatteryLevel = 20;
		final int batteryLevel = 80;

		IWrapDevice deviceWrapper = environment.getFirstAvailableEmulatorDeviceWrapper();
		environment.waitForDeviceOsToStart(deviceWrapper);

		DeviceInformation deviceInformation = deviceWrapper.getDeviceInformation();
		assertTrue("Device must be an emulator.", deviceInformation.isEmulator());

		deviceWrapper.setBatteryLevel(initialBatteryLevel);
		int newBatteryLevel = deviceWrapper.getBatteryLevel();
		assertEquals("Battery level doesn't match.", initialBatteryLevel, newBatteryLevel);

		deviceWrapper.setBatteryLevel(batteryLevel);
		int newBatteryLevel2 = deviceWrapper.getBatteryLevel();
		assertEquals("Battery level doesn't match.", batteryLevel, newBatteryLevel2);
	}

	@Test
	public void testSetNetworkLatency()
	{
		// TODO method setNetworkLatency() is not implemented for device
		fail("Not yet implemented");
	}

	@Test
	public void testSetBatteryState()
	{
		// TODO method setBatteryState() is not implemented for device
		fail("Not yet implemented");
	}

	@Test
	public void testSetPowerState()
		throws RemoteException,
			CommandFailedException,
			IllegalStateException,
			NotBoundException
	{
		IWrapDevice deviceWrapper = environment.getFirstAvailableEmulatorDeviceWrapper();
		environment.waitForDeviceOsToStart(deviceWrapper);

		boolean state = true; // connected

		DeviceInformation deviceInformation = deviceWrapper.getDeviceInformation();

		deviceWrapper.setPowerState(state);
		boolean newState = deviceWrapper.getPowerState();
		assertEquals("Power state doesn't match.", state, newState);

		deviceWrapper.setPowerState(state);
		boolean newState2 = deviceWrapper.getPowerState();
		assertEquals("PowerState doesn't match.", state, newState2);
	}
}
