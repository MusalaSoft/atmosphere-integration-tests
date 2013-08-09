package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.commons.CommandFailedException;
import com.musala.atmosphere.commons.DeviceInformation;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.sa.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;

public class GetScreenshotTest
{
	private static final int AGENT_RMI_PORT = 1989;

	private AgentIntegrationEnvironmentCreator environment;

	private static final int EMULATOR_CREATION_DPI = 120;

	private static final int EMULATOR_CREATION_RAM = 256;

	private static final int EMULATOR_CREATION_RESOLUTION_H = 240;

	private static final int EMULATOR_CREATION_RESOLUTION_W = 360;

	@Before
	public void setUp() throws Exception
	{

		environment = new AgentIntegrationEnvironmentCreator(AGENT_RMI_PORT);

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
	public void testGetScreenshot()
		throws IllegalStateException,
			NotBoundException,
			CommandFailedException,
			IOException
	{
		IWrapDevice deviceWrapper = environment.getFirstAvailableDeviceWrapper();
		environment.waitForDeviceOsToStart(deviceWrapper);

		byte[] screenshot = deviceWrapper.getScreenshot();
		DeviceInformation deviceInformation = deviceWrapper.getDeviceInformation();
		Pair<Integer, Integer> screenResolution = deviceInformation.getResolution();

		/*
		 * FileOutputStream output = new FileOutputStream(new File(saveFilename)); output.write(screenshot);
		 * output.close();
		 */

		InputStream imageInput = new ByteArrayInputStream(screenshot);
		BufferedImage image = ImageIO.read(imageInput);

		assertNotNull("Screenshot data is not a valid image.", image);

		assertEquals(	"Device screen resolution height did not match screenshot height.",
						(int) screenResolution.getKey(),
						image.getHeight());
		assertEquals(	"Device screen resolution width did not match screenshot width.",
						(int) screenResolution.getValue(),
						image.getWidth());
	}

}
