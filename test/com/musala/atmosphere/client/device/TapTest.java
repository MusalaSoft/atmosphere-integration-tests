package com.musala.atmosphere.client.device;

import static org.junit.Assert.assertTrue;

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
import com.musala.atmosphere.client.geometry.Point;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;

/**
 * 
 * @author yordan.petrov
 * 
 */
public class TapTest
{
	private final static int POOLMANAGER_RMI_PORT = 2099;

	private final static int AGENTMANAGER_RMI_PORT = 2000;

	private final static String PATH_TO_APK_DIR = "./";

	private final static String VALIDATOR_APP_PACKAGE = "com.musala.atmosphere.ondevice.validator";

	private final static String VALIDATOR_APP_ACTIVITY = "MainActivity";

	private final static String VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC = "ATMOSPHEREValidator";

	private final static String NAME_OF_APK_FILE = "OnDeviceValidator.apk";

	private final static String PATH_TO_APK = PATH_TO_APK_DIR + NAME_OF_APK_FILE;

	private final static String WIDGET_RELATIVE_LAYOUT = "android.widget.RelativeLayout";

	private final static String BATTERY_LEVEL_BOX = "BatteryLevelBox";

	private final static String BATTERY_STATUS_BOX = "BatteryStatusBox";

	private static AgentIntegrationEnvironmentCreator agentEnvironment;

	private static ServerIntegrationEnvironmentCreator serverEnvironment;

	private static Device testDevice;

	@Server(ip = "localhost", port = POOLMANAGER_RMI_PORT)
	private class BuilderHelperClass
	{
		public BuilderHelperClass()
		{
		}

		public Builder getBuilder()
		{
			return Builder.getInstance();
		}
	}

	@BeforeClass
	public static void setUp() throws Exception
	{
		agentEnvironment = new AgentIntegrationEnvironmentCreator(AGENTMANAGER_RMI_PORT);
		serverEnvironment = new ServerIntegrationEnvironmentCreator(POOLMANAGER_RMI_PORT);
		agentEnvironment.connectToLocalhostServer(POOLMANAGER_RMI_PORT);
		String agentId = agentEnvironment.getUnderlyingAgentId();
		serverEnvironment.waitForAgentConnection(agentId);

		if (!agentEnvironment.isAnyDevicePresent())
		{
			com.musala.atmosphere.commons.sa.DeviceParameters emulatorCreationParameters = new com.musala.atmosphere.commons.sa.DeviceParameters();
			IWrapDevice createdEmulatorWrapper = agentEnvironment.startEmulator(emulatorCreationParameters);
			agentEnvironment.waitForDeviceOsToStart(createdEmulatorWrapper);
		}

		String anyDeviceId = agentEnvironment.getFirstAvailableDeviceWrapper().getDeviceInformation().getSerialNumber();
		serverEnvironment.waitForDeviceToBeAvailable(anyDeviceId, agentId);
	}

	@AfterClass
	public static void tearDown() throws Exception
	{
		serverEnvironment.close();
		agentEnvironment.close();
	}

	@Test
	public void testTap() throws InterruptedException, ActivityStartingException, UiElementFetchingException
	{
		BuilderHelperClass helper = new BuilderHelperClass();
		Builder builder = helper.getBuilder();
		DeviceParameters selectionParameters = new DeviceParameters();
		Device testDevice = builder.getDevice(selectionParameters);

		testDevice.installAPK(PATH_TO_APK);

		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(1000);

		testDevice.unlock();

		Screen deviceScreen = testDevice.getActiveScreen();
		UiElementSelector validationViewSelector = new UiElementSelector();
		validationViewSelector.setContentDescription(VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC);
		// If the validator app activity is not started, this element fetching will fail.
		UiElement validationView = deviceScreen.getElement(validationViewSelector);

		// test tapping
		UiElement batteryStatusBox = deviceScreen.getElementCSS("[content-desc=" + BATTERY_STATUS_BOX + "]");
		batteryStatusBox.tap();

		deviceScreen = testDevice.getActiveScreen();
		batteryStatusBox = deviceScreen.getElementCSS("[content-desc=" + BATTERY_STATUS_BOX + "]");
		UiElementAttributes batteryStatusBoxAttributes = batteryStatusBox.getElementAttributes();
		boolean isBatteryStatusBoxFocused = batteryStatusBoxAttributes.isFocused();
		assertTrue("Battery status box not focused.", isBatteryStatusBoxFocused);

		// test completed. Releasing device for other tests to use it.
		builder.releaseDevice(testDevice);
	}

	@Test
	public void testRelativeTap() throws InterruptedException, ActivityStartingException, UiElementFetchingException
	{
		BuilderHelperClass helper = new BuilderHelperClass();
		Builder builder = helper.getBuilder();
		DeviceParameters selectionParameters = new DeviceParameters();
		Device testDevice = builder.getDevice(selectionParameters);

		testDevice.installAPK(PATH_TO_APK);

		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(1000);

		testDevice.unlock();

		Screen deviceScreen = testDevice.getActiveScreen();
		UiElementSelector validationViewSelector = new UiElementSelector();
		validationViewSelector.setContentDescription(VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC);
		// If the validator app activity is not started, this element fetching will fail.
		UiElement validationView = deviceScreen.getElement(validationViewSelector);

		// test relative tapping
		UiElement widgetRelativeLayout = deviceScreen.getElementCSS("[class=" + WIDGET_RELATIVE_LAYOUT + "]");
		UiElementAttributes widgetRelativeLayoutAttributes = widgetRelativeLayout.getElementAttributes();
		UiElement batteryLevelBox = deviceScreen.getElementCSS("[content-desc=" + BATTERY_LEVEL_BOX + "]");
		UiElementAttributes batteryLevelBoxAttributes = batteryLevelBox.getElementAttributes();

		Point batteryLevelBoxUpperLeftCorner = batteryLevelBoxAttributes.getBounds().getUpperLeftCorner();
		Point BatteryLevelRelativeUpperLeftCorner = widgetRelativeLayoutAttributes.getBounds()
																					.getRelativePoint(batteryLevelBoxUpperLeftCorner);

		widgetRelativeLayout.tap(BatteryLevelRelativeUpperLeftCorner);

		deviceScreen = testDevice.getActiveScreen();
		batteryLevelBox = deviceScreen.getElementCSS("[content-desc=" + BATTERY_LEVEL_BOX + "]");
		batteryLevelBoxAttributes = batteryLevelBox.getElementAttributes();
		boolean isBatteryLevelBoxFocused = batteryLevelBoxAttributes.isFocused();
		assertTrue("Battry level box not focused.", isBatteryLevelBoxFocused);

		// test completed. Releasing device for other tests to use it.
		builder.releaseDevice(testDevice);
	}
}