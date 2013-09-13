package com.musala.atmosphere.server;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.server.pool.PoolManager;
import com.musala.atmosphere.testsuites.AtmosphereFailingIntegrationTestsSuite;
import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;

public class EmulatorCreationTest
{
	private static ServerIntegrationEnvironmentCreator serverEnvironment;

	private static AgentIntegrationEnvironmentCreator agentEnvironment;

	private ServerManager serverManager = serverEnvironment.getServerManager();

	private PoolManager poolManager = PoolManager.getInstance();

	private static final int MAX_TIMEOUT = 240; // max timeout in seconds for waiting for a newly created emulator
												// to be registered on the Agent

	private com.musala.atmosphere.commons.sa.DeviceParameters getEmulatorDeviceParameters()
	{
		com.musala.atmosphere.commons.sa.DeviceParameters params = new com.musala.atmosphere.commons.sa.DeviceParameters();
		params.setDpi(120);
		params.setApiLevel(17);
		params.setRam(256);
		Pair<Integer, Integer> resolution = new Pair<>(320, 240);
		params.setResolution(resolution);
		return params;
	}

	/**
	 * Removes all created virtual devices and emulators during the execution of this test.
	 * 
	 * @param initialListOfDevices
	 *        - list of all connected to the Agent running emulators before the test run.
	 * @param listOfDevicesAfterTest
	 *        - list of all connected devices to the Agent <u>after</u> the test is executed.
	 * @throws Exception
	 */
	private void discardCreatedEmulators(List<String> initialListOfDevices, List<String> listOfDevicesAfterTest)
		throws Exception
	{
		int numberOfDevicesAfterTest = listOfDevicesAfterTest.size();

		for (int indexOfDevice = numberOfDevicesAfterTest - 1; indexOfDevice >= 0; indexOfDevice--)
		{
			String currentDeviceId = listOfDevicesAfterTest.get(indexOfDevice);
			if (!initialListOfDevices.contains(currentDeviceId))
			{
				poolManager.refreshDevice(currentDeviceId, agentEnvironment.getUnderlyingAgentId(), false);
			}
		}
	}

	@BeforeClass
	public static void setUp() throws RemoteException, InterruptedException
	{
		
		serverEnvironment = AtmosphereIntegrationTestsSuite.getServerIntegrationEnvironmentCreator();
		agentEnvironment = AtmosphereIntegrationTestsSuite.getAgentIntegrationEnvironmentCreator();
	}

	@Test
	public void createEmulatorTest() throws Exception
	{
		List<String> initialListOfDevices = poolManager.getAllUnderlyingDeviceProxyIds();
		int initialNumberOfDevices = initialListOfDevices.size();
		int expectedNumberOfDevices = initialNumberOfDevices + 1;

		com.musala.atmosphere.commons.sa.DeviceParameters params = getEmulatorDeviceParameters();
		AtmosphereFailingIntegrationTestsSuite.createAndPublishEmulator(params);

		// FIXME This timeout mechanism should be revised.
		for (int timeout = MAX_TIMEOUT; timeout > 0; timeout--)
		{
			Thread.sleep(1000);
			if (poolManager.getAllUnderlyingDeviceProxyIds().size() != initialNumberOfDevices)
			{
				break;
			}
		}

		int actualNumberOfDevices = poolManager.getAllUnderlyingDeviceProxyIds().size();
		assertEquals(	"Number of expected and actual devices doesn't match.",
						expectedNumberOfDevices,
						actualNumberOfDevices);

		List<String> newListOfDevices = poolManager.getAllUnderlyingDeviceProxyIds();
		discardCreatedEmulators(initialListOfDevices, newListOfDevices);
	}
}
