package com.musala.atmosphere.client.device;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;

/**
 * 
 * @author georgi.gaydarov
 * 
 */
public class LockUnlockTest
{
	private final static int POOLMANAGER_RMI_PORT = 2099;

	private final static int AGENTMANAGER_RMI_PORT = 2000;

	private final static String PATH_TO_APK_DIR = "./";

	private final static String VALIDATOR_APP_PACKAGE = "com.musala.atmosphere.ondevice.validator";

	private final static String VALIDATOR_APP_ACTIVITY = "MainActivity";

	private final static String VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC = "ATMOSPHEREValidator";

	private final static String NAME_OF_APK_FILE = "OnDeviceValidator.apk";

	private final static String PATH_TO_APK = PATH_TO_APK_DIR + NAME_OF_APK_FILE;

	private AgentIntegrationEnvironmentCreator agentEnvironment;

	private ServerIntegrationEnvironmentCreator serverEnvironment;

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

	@Before
	public void setUp() throws Exception
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

	@After
	public void tearDown() throws Exception
	{
		serverEnvironment.close();
		agentEnvironment.close();
	}

	@Test
	public void testUnlockLockDevice()
		throws InterruptedException,
			ActivityStartingException,
			UiElementFetchingException
	{
		BuilderHelperClass helper = new BuilderHelperClass();
		Builder builder = helper.getBuilder();
		DeviceParameters selectionParameters = new DeviceParameters();
		Device testDevice = builder.getDevice(selectionParameters);

		testDevice.installAPK(PATH_TO_APK);

		testDevice.unlock();
		testDevice.pressButton(HardwareButton.HOME);

		assertFalse("Device shouldn't be locked after .unlock().", testDevice.isLocked());
		assertTrue("Device should be awake after .unlock().", testDevice.isAwake());

		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(1000);

		Screen deviceScreen = testDevice.getActiveScreen();
		UiElementSelector validationViewSelector = new UiElementSelector();
		validationViewSelector.setContentDescription(VALIDATOR_APP_CONTROL_ELEMENT_CONTENTDESC);
		// If the validator app activity is not started, this element fetching will fail.
		UiElement validationView = deviceScreen.getElement(validationViewSelector);

		testDevice.lock();
		try
		{
			deviceScreen = testDevice.getActiveScreen();
			// when the device is locked, the lock screen will be fetched that does not contain our controll element.
			validationView = deviceScreen.getElement(validationViewSelector);
			fail("The validation element should not be available when the device is locked.");
		}
		catch (UiElementFetchingException e)
		{
			// this should be thrown.
		}

		assertTrue("Device should be locked after .lock().", testDevice.isLocked());
		assertFalse("Device shouldn't be awake after .lock().", testDevice.isAwake());
	}
}