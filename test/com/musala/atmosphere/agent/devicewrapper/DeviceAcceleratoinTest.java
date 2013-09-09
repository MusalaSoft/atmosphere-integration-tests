package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAccelerationX;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAccelerationY;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertAccelerationZ;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.DeviceAcceleration;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

public class DeviceAcceleratoinTest extends BaseIntegrationTest
{
	private final static String VALIDATOR_APP_ACCELERATION_ACTIVITY = "AccelerationActivity";

	@BeforeClass
	public static void setUp() throws Exception
	{

		DeviceParameters emulatorTestDevice = new DeviceParameters();
		emulatorTestDevice.setDeviceType(DeviceType.EMULATOR_ONLY);
		initTestDevice(emulatorTestDevice);
		installValidatorApplication();
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACCELERATION_ACTIVITY, true);
		Thread.sleep(1000);
	}

	@Test
	public void testDeviceRandomAccelerationSetting() throws UiElementFetchingException
	{
		// set "random" acceleration
		final float accelerationX = 3.087f;
		final float accelerationY = -1.130f;
		final float accelerationZ = 5.904f;
		DeviceAcceleration deviceAcceleration = new DeviceAcceleration(accelerationX, accelerationY, accelerationZ);
		testDevice.setAcceleration(deviceAcceleration);

		assertAccelerationX("Device acceleration on the X axis not set to the expected value.", accelerationX);
		assertAccelerationY("Device acceleration on the Y axis not set to the expected value.", accelerationY);
		assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.", accelerationZ);
	}

	@Test
	public void testDeviceLandscapeAccelerationSetting() throws UiElementFetchingException
	{
		// set landscape acceleration
		DeviceAcceleration landscapeAcceleration = DeviceAcceleration.getLandscape();
		testDevice.setAcceleration(landscapeAcceleration);

		assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
							landscapeAcceleration.getAccelerationX());
		assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
							landscapeAcceleration.getAccelerationY());
		assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
							landscapeAcceleration.getAccelerationZ());
	}

	public void testDeviceReverseLandscapeAccelerationSetting() throws UiElementFetchingException
	{
		// set landscape acceleration
		DeviceAcceleration reverseLandscapeAcceleration = DeviceAcceleration.getReverseLandscape();
		testDevice.setAcceleration(reverseLandscapeAcceleration);

		assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
							reverseLandscapeAcceleration.getAccelerationX());
		assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
							reverseLandscapeAcceleration.getAccelerationY());
		assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
							reverseLandscapeAcceleration.getAccelerationZ());
	}

	@Test
	public void testDevicePortraitAccelerationSetting() throws UiElementFetchingException
	{
		// set portrait acceleration
		DeviceAcceleration portraitAcceleration = DeviceAcceleration.getPortrait();
		testDevice.setAcceleration(portraitAcceleration);

		assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
							portraitAcceleration.getAccelerationX());
		assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
							portraitAcceleration.getAccelerationY());
		assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
							portraitAcceleration.getAccelerationZ());
	}

	@Test
	public void testDeviceReversePortraitAccelerationSetting() throws UiElementFetchingException
	{
		// set portrait acceleration
		DeviceAcceleration reversePortraitAcceleration = DeviceAcceleration.getReversePortrait();
		testDevice.setAcceleration(reversePortraitAcceleration);

		assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
							reversePortraitAcceleration.getAccelerationX());
		assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
							reversePortraitAcceleration.getAccelerationY());
		assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
							reversePortraitAcceleration.getAccelerationZ());
	}

	@Test
	public void testDeviceLieDownAccelerationSetting() throws UiElementFetchingException
	{
		// set portrait acceleration
		DeviceAcceleration lieDownAcceleration = DeviceAcceleration.getLieDown();
		testDevice.setAcceleration(lieDownAcceleration);

		assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
							lieDownAcceleration.getAccelerationX());
		assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
							lieDownAcceleration.getAccelerationY());
		assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
							lieDownAcceleration.getAccelerationZ());
	}

	@Test
	public void testDeviceReverseLieDownAccelerationSetting() throws UiElementFetchingException
	{
		// set portrait acceleration
		DeviceAcceleration reverseLieDownAcceleration = DeviceAcceleration.getReverseLieDown();
		testDevice.setAcceleration(reverseLieDownAcceleration);

		assertAccelerationX("Device acceleration on the X axis not set to the expected value.",
							reverseLieDownAcceleration.getAccelerationX());
		assertAccelerationY("Device acceleration on the Y axis not set to the expected value.",
							reverseLieDownAcceleration.getAccelerationY());
		assertAccelerationZ("Device acceleration on the Z axis not set to the expected value.",
							reverseLieDownAcceleration.getAccelerationZ());
	}

	@Test
	public void testGetDeviceAcceleration() throws Exception
	{
		// Getting device acceleration works for both real devices and emulators.
		DeviceAcceleration deviceAcceleration = testDevice.getDeviceAcceleration();
		assertNotNull("Failed getting device acceleration value on the X axis.", deviceAcceleration.getAccelerationX());
		assertNotNull("Failed getting device acceleration value on the Y axis.", deviceAcceleration.getAccelerationY());
		assertNotNull("Failed getting device acceleration value on the Z axis.", deviceAcceleration.getAccelerationZ());
	}
}
