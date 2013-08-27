package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.*;

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
		installValidatorApp();
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY, true);
		Thread.sleep(1000);
	}

	@Test
	public void testAutoRotationSetting() throws Exception
	{
		testDevice.setAutoRotation(false);
		assertAutoRotationOff("Auto ratation is not off.");

		testDevice.setAutoRotation(true);
		assertAutoRotationOn("Auto ratation is not on.");
	}

	@Test
	public void testScreenOrientationSetting() throws Exception
	{
		testDevice.setScreenOrientation(ScreenOrientation.LANDSCAPE);
		assertScreenOrientation("Screen rotation expected to be landscape", ScreenOrientation.LANDSCAPE);

		testDevice.setScreenOrientation(ScreenOrientation.PORTRAIT);
		assertScreenOrientation("Screen rotation expected to be portrait", ScreenOrientation.PORTRAIT);

		testDevice.setScreenOrientation(ScreenOrientation.UPSIDE_DOWN_LANDSCAPE);
		assertScreenOrientation("Screen rotation expected to be upside down landscape",
								ScreenOrientation.UPSIDE_DOWN_LANDSCAPE);

		testDevice.setScreenOrientation(ScreenOrientation.UPSIDE_DOWN_PORTRAIT);
		assertScreenOrientation("Screen rotation expected to be upside down portrait",
								ScreenOrientation.UPSIDE_DOWN_PORTRAIT);
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void testScreenOrientationWithNull()
	{
		testDevice.setScreenOrientation(null);
	}
}
