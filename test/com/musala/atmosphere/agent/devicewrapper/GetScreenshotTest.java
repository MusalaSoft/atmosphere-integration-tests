package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.DeviceInformation;
import com.musala.atmosphere.commons.util.Pair;

public class GetScreenshotTest extends BaseIntegrationTest
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		initTestDevice(new com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters());
	}

	@Test
	public void testGetScreenshot() throws Exception
	{
		byte[] screenshot = testDevice.getScreenshot();
		DeviceInformation deviceInformation = testDevice.getInformation();
		Pair<Integer, Integer> screenResolution = deviceInformation.getResolution();

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
