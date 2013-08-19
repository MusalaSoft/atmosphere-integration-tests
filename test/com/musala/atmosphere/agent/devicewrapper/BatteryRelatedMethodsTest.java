package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryNotLow;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryState;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertNotPowerConnected;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertPowerConnected;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setupAndStartMainActivity;

import java.rmi.RemoteException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.BatteryState;
import com.musala.atmosphere.commons.CommandFailedException;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.sa.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;

@Server(ip = "localhost", port = 1980)
public class BatteryRelatedMethodsTest
{
	private final static int POOLMANAGER_RMI_PORT = 2099;

	private final static int AGENTMANAGER_RMI_PORT = 2000;

	private static AgentIntegrationEnvironmentCreator agentEnvironment;

	private static ServerIntegrationEnvironmentCreator serverEnvironment;

	private static final int EMULATOR_CREATION_DPI = 120;

	private static final int EMULATOR_CREATION_RAM = 256;

	private static final int EMULATOR_CREATION_RESOLUTION_H = 240;

	private static final int EMULATOR_CREATION_RESOLUTION_W = 360;

	private static Device testDevice;

	@BeforeClass
	public static void setUp() throws Exception
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
		testDevice = deviceBuilder.getDevice(blankParameters);

		setTestDevice(testDevice);
		setupAndStartMainActivity();
	}

	@AfterClass
	public static void tearDown() throws Exception
	{
		serverEnvironment.close();
		agentEnvironment.close();
	}

	@Server(ip = "localhost", port = POOLMANAGER_RMI_PORT)
	private static class GettingBuilderClass
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
	public void testSetBatteryLevel()
		throws UiElementFetchingException,
			ActivityStartingException,
			InterruptedException,
			CommandFailedException,
			RemoteException
	{
		testDevice.unlock();

		// set battery level to 75
		int batteryLevel = 75;
		testDevice.setBatteryLevel(batteryLevel);

		assertBatteryNotLow("Battery low flag not set as expected.");
	}

	@Test
	public void testSetBatteryState()
		throws UiElementFetchingException,
			ActivityStartingException,
			InterruptedException,
			CommandFailedException,
			RemoteException
	{
		BatteryState batteryState;

		// set battery state unknown
		batteryState = BatteryState.UNKNOWN;
		testDevice.setBatteryState(batteryState);
		assertBatteryState("Battery status not set to the expected value.", BatteryState.UNKNOWN);

		// set battery state charging
		batteryState = BatteryState.CHARGING;
		testDevice.setBatteryState(batteryState);
		assertBatteryState("Battery status not set to the expected value.", BatteryState.CHARGING);

		// set battery state discharging
		batteryState = BatteryState.DISCHARGING;
		testDevice.setBatteryState(batteryState);
		assertBatteryState("Battery status not set to the expected value.", BatteryState.DISCHARGING);

		// set battery state not_charging
		batteryState = BatteryState.NOT_CHARGING;
		testDevice.setBatteryState(batteryState);
		assertBatteryState("Battery status not set to the expected value.", BatteryState.NOT_CHARGING);

		// set battery state full
		batteryState = BatteryState.FULL;
		testDevice.setBatteryState(batteryState);
		assertBatteryState("Battery status not set to the expected value.", BatteryState.FULL);
	}

	@Test
	public void testSetPowerState()
		throws UiElementFetchingException,
			ActivityStartingException,
			InterruptedException,
			CommandFailedException,
			RemoteException
	{
		// set device power connection off
		boolean powerState = false;
		testDevice.setPowerState(powerState);
		assertNotPowerConnected("Power state not set to the expected value.");

		// set device power connection on
		powerState = true;
		testDevice.setPowerState(powerState);
		assertPowerConnected("Power state not set to the expected value.");
	}
}
