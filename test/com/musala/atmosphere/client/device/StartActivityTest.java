package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;

public class StartActivityTest
{
	private final static int POOLMANAGER_RMI_PORT = 2099;

	private final static int AGENTMANAGER_RMI_PORT = 2000;

	private AgentIntegrationEnvironmentCreator agentEnvironment;

	private ServerIntegrationEnvironmentCreator serverEnvironment;

	@Server(ip = "localhost", port = POOLMANAGER_RMI_PORT)
	class DeviceTestClass
	{

		private Device device = null;

		public DeviceTestClass(DeviceParameters parameters)
		{
			device = Builder.getInstance().getDevice(parameters);
		}

		public Device getSelectedDevice()
		{
			return device;
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
	public void testStartActivity() throws InterruptedException, ActivityStartingException, UiElementFetchingException
	{
		DeviceParameters selectionParameters = new DeviceParameters();
		DeviceTestClass testClass = new DeviceTestClass(selectionParameters);

		Device testDevice = testClass.getSelectedDevice();
		setTestDevice(testDevice);
		setupAndStartMainActivity();
	}
}
