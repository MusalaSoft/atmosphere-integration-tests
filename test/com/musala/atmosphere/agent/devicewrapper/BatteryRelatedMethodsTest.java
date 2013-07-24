package com.musala.atmosphere.agent.devicewrapper;

import java.rmi.RemoteException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.UiElementAttributes;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.util.Server;
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

	private AgentIntegrationEnvironmentCreator agentEnvironment;

	private ServerIntegrationEnvironmentCreator serverEnvironment;

	private static final int EMULATOR_CREATION_DPI = 120;

	private static final int EMULATOR_CREATION_RAM = 256;

	private static final int EMULATOR_CREATION_RESOLUTION_H = 240;

	private static final int EMULATOR_CREATION_RESOLUTION_W = 360;

	private final static String PATH_TO_APK_DIR = "./";

	private final static String NAME_OF_APK_FILE = "OnDeviceValidator.apk";

	private final static String PATH_TO_APK = PATH_TO_APK_DIR + NAME_OF_APK_FILE;

	private final static String VALIDATOR_APP_PACKAGE = "com.musala.atmosphere.ondevice.validator";

	private final static String VALIDATOR_APP_ACTIVITY = "MainActivity";

	private final static String VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC = "ATMOSPHEREValidator";

	private final static String BATTERY_LEVEL_BOX = "BatteryLevelBox";

	private final static String BATTERY_LOW_FLAG = "BatteryLowFlag";

	private final static String POWER_CONNECTED_FLAG = "PowerConnectedFlag";

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
		String agentId = agentEnvironment.getUnderlyingAgentId();
		serverEnvironment.waitForAgentConnection(agentId);
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
	public void testBatteryRelatedMethods()
		throws UiElementFetchingException,
			ActivityStartingException,
			InterruptedException,
			CommandFailedException,
			RemoteException
	{
		GettingBuilderClass builderGet = new GettingBuilderClass();
		Builder deviceBuilder = builderGet.getBuilder();

		com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters blankParameters = new com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters();
		Device testDevice = deviceBuilder.getDevice(blankParameters);

		testDevice.installAPK(PATH_TO_APK);

		// TODO add device unlock here when device.unlock() method is implemented;
		// TODO add HOME BUTTON press here when device.pressButton(HOME) method is implemented;

		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(1000);

		Screen activeScreen = testDevice.getActiveScreen();
		UiElement check = activeScreen.getElementCSS("[content-desc=" + VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC + "]");

		// set battery level
		int batteryLevel = 75;
		testDevice.setBatteryLevel(batteryLevel);
		activeScreen = testDevice.getActiveScreen();
		UiElement batteryLevelBox = activeScreen.getElementCSS("[content-desc=" + BATTERY_LEVEL_BOX + "]");
		UiElementAttributes batteryLevelBoxAttributes = batteryLevelBox.getElementAttributes();
		UiElement batteryLow = activeScreen.getElementCSS("[content-desc=" + BATTERY_LOW_FLAG + "]");
		UiElementAttributes batteryLowAttributes = batteryLow.getElementAttributes();
		String batteryLowText = batteryLowAttributes.getText();

		// set power state
		boolean powerState = false;
		testDevice.setPowerState(powerState);
		activeScreen = testDevice.getActiveScreen();
		UiElement powerButton = activeScreen.getElementCSS("[content-desc=" + POWER_CONNECTED_FLAG + "]");
		UiElementAttributes powerButtonAttributes = powerButton.getElementAttributes();
		boolean powerStateResult = powerButtonAttributes.isEnabled();
	}
}
