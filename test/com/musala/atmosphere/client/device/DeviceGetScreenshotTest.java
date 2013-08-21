package com.musala.atmosphere.client.device;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class DeviceGetScreenshotTest extends BaseIntegrationTest
{
	private static final String PATH_TO_SCREENSHOT = "./Screenshot.png";

	/**
	 * {@value #WIDGET_DRAWING_TIMEOUT} milliseconds
	 */
	private static final long WIDGET_DRAWING_TIMEOUT = 2000;

	@Before
	public void setUp()
	{
		initTestDevice(new DeviceParameters());
	}

	@Test
	public void getScreenShotTest()
	{
		testDevice.unlock();
		// getting screenshot without dumping it to file
		byte[] screenshot = testDevice.getScreenshot();
		assertNotNull("Getting screenshot returned 'null'", screenshot);

		// getting screenshot with dumping it to file
		testDevice.getScreenshot(PATH_TO_SCREENSHOT);
		File dumpedScreenshot = new File(PATH_TO_SCREENSHOT);
		assertTrue("Getting screenshot failed!", dumpedScreenshot.exists());
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
			catch (Throwable e)
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
