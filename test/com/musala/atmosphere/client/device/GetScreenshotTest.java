package com.musala.atmosphere.client.device;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;

public class GetScreenshotTest
{
	private static final Logger LOGGER = Logger.getLogger(GetScreenshotTest.class.getCanonicalName());

	private final static int POOLMANAGER_RMI_PORT = 2200;

	private final static int AGENTMANAGER_RMI_PORT = 2300;

	private static final String PATH_TO_SCREENSHOT = "./Screenshot.png";

	/**
	 * {@value #WIDGET_DRAWING_TIMEOUT} milliseconds
	 */
	private static final long WIDGET_DRAWING_TIMEOUT = 2000;

	private AgentIntegrationEnvironmentCreator agentEnvironment;

	private ServerIntegrationEnvironmentCreator serverEnvironment;

	@Server(ip = "localhost", port = POOLMANAGER_RMI_PORT)
	class DeviceTestClass
	{
		public DeviceTestClass()
		{
		}

		public Device getSelectedDevice(DeviceParameters parameters)
		{
			return Builder.getInstance().getDevice(parameters);
		}
	}

	@Before
	public void setUp() throws Exception
	{
		// TODO extract emulator creation logic in separate utility class
		agentEnvironment = new AgentIntegrationEnvironmentCreator(AGENTMANAGER_RMI_PORT);
		serverEnvironment = new ServerIntegrationEnvironmentCreator(POOLMANAGER_RMI_PORT);
		agentEnvironment.connectToLocalhostServer(POOLMANAGER_RMI_PORT);
		String agentId = agentEnvironment.getUnderlyingAgentId();
		serverEnvironment.waitForAgentConnection(agentId);

		if (!agentEnvironment.isAnyEmulatorPresent())
		{
			com.musala.atmosphere.commons.sa.DeviceParameters emulatorCreationParameters = new com.musala.atmosphere.commons.sa.DeviceParameters();
			emulatorCreationParameters.setResolution(new Pair<Integer, Integer>(120, 160));
			emulatorCreationParameters.setRam(1024);
			emulatorCreationParameters.setDpi(96);

			IWrapDevice createdEmulatorWrapper = agentEnvironment.startEmulator(emulatorCreationParameters);
			// agentEnvironment.waitForDeviceOsToStart(createdEmulatorWrapper);
		}
	}

	@After
	public void tearDown() throws Exception
	{
		serverEnvironment.close();
		agentEnvironment.close();
	}

	@Test
	public void getScreenShotTest()
	{
		DeviceParameters selectionParameters = new DeviceParameters();
		selectionParameters.setDeviceType(DeviceType.EMULATOR_ONLY);
		DeviceTestClass testClass = new DeviceTestClass();
		Device testDevice = testClass.getSelectedDevice(selectionParameters);

		waitForDeviceToBeLoaded(testDevice);

		// getting screenshot without dumping it to file
		byte[] screenshot = testDevice.getScreenshot();
		assertTrue("Getting screenshot returned 'null'", (screenshot != null));

		// getting screenshot with dumping it to file
		testDevice.getScreenshot(PATH_TO_SCREENSHOT);
		File dumpedScreenshot = new File(PATH_TO_SCREENSHOT);
		assertTrue("Screenshot is not dumped as image!", dumpedScreenshot.exists());
	}

	// waits for the Android to be loaded by checking if there are any clickable elements
	// FIXME this should be extracted to a utility class
	private static void waitForDeviceToBeLoaded(Device testDevice)
	{
		// FIXME Extract to config file.
		final int MAX_TRY_COUNT = 123456;

		for (int numberOfTry = 1; numberOfTry <= MAX_TRY_COUNT; numberOfTry++)
		{
			try
			{
				Screen deviceScreen = testDevice.getActiveScreen();
				UiElement someClickableElement = deviceScreen.getElementXPath("//*[(@clickable='true' or @long-clickable='true') and @enabled='true']");
				// if no screen is fetched or no clickable UI element is present, we are thrown an exception and assume
				// the emulator has not loaded; otherwise we assume the emulator OS has loaded
				break;
			}
			catch (Throwable throwable)
			{
			}
		}
		// just in case we wait some time for drawing the UI elements after everything is loaded
		try
		{
			Thread.sleep(WIDGET_DRAWING_TIMEOUT);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
