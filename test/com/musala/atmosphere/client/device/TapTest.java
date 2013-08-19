package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertBatteryStatusBoxIsFocused;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByClass;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContenDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setupAndStartMainActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.UiElementAttributes;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.geometry.Point;
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

	private final static String WIDGET_RELATIVE_LAYOUT = "android.widget.RelativeLayout";

	private final static String BATTERY_STATUS_BOX = "BatteryStatusBox";

	private static AgentIntegrationEnvironmentCreator agentEnvironment;

	private static ServerIntegrationEnvironmentCreator serverEnvironment;

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

		setTestDevice(testDevice);
		setupAndStartMainActivity();

		// test tapping
		UiElement batteryStatusBox = getElementByContenDescriptor(BATTERY_STATUS_BOX);
		batteryStatusBox.tap();

		assertBatteryStatusBoxIsFocused("Battery status box not focused.");

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

		setTestDevice(testDevice);
		setupAndStartMainActivity();

		// test relative tapping
		UiElement widgetRelativeLayout = getElementByClass(WIDGET_RELATIVE_LAYOUT);
		UiElementAttributes widgetRelativeLayoutAttributes = widgetRelativeLayout.getElementAttributes();
		UiElement batteryStatusBox = getElementByContenDescriptor(BATTERY_STATUS_BOX);
		UiElementAttributes batteryStatusBoxAttributes = batteryStatusBox.getElementAttributes();

		Point batteryStatusBoxUpperLeftCorner = batteryStatusBoxAttributes.getBounds().getUpperLeftCorner();
		Point BatteryStatusRelativeUpperLeftCorner = widgetRelativeLayoutAttributes.getBounds()
																					.getRelativePoint(batteryStatusBoxUpperLeftCorner);

		widgetRelativeLayout.tap(BatteryStatusRelativeUpperLeftCorner);

		assertBatteryStatusBoxIsFocused("Battery status box not focused.");

		// test completed. Releasing device for other tests to use it.
		builder.releaseDevice(testDevice);
	}
}