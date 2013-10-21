package com.musala.atmosphere.client.device;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
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
}
