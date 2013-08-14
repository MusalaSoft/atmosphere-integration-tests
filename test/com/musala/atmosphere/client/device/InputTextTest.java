package com.musala.atmosphere.client.device;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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

/**
 * @author valyo.yolovski
 */

@Server(ip = "localhost", port = 1980)
public class InputTextTest
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

	private final static String VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC = "ATMOSPHEREValidator";

	private final static String INPUT_TEXT_BOX = "InputTextBox";

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
		String agentId = agentEnvironment.getUnderlyingAgentId();
		serverEnvironment.waitForAgentConnection(agentId);
	}

	@AfterClass
	public static void tearDown() throws Exception
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
		UiElement inputTextBox = activeScreen.getElementCSS("[content-desc=" + INPUT_TEXT_BOX + "]");
		String textToInput = "Hi! Кирилица. €%@$§№%()456*/0,.";
		inputTextBox.inputText(textToInput); // Input text into field.

		activeScreen = testDevice.getActiveScreen();
		inputTextBox = activeScreen.getElementXPath("//*[@content-desc='" + INPUT_TEXT_BOX + "']");
		UiElementAttributes inputTextBoxAttributes = inputTextBox.getElementAttributes();
		String textFromDevice = inputTextBoxAttributes.getText();
		assertEquals("Inputting text failed.", textToInput, textFromDevice);

		deviceBuilder.releaseDevice(testDevice);// Release device so it can be reused.
	}

	@Test
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
		UiElement inputTextBox = activeScreen.getElementCSS("[content-desc=" + INPUT_TEXT_BOX + "]");
		String textToInput = "Letters."; // Text to input.
		int inputInterval = 2500;// Time interval between the input of each letter in ms.
		inputTextBox.inputText(textToInput, inputInterval); // Input text into field.

		activeScreen = testDevice.getActiveScreen();
		inputTextBox = activeScreen.getElementXPath("//*[@content-desc='" + INPUT_TEXT_BOX + "']");
		UiElementAttributes inputTextBoxAttributes = inputTextBox.getElementAttributes();
		String textFromDevice = inputTextBoxAttributes.getText(); // Get inputted text.

		assertEquals("Inputting text failed.", textToInput, textFromDevice);

		deviceBuilder.releaseDevice(testDevice); // Release device so it can be reused.
	}
}
