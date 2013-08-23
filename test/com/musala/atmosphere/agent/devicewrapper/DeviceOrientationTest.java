package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationAzimuth;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationPitch;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationRoll;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
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
	private final String VALIDATOR_APP_ORIENTATION_ACTIVITY = "OrientationActivity";

	@Test
	public void setDeviceOrientationTest() throws Exception
	{
		// Only this test requires emulator.
		DeviceParameters emulatorTestDeviceParameters = new DeviceParameters();
		emulatorTestDeviceParameters.setDeviceType(DeviceType.EMULATOR_ONLY);
		initTestDevice(emulatorTestDeviceParameters); // Works only for emulators!
		installValidatorApp();

		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ORIENTATION_ACTIVITY, true);
		Thread.sleep(1000);

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
	public void getDeviceOrientationTest() throws Exception
	{
		initTestDevice(new DeviceParameters());
		installValidatorApp();

		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ORIENTATION_ACTIVITY, true);
		Thread.sleep(1000);

		DeviceOrientation deviceOrientation = testDevice.getDeviceOrientation(); // Works for both real devices and
																					// emulators.
		assertNotNull("Failed getting device azimuth value.", deviceOrientation.getAzimuth());
		assertNotNull("Failed getting device pitch value.", deviceOrientation.getPitch());
		assertNotNull("Failed getting device rol value.", deviceOrientation.getRoll());
	}
}
