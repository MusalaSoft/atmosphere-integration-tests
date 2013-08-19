package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationAzimuth;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationPitch;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertOrientationRoll;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setupAndStartOrientationActivity;

import java.rmi.RemoteException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.CommandFailedException;
import com.musala.atmosphere.commons.DeviceOrientation;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.commons.sa.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;

@Server(ip = "localhost", port = 1980)
public class DeviceOrientationTest
{
	private final static int POOLMANAGER_RMI_PORT = 2099;

	private final static int AGENTMANAGER_RMI_PORT = 2000;

	private AgentIntegrationEnvironmentCreator agentEnvironment;

	private ServerIntegrationEnvironmentCreator serverEnvironment;

	private static final int EMULATOR_CREATION_DPI = 120;

	private static final int EMULATOR_CREATION_RAM = 256;

	private static final int EMULATOR_CREATION_RESOLUTION_H = 240;

	private static final int EMULATOR_CREATION_RESOLUTION_W = 360;

	private static Device testDevice;

	@Before
	public void setUp() throws Exception
	{
		agentEnvironment = new AgentIntegrationEnvironmentCreator(AGENTMANAGER_RMI_PORT);
		serverEnvironment = new ServerIntegrationEnvironmentCreator(POOLMANAGER_RMI_PORT);

		if (!agentEnvironment.isAnyDevicePresent())
		{
			DeviceParameters emulatorCreationParameters = new DeviceParameters();
			emulatorCreationParameters.setDpi(EMULATOR_CREATION_DPI);
			emulatorCreationParameters.setRam(EMULATOR_CREATION_RAM);
			emulatorCreationParameters.setResolution(new Pair<Integer, Integer>(EMULATOR_CREATION_RESOLUTION_H,
																				EMULATOR_CREATION_RESOLUTION_W));

			IWrapDevice createdEmulator = agentEnvironment.startEmulator(emulatorCreationParameters);
			agentEnvironment.waitForDeviceOsToStart(createdEmulator);
		}

		agentEnvironment.connectToLocalhostServer(POOLMANAGER_RMI_PORT);
		String anyDeviceId = agentEnvironment.getFirstAvailableDeviceWrapper().getDeviceInformation().getSerialNumber();
		String agentId = agentEnvironment.getUnderlyingAgentId();
		serverEnvironment.waitForAgentConnection(agentId);

		serverEnvironment.waitForDeviceToBeAvailable(anyDeviceId, agentId);

		GettingBuilderClass builderGet = new GettingBuilderClass();
		Builder deviceBuilder = builderGet.getBuilder();

		com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters blankParameters = new com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters();
		blankParameters.setDeviceType(DeviceType.EMULATOR_ONLY);
		testDevice = deviceBuilder.getDevice(blankParameters);

		setTestDevice(testDevice);
		setupAndStartOrientationActivity();
	}

	@After
	public void tearDown() throws Exception
	{
		serverEnvironment.close();
		agentEnvironment.close();
	}

	@Server(ip = "localhost", port = POOLMANAGER_RMI_PORT)
	private class GettingBuilderClass
	{
		public GettingBuilderClass()
		{
		}

		public Builder getBuilder()
		{
			Builder classDeviceBuilder = Builder.getInstance();
			return classDeviceBuilder;
		}
	}

	@Test
	public void testDeviceOrientationSetting()
		throws UiElementFetchingException,
			ActivityStartingException,
			InterruptedException,
			CommandFailedException,
			RemoteException
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
