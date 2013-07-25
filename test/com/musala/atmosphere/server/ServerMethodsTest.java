package com.musala.atmosphere.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.agent.Agent;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceOs;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

// FIXME vlado should fix the connection sequence here.
public class ServerMethodsTest
{

	private static final int SERVER_PORT = 1980;

	private static Server testedServer;

	private static Agent localAgent;

	private static int AGENT_PORT = 1990;

	private static final int MAX_TIMEOUT = 160; // max timeout in seconds for waiting for a newly created emulator
												// to be registered on the Agent

	/**
	 * Used to create some Device parameters, for the need of creating emulators.
	 * 
	 * @return
	 */
	private DeviceParameters setSomeDeviceParameters()
	{
		DeviceParameters params = new DeviceParameters();
		params.setDeviceType(DeviceType.EMULATOR_ONLY);
		params.setDpi(100);
		params.setOs(DeviceOs.JELLY_BEAN_4_1);
		params.setRam(1024);
		params.setResolutionHeight(200);
		params.setResolutionWidth(200);

		return params;
	}

	/**
	 * Removes all created virtual devices and emulators during the execution of some test.
	 * 
	 * @param initialListOfDevices
	 *        - list of all connected to the Agent running emulators before the test run.
	 * @param newListOfDevices
	 *        - list of all connected devices to the Agent <u>after</u> the test is executed.
	 * @throws InterruptedException
	 */
	private void discardCreatedEmulators(List<String> initialListOfDevices, List<String> newListOfDevices)
		throws InterruptedException
	{
		int newNumberOfDevices = newListOfDevices.size();
		for (int indexOfDevice = newNumberOfDevices - 1; indexOfDevice >= 0; indexOfDevice--)
		{
			String currentDeviceId = newListOfDevices.get(indexOfDevice);
			if (initialListOfDevices.contains(currentDeviceId) == false)
			{
				try
				{
					localAgent.removeEmulatorBySerialNumber(currentDeviceId);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@BeforeClass
	public static void setUp() throws RemoteException, InterruptedException
	{
		localAgent = new Agent(AGENT_PORT);
		localAgent.startAgentThread();
		testedServer = new Server(SERVER_PORT);
		// testedServer.addAgentToServer("localhost", AGENT_PORT);
		testedServer.startServerThread(false);
	}

	@AfterClass
	public static void tearDown() throws InterruptedException
	{
		testedServer.stop();
		localAgent.stop();
	}

	@Test
	public void getAllAccessibleDevicesTest() throws NotBoundException, IOException, InterruptedException
	{
		List<String> initialListOfDevices = testedServer.getAllAccessibleDevices();
		int initialNumberOfDevices = initialListOfDevices.size();

		final int NUMBER_OF_CREATED_EMULATORS = 2;
		DeviceParameters params = setSomeDeviceParameters();

		for (int createdEmulatorNumber = 1; createdEmulatorNumber <= NUMBER_OF_CREATED_EMULATORS; createdEmulatorNumber++)
		{
			int currentNumberOfDevices = testedServer.getAllAccessibleDevices().size();
			testedServer.createEmulatorOnAgent("localhost", AGENT_PORT, params);
			for (int timeout = MAX_TIMEOUT; timeout > 0; timeout--)
			{
				Thread.sleep(1000);
				if (testedServer.getAllAccessibleDevices().size() != currentNumberOfDevices)
				{
					break;
				}
			}
		}

		int expectedNumberOfDevices = initialNumberOfDevices + NUMBER_OF_CREATED_EMULATORS;

		assertEquals(	"Number of available devices on all agents doesn't match",
						expectedNumberOfDevices,
						testedServer.getAllAccessibleDevices().size());
		discardCreatedEmulators(initialListOfDevices, testedServer.getAllAccessibleDevices());
	}

	@Test
	public void createEmulatorTest() throws NotBoundException, IOException, InterruptedException
	{
		List<String> initialListOfDevices = testedServer.getAllAccessibleDevices();
		int initialNumberOfDevices = initialListOfDevices.size();
		int expectedNumberOfDevices = initialNumberOfDevices + 1;

		DeviceParameters params = setSomeDeviceParameters();
		testedServer.createEmulator(params);

		for (int timeout = MAX_TIMEOUT; timeout > 0; timeout--)
		{
			Thread.sleep(1000);
			if (testedServer.getAllAccessibleDevices().size() != initialNumberOfDevices)
			{
				break;
			}
		}

		assertEquals(	"Number of available devices on all agents doesn't match",
						expectedNumberOfDevices,
						testedServer.getAllAccessibleDevices().size());

		List<String> newListOfDevices = testedServer.getAllAccessibleDevices();
		discardCreatedEmulators(initialListOfDevices, newListOfDevices);
	}

	@Test
	public void createEmulatorOnAgentTest() throws NotBoundException, IOException, InterruptedException
	{
		List<String> initialListOfDevices = testedServer.getAllAccessibleDevices();
		int initialNumberOfDevices = initialListOfDevices.size();
		DeviceParameters params = setSomeDeviceParameters();
		testedServer.createEmulatorOnAgent("localhost", AGENT_PORT, params);

		int expectedNumberOfDevices = initialNumberOfDevices + 1;

		for (int timeout = MAX_TIMEOUT; timeout > 0; timeout--)
		{
			Thread.sleep(1000);
			if (testedServer.getAllAccessibleDevices().size() != initialNumberOfDevices)
			{
				break;
			}
		}

		assertEquals(	"Number of available devices on all agents doesn't match",
						expectedNumberOfDevices,
						testedServer.getAllAccessibleDevices().size());
		discardCreatedEmulators(initialListOfDevices, testedServer.getAllAccessibleDevices());
	}

	@Test
	public void getAgentAdressesListTest() throws InterruptedException
	{
		List<Pair<String, Integer>> initialAgentAdressesList = testedServer.getAgentAdressesList();
		// FIXME this is also invoked in @Before
		// a.addAgentToServer("localhost", AGENT_PORT);
		List<Pair<String, Integer>> expectedAgentAdressesList = initialAgentAdressesList;
		expectedAgentAdressesList.add(new Pair<String, Integer>("localhost", AGENT_PORT));

		assertEquals(	"Agent address is not found on the Server",
						expectedAgentAdressesList,
						testedServer.getAgentAdressesList());
		localAgent.stop();
	}

}
