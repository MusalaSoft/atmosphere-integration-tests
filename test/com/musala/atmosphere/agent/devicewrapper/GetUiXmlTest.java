package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.commons.CommandFailedException;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.sa.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;

public class GetUiXmlTest
{
	private static final int RMI_PORT = 1989;

	private AgentIntegrationEnvironmentCreator environment;

	private static final Logger LOGGER = Logger.getLogger(GetUiXmlTest.class.getName());

	private static final int EMULATOR_CREATION_DPI = 120;

	private static final int EMULATOR_CREATION_RAM = 256;

	private static final int EMULATOR_CREATION_RESOLUTION_H = 240;

	private static final int EMULATOR_CREATION_RESOLUTION_W = 360;

	@Before
	public void setUp() throws Exception
	{
		// TODO configure logger properly
		LOGGER.setLevel(Level.ALL);
		LOGGER.addHandler(new ConsoleHandler());

		environment = new AgentIntegrationEnvironmentCreator(RMI_PORT);

		if (environment.isAnyDevicePresent() == false)
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
	public void getUiXmlTest() throws RemoteException, CommandFailedException, NotBoundException
	{
		IWrapDevice deviceWrapper = environment.getFirstAvailableDeviceWrapper();
		environment.waitForDeviceOsToStart(deviceWrapper);

		String uiXmlDump = deviceWrapper.getUiXml();

		LOGGER.log(Level.INFO, "UI xml dump returned :\n" + uiXmlDump);
		assertNotNull("ui xml dump response can never be 'null'.", uiXmlDump);
		assertTrue(	"ui xml dump must start with a <hierarchy ..> tag.",
					uiXmlDump.startsWith("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><hierarchy"));
		assertTrue(	"ui xml dump must end with a closing tag for the <hierarchy ..> tag.",
					uiXmlDump.endsWith("</hierarchy>"));
	}

}
