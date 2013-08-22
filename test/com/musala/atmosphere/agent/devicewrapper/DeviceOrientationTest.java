package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationAzimuth;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationPitch;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationRoll;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.DeviceOrientation;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

public class DeviceOrientationTest extends BaseIntegrationTest
{

	@BeforeClass
	public static void setUp() throws Exception
	{

		DeviceParameters emulatorTestDevice = new DeviceParameters();
		emulatorTestDevice.setDeviceType(DeviceType.EMULATOR_ONLY);
		initTestDevice(emulatorTestDevice);
		installValidatorApp();
		// TODO start orientation activity here.
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY, true);
		Thread.sleep(1000);
	}

	@Test
	public void testDeviceOrientationSetting() throws Exception
	{
		// set orientation
		final float orientationAzimuth = 97.087f;
		final float orientationPitch = -13.130f;
		final float orientationRoll = 55.904f;
		DeviceOrientation deviceOrientation = new DeviceOrientation(orientationAzimuth,
																	orientationPitch,
																	orientationRoll);
		testDevice.setOrientation(deviceOrientation);

		assertOrientationAzimuth("Device Azimuth not set to the expected value.", orientationAzimuth);
		assertOrientationPitch("Device pitch not set to the expected value.", orientationPitch);
		assertOrientationRoll("Device roll not set to the expected value.", orientationRoll);
	}
}
