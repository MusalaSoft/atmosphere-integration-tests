package com.musala.atmosphere.testsuites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.agent.devicewrapper.EmulatorConsoleTest;
import com.musala.atmosphere.agent.devicewrapper.NetworkConnectionTest;
import com.musala.atmosphere.agent.devicewrapper.ServiceConnectionTest;
import com.musala.atmosphere.client.builder.BuilderIntegrationTest;
import com.musala.atmosphere.commons.sa.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.commons.util.Pair;
import com.musala.atmosphere.server.EmulatorCreationTest;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;

/**
 * JUnit test suite with all failing integration tests.
 * 
 * @author valyo.yolovski
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({BuilderIntegrationTest.class, EmulatorConsoleTest.class, EmulatorCreationTest.class,
		NetworkConnectionTest.class, ServiceConnectionTest.class})
public class AtmosphereFailingIntegrationTestsSuite
{
	private final static int SERVER_MANAGER_RMI_PORT = 2099;

	private final static int AGENTMANAGER_RMI_PORT = 2000;

	private static AgentIntegrationEnvironmentCreator agentEnvironment;

	private static ServerIntegrationEnvironmentCreator serverEnvironment;

	private static final int EMULATOR_CREATION_DPI = 120;

	private static final int EMULATOR_CREATION_RAM = 256;

	private static final int EMULATOR_CREATION_RESOLUTION_H = 240;

	private static final int EMULATOR_CREATION_RESOLUTION_W = 320;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		// Master setup.
		agentEnvironment = new AgentIntegrationEnvironmentCreator(AGENTMANAGER_RMI_PORT);
		serverEnvironment = new ServerIntegrationEnvironmentCreator(SERVER_MANAGER_RMI_PORT);

		if (!agentEnvironment.isAnyDevicePresent()) // Create emulator if no real device is available.
		{
			DeviceParameters emulatorCreationParameters = new DeviceParameters();
			emulatorCreationParameters.setDpi(EMULATOR_CREATION_DPI);
			emulatorCreationParameters.setRam(EMULATOR_CREATION_RAM);
			emulatorCreationParameters.setResolution(new Pair<Integer, Integer>(EMULATOR_CREATION_RESOLUTION_H,
																				EMULATOR_CREATION_RESOLUTION_W));
			createAndPublishEmulator(emulatorCreationParameters);
		}
		agentEnvironment.connectToLocalhostServer(SERVER_MANAGER_RMI_PORT);
		String agentId = agentEnvironment.getUnderlyingAgentId();
		serverEnvironment.waitForAgentConnection(agentId);
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
		// Master tear down.
		serverEnvironment.close();
		agentEnvironment.close();
	}

	public static AgentIntegrationEnvironmentCreator getAgentIntegrationEnvironmentCreator()
	{
		return agentEnvironment;
	}

	public static ServerIntegrationEnvironmentCreator getServerIntegrationEnvironmentCreator()
	{
		return serverEnvironment;
	}

	public static void createAndPublishEmulator(DeviceParameters parameters) throws Exception
	{
		IWrapDevice createdEmulator = agentEnvironment.startEmulator(parameters);
		agentEnvironment.waitForDeviceOsToStart(createdEmulator);
	}
}
