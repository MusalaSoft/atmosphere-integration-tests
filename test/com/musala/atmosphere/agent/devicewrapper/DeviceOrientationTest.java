package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationAzimuth;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationPitch;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationRoll;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.DeviceOrientation;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

/**
 * 
 * @author valyo.yolovski
 * 
 */
public class DeviceOrientationTest extends BaseIntegrationTest
{
	private static final String VALIDATOR_APP_ORIENTATION_ACTIVITY = "OrientationActivity";

	@BeforeClass
	public static void setUp() throws Exception
	{

		DeviceParameters emulatorTestDevice = new DeviceParameters();
		emulatorTestDevice.setDeviceType(DeviceType.EMULATOR_ONLY);
		initTestDevice(emulatorTestDevice);
		installValidatorApplication();
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ORIENTATION_ACTIVITY, true);
		Thread.sleep(1000);
	}

	@Test
	public void testSetRandomDeviceOrientation() throws Exception
	{
		final float orientationAzimuth = 97.087f;
		final float orientationPitch = -13.130f;
		final float orientationRoll = 55.904f;
		DeviceOrientation deviceOrientation = new DeviceOrientation(orientationAzimuth,
																	orientationPitch,
																	orientationRoll);
		testDevice.setDeviceOrientation(deviceOrientation);

		assertOrientationAzimuth("Device Azimuth not set to the expected value.", orientationAzimuth);
		assertOrientationPitch("Device pitch not set to the expected value.", orientationPitch);
		assertOrientationRoll("Device roll not set to the expected value.", orientationRoll);
	}

	@Test
	public void testSetDeviceOrientationPortrait() throws UiElementFetchingException
	{
		DeviceOrientation portraitOrientation = DeviceOrientation.getPortraitOrientation();
		testDevice.setDeviceOrientation(portraitOrientation);

		assertOrientationAzimuth("Device Azimuth not set to the expected value.", portraitOrientation.getAzimuth());
		assertOrientationPitch("Device pitch not set to the expected value.", portraitOrientation.getPitch());
		assertOrientationRoll("Device roll not set to the expected value.", portraitOrientation.getRoll());
	}

	@Test
	public void testSetDeviceOrientationUpsideDownPortrait() throws UiElementFetchingException
	{
		DeviceOrientation upsideDownPortraitOrientation = DeviceOrientation.getUpsideDownPortrait();
		testDevice.setDeviceOrientation(upsideDownPortraitOrientation);

		assertOrientationAzimuth(	"Device Azimuth not set to the expected value.",
									upsideDownPortraitOrientation.getAzimuth());
		assertOrientationPitch("Device pitch not set to the expected value.", upsideDownPortraitOrientation.getPitch());
		assertOrientationRoll("Device roll not set to the expected value.", upsideDownPortraitOrientation.getRoll());
	}

	@Test
	public void testSetDeviceOrientationLandscape() throws UiElementFetchingException
	{
		DeviceOrientation landscapeOrientation = DeviceOrientation.getLandscapeOrientation();
		testDevice.setDeviceOrientation(landscapeOrientation);

		assertOrientationAzimuth("Device Azimuth not set to the expected value.", landscapeOrientation.getAzimuth());
		assertOrientationPitch("Device pitch not set to the expected value.", landscapeOrientation.getPitch());
		assertOrientationRoll("Device roll not set to the expected value.", landscapeOrientation.getRoll());
	}

	@Test
	public void testSetDeviceOrientationUpsideDownLandscape() throws UiElementFetchingException
	{
		DeviceOrientation upsideDownLandscapeOrientation = DeviceOrientation.getUpsideDownLandscape();
		testDevice.setDeviceOrientation(upsideDownLandscapeOrientation);

		assertOrientationAzimuth(	"Device Azimuth not set to the expected value.",
									upsideDownLandscapeOrientation.getAzimuth());
		assertOrientationPitch("Device pitch not set to the expected value.", upsideDownLandscapeOrientation.getPitch());
		assertOrientationRoll("Device roll not set to the expected value.", upsideDownLandscapeOrientation.getRoll());
	}

	@Test
	public void testGetDeviceOrientation() throws Exception
	{
		// Getting device orientation works for both real devices and emulators.
		DeviceOrientation deviceOrientation = testDevice.getDeviceOrientation();
		assertNotNull("Failed getting device azimuth value.", deviceOrientation.getAzimuth());
		assertNotNull("Failed getting device pitch value.", deviceOrientation.getPitch());
		assertNotNull("Failed getting device rol value.", deviceOrientation.getRoll());
	}
}
