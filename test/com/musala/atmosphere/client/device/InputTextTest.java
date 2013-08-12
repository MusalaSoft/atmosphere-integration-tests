package com.musala.atmosphere.client.device;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.UiElementAttributes;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.sa.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;

// FIXME valyo should fix this test to actually validate input.
@Server(ip = "localhost", port = 1980)
public class InputTextTest
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

	private final static String INPUT_TEXT_BOX = "InputTextBox";

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
	public void inputTextTest() throws Exception
	{
		GettingBuilderClass builderGet = new GettingBuilderClass();
		Builder deviceBuilder = builderGet.getBuilder();

		com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters blankParameters = new com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters();
		Device testDevice = deviceBuilder.getDevice(blankParameters);

		testDevice.unlock(); // Unlock device if locked.
		testDevice.pressButton(HardwareButton.HOME); // Press HOME button.

		testDevice.installAPK(PATH_TO_APK);
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(1000);
		Screen activeScreen = testDevice.getActiveScreen();
		UiElement check = activeScreen.getElementCSS("[content-desc=" + VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC + "]");
		assertNotNull("Validator app not loaded.", check);

		// text input test
		String textToInput = "Hi! Кирилица. €%@$§№%()456*/0,";
		testDevice.inputText(textToInput);

		// text input with time intervals
		int inputInterval = 2500;// ms
		String textToInput1 = "letters.";
		testDevice.inputText(textToInput1, inputInterval);
		activeScreen = testDevice.getActiveScreen();
		UiElement inputTextBox = activeScreen.getElementCSS("[content-desc=" + INPUT_TEXT_BOX + "]");
		UiElementAttributes inputTextBoxAttributes = inputTextBox.getElementAttributes();
		deviceBuilder.releaseDevice(testDevice);
	}

	public void inputTextTestTwo() throws Exception
	{
		GettingBuilderClass builderGet = new GettingBuilderClass();
		Builder deviceBuilder = builderGet.getBuilder();

		com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters blankParameters = new com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters();
		Device testDevice = deviceBuilder.getDevice(blankParameters);

		testDevice.unlock(); // Unlock device if locked.
		testDevice.pressButton(HardwareButton.HOME); // Press HOME button.

		testDevice.installAPK(PATH_TO_APK);
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(1000);

		Screen activeScreen = testDevice.getActiveScreen();
		UiElement check = activeScreen.getElementCSS("[content-desc=" + VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC + "]");
		assertNotNull("Validator app not loaded.", check);

		// text input with time intervals
		int inputInterval = 2500;// ms
		String textToInput1 = "letters.";
		testDevice.inputText(textToInput1, inputInterval);
		activeScreen = testDevice.getActiveScreen();
		UiElement inputTextBox = activeScreen.getElementCSS("[content-desc=" + INPUT_TEXT_BOX + "]");
		UiElementAttributes inputTextBoxAttributes = inputTextBox.getElementAttributes();
		deviceBuilder.releaseDevice(testDevice);
	}
}
