package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAutoRotationOff;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAutoRotationOn;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScreenOrientation;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.ScreenOrientation;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class ScreenOrientationTest extends BaseIntegrationTest
{
	@BeforeClass
	public static void setUp() throws Exception
	{

		DeviceParameters testDeviceParams = new DeviceParameters();
		initTestDevice(testDeviceParams);
		installValidatorApplication();
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY, true);
		Thread.sleep(1000);
	}

	@Test
	public void testSettingAutoRotationOff() throws Exception
	{
		testDevice.setAutoRotation(false);
		assertAutoRotationOff("Auto ratation is not off.");
	}

	@Test
	public void testSettingAutoRotationOn() throws Exception
	{
		testDevice.setAutoRotation(true);
		assertAutoRotationOn("Auto ratation is not on.");
	}

	@Test
	public void testSettingScreenOrientationPortrait() throws Exception
	{
		testDevice.setScreenOrientation(ScreenOrientation.PORTRAIT);
		assertScreenOrientation("Screen rotation expected to be portrait", ScreenOrientation.PORTRAIT);
	}

	@Test
	public void testSettingScreenOrientationUpsideDownPortrait() throws Exception
	{
		testDevice.setScreenOrientation(ScreenOrientation.UPSIDE_DOWN_PORTRAIT);
		assertScreenOrientation("Screen rotation expected to be upside down portrait",
								ScreenOrientation.UPSIDE_DOWN_PORTRAIT);
	}

	@Test
	public void testSettingScreenOrientationLandscape() throws Exception
	{
		testDevice.setScreenOrientation(ScreenOrientation.LANDSCAPE);
		assertScreenOrientation("Screen rotation expected to be landscape", ScreenOrientation.LANDSCAPE);
	}

	@Test
	public void testSettingScreenOrientationUpsideDown() throws Exception
	{
		testDevice.setScreenOrientation(ScreenOrientation.UPSIDE_DOWN_LANDSCAPE);
		assertScreenOrientation("Screen rotation expected to be upside down landscape",
								ScreenOrientation.UPSIDE_DOWN_LANDSCAPE);
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void testScreenOrientationWithNull()
	{
		testDevice.setScreenOrientation(null);
	}
}
