package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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

	private final static String PATH_TO_APK_DIR = "./";

	private final static String NAME_OF_APK_FILE = "OnDeviceValidator.apk";

	private final static String PATH_TO_APK = PATH_TO_APK_DIR + NAME_OF_APK_FILE;

	private final static String VALIDATOR_APP_PACKAGE = "com.musala.atmosphere.ondevice.validator";

	private final static String VALIDATOR_APP_ACTIVITY = "MainActivity";

	private final static String BATTERY_LEVEL_BOX = "BatteryLevelBox";

	private final static String BATTERY_STATUS_BOX = "BatteryStatusBox";

	private final static String BATTERY_LOW_FLAG = "BatteryLowFlag";

	private final static String POWER_CONNECTED_FLAG = "PowerConnectedFlag";

	private static Device testDevice;

	private static Screen activeScreen;

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

		testDevice.installAPK(PATH_TO_APK);

		// TODO add device unlock here when device.unlock() method is implemented;
		// TODO add HOME BUTTON press here when device.pressButton(HOME) method is implemented;

		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(1000);

		activeScreen = testDevice.getActiveScreen();
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

		// set battery level to 75
		int batteryLevel = 75;
		testDevice.setBatteryLevel(batteryLevel);
		activeScreen = testDevice.getActiveScreen();
		UiElement batteryLevelBox = activeScreen.getElementCSS("[content-desc=" + BATTERY_LEVEL_BOX + "]");
		UiElementAttributes batteryLevelBoxAttributes = batteryLevelBox.getElementAttributes();
		String batteryLevelBoxString = batteryLevelBoxAttributes.getText();
		assertEquals("Battery level not set to the expected value.", "75", batteryLevelBoxString);

		UiElement batteryLow = activeScreen.getElementCSS("[content-desc=" + BATTERY_LOW_FLAG + "]");
		UiElementAttributes batteryLowAttributes = batteryLow.getElementAttributes();
		boolean isBatteryLow = batteryLowAttributes.isEnabled();
		assertFalse("Battery low flag not set as expected.", isBatteryLow);
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
		activeScreen = testDevice.getActiveScreen();
		UiElement batteryStatusBox = activeScreen.getElementCSS("[content-desc=" + BATTERY_STATUS_BOX + "]");
		UiElementAttributes batteryStatusBoxAttributes = batteryStatusBox.getElementAttributes();
		String batteryStatusBoxText = batteryStatusBoxAttributes.getText();
		assertEquals(	"Battery status not set to the expected value.",
						BatteryState.UNKNOWN.toString(),
						batteryStatusBoxText);

		// set battery state charging
		batteryState = BatteryState.CHARGING;
		testDevice.setBatteryState(batteryState);
		activeScreen = testDevice.getActiveScreen();
		batteryStatusBox = activeScreen.getElementCSS("[content-desc=" + BATTERY_STATUS_BOX + "]");
		batteryStatusBoxAttributes = batteryStatusBox.getElementAttributes();
		batteryStatusBoxText = batteryStatusBoxAttributes.getText();
		assertEquals(	"Battery status not set to the expected value.",
						BatteryState.CHARGING.toString(),
						batteryStatusBoxText);

		// set battery state discharging
		batteryState = BatteryState.DISCHARGING;
		testDevice.setBatteryState(batteryState);
		activeScreen = testDevice.getActiveScreen();
		batteryStatusBox = activeScreen.getElementCSS("[content-desc=" + BATTERY_STATUS_BOX + "]");
		batteryStatusBoxAttributes = batteryStatusBox.getElementAttributes();
		batteryStatusBoxText = batteryStatusBoxAttributes.getText();
		assertEquals(	"Battery status not set to the expected value.",
						BatteryState.DISCHARGING.toString(),
						batteryStatusBoxText);

		// set battery state not_charging
		batteryState = BatteryState.NOT_CHARGING;
		testDevice.setBatteryState(batteryState);
		activeScreen = testDevice.getActiveScreen();
		batteryStatusBox = activeScreen.getElementCSS("[content-desc=" + BATTERY_STATUS_BOX + "]");
		batteryStatusBoxAttributes = batteryStatusBox.getElementAttributes();
		batteryStatusBoxText = batteryStatusBoxAttributes.getText();
		assertEquals(	"Battery status not set to the expected value.",
						BatteryState.NOT_CHARGING.toString(),
						batteryStatusBoxText);

		// set battery state full
		batteryState = BatteryState.FULL;
		testDevice.setBatteryState(batteryState);
		activeScreen = testDevice.getActiveScreen();
		batteryStatusBox = activeScreen.getElementCSS("[content-desc=" + BATTERY_STATUS_BOX + "]");
		batteryStatusBoxAttributes = batteryStatusBox.getElementAttributes();
		batteryStatusBoxText = batteryStatusBoxAttributes.getText();
		assertEquals(	"Battery status not set to the expected value.",
						BatteryState.FULL.toString(),
						batteryStatusBoxText);
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
		activeScreen = testDevice.getActiveScreen();
		UiElement powerButton = activeScreen.getElementCSS("[content-desc=" + POWER_CONNECTED_FLAG + "]");
		UiElementAttributes powerButtonAttributes = powerButton.getElementAttributes();
		assertFalse("Power state not set to the expected value.", powerButtonAttributes.isEnabled());

		// set device power connection on
		powerState = true;
		testDevice.setPowerState(powerState);
		activeScreen = testDevice.getActiveScreen();
		powerButton = activeScreen.getElementCSS("[content-desc=" + POWER_CONNECTED_FLAG + "]");
		powerButtonAttributes = powerButton.getElementAttributes();
		assertTrue("Power state not set to the expected value.", powerButtonAttributes.isEnabled());
	}
}
