package com.musala.atmosphere.agent;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.musala.atmosphere.agent.util.AgentPropertiesLoader;
import com.musala.atmosphere.commons.CommandFailedException;
import com.musala.atmosphere.commons.DeviceInformation;
import com.musala.atmosphere.commons.sa.DeviceParameters;
import com.musala.atmosphere.commons.sa.IAgentManager;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.commons.sa.RmiStringConstants;
import com.musala.atmosphere.commons.sa.exceptions.ADBridgeFailException;
import com.musala.atmosphere.commons.sa.exceptions.DeviceNotFoundException;
import com.musala.atmosphere.commons.sa.exceptions.NotPossibleForDeviceException;

/**
 * Class for managing the testing environment. Creates an Agent on the current computer and gives the user the RMI stub
 * to invoke methods. The user can use this class to create and start emulators if no device is present on this
 * computer. Getting the underlying Registry object is also possible so the user can extend the logic by getting
 * apropriate devices and so on.
 * 
 * @author georgi.gaydarov
 * 
 */
public class AgentIntegrationEnvironment
{
	private AgentManager agentManager;

	private IAgentManager remoteAgentManager;

	private Registry remoteAgentRegistry;

	private List<String> createdEmulatorsSerialNumbers = new LinkedList<String>();

	/**
	 * Starts a new agent with an RMI registry opened on a specified port.
	 * 
	 * @param rmiPort
	 *        agent RMI registry port.
	 * @throws RemoteException
	 * @throws ADBridgeFailException
	 * @throws NotBoundException
	 */
	public AgentIntegrationEnvironment(int rmiPort) throws RemoteException, ADBridgeFailException, NotBoundException
	{
		agentManager = new AgentManager(AgentPropertiesLoader.getADBPath(), rmiPort);

		remoteAgentRegistry = LocateRegistry.getRegistry("localhost", rmiPort);
		remoteAgentManager = (IAgentManager) remoteAgentRegistry.lookup(RmiStringConstants.AGENT_MANAGER.toString());
	}

	/**
	 * Checks if any device is present on the agent (current machine).
	 * 
	 * @return true if a device is present, false otherwise.
	 * @throws RemoteException
	 */
	public boolean isAnyDevicePresent() throws RemoteException
	{
		List<String> deviceWrappers = agentManager.getAllDeviceWrappers();
		if (deviceWrappers.isEmpty())
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Checks if any emulator is present on the agent (current machine).
	 * 
	 * @return true if emulator is present, false otherwise.
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public boolean isAnyEmulatorPresent() throws RemoteException, NotBoundException
	{
		List<String> wrapperIdentifiers = remoteAgentManager.getAllDeviceWrappers();

		for (String wrapperId : wrapperIdentifiers)
		{
			IWrapDevice deviceWrapper = (IWrapDevice) remoteAgentRegistry.lookup(wrapperId);
			DeviceInformation deviceInformation = deviceWrapper.getDeviceInformation();

			if (deviceInformation.isEmulator())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the first available device that is present on the agent (current machine).
	 * 
	 * @return the first available device wrapper ({@link IWrapDevice} interface).
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public IWrapDevice getFirstAvailableDeviceWrapper()
		throws RemoteException,
			NotBoundException,
			IllegalStateException
	{
		List<String> wrapperIdentifiers = remoteAgentManager.getAllDeviceWrappers();

		if (wrapperIdentifiers.isEmpty())
		{
			throw new IllegalStateException("No devices are present on the current agent. Consider creating and starting an emulator.");
		}
		IWrapDevice deviceWrapper = (IWrapDevice) remoteAgentRegistry.lookup(wrapperIdentifiers.get(0));
		return deviceWrapper;
	}

	/**
	 * Gets the first available emulator device that is present on the agent (current machine).
	 * 
	 * @return the first available emulator wrapper ({@link IWrapDevice} interface).
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws IllegalStateException
	 */
	public IWrapDevice getFirstAvailableEmulatorDeviceWrapper()
		throws RemoteException,
			NotBoundException,
			IllegalStateException
	{
		List<String> wrapperIdentifiers = remoteAgentManager.getAllDeviceWrappers();

		for (String wrapperId : wrapperIdentifiers)
		{
			IWrapDevice deviceWrapper = (IWrapDevice) remoteAgentRegistry.lookup(wrapperId);
			DeviceInformation deviceInformation = deviceWrapper.getDeviceInformation();

			if (deviceInformation.isEmulator())
			{
				return deviceWrapper;
			}
		}
		throw new IllegalStateException("No emulator devices are present on the agent (current machine).");
	}

	/**
	 * Creates and starts an emulator with specified parameters.
	 * 
	 * @param emulatorCreationParameters
	 *        parameters for the emulator that is to be created.
	 * @return the device wrapper ({@link IWrapDevice} interface) for the newly created emulator.
	 * @throws NotBoundException
	 * @throws IOException
	 */
	public IWrapDevice startEmulator(DeviceParameters emulatorCreationParameters)
		throws IOException,
			NotBoundException,
			TimeoutException
	{
		List<String> wrappersBeforeEmulatorCreation = remoteAgentManager.getAllDeviceWrappers();

		remoteAgentManager.createAndStartEmulator(emulatorCreationParameters);

		int timeout = AgentPropertiesLoader.getEmulatorCreationWaitTimeout();

		try
		{
			// FIXME This method should be moved in the EmulatorManager
			do
			{
				LinkedList<String> currentWrappers = new LinkedList<String>(remoteAgentManager.getAllDeviceWrappers());
				currentWrappers.removeAll(wrappersBeforeEmulatorCreation);

				for (String currentWrapperId : currentWrappers)
				{
					IWrapDevice newDeviceWrapper = (IWrapDevice) remoteAgentRegistry.lookup(currentWrapperId);
					DeviceInformation newDeviceInformation = newDeviceWrapper.getDeviceInformation();

					if (newDeviceInformation.isEmulator())
					{
						// this must be our newly created emulator.
						String createdEmulatorSerialNumber = newDeviceInformation.getSerialNumber();
						createdEmulatorsSerialNumbers.add(createdEmulatorSerialNumber);

						return newDeviceWrapper;
					}
				}

				Thread.sleep(1000);
				timeout -= 1000;
			} while (timeout > 0);
		}
		catch (InterruptedException e)
		{
			// Sleep interupted. Doesn't reflect the logic.
		}

		throw new TimeoutException("Waiting for device to appear in the wrappers list timed out.");
	}

	/**
	 * Blocks until the wrapped device has it's graphical environment loaded. Working with a device which has not loaded
	 * will result in undefined behavior for some tasks and is <b>strongly unadvised</b>.
	 * 
	 * @param deviceWrapper
	 * @throws RemoteException
	 */
	public void waitForDeviceOsToStart(IWrapDevice deviceWrapper) throws RemoteException
	{
		// Wait for the device to become ready
		boolean deviceReady = false;

		while (!deviceReady)
		{
			try
			{
				Thread.sleep(100);

				// TODO add a method in the IWrapDevice interface to do this a better way
				// This will return if the boot animation is running
				String response = deviceWrapper.executeShellCommand("getprop init.svc.bootanim");

				// When the boot animation finishes this property will be set to stopped.
				if (!response.contains("stopped"))
				{
					continue;
				}

				deviceReady = true;
			}
			catch (CommandFailedException e)
			{
				// command could not be sent, so the device must be still offline.
			}
			catch (InterruptedException e)
			{
				// Sleep was interrupted. No certain meening in this case.
			}
		}
	}

	/**
	 * Closes the Agent integration environment. <b> MUST BE CALLED AT THE END OF THE TEST OR WHEN THE ENVIRONMENT IS NO
	 * LONGER NEEDED.</b>
	 * 
	 * @throws DeviceNotFoundException
	 * @throws IOException
	 * @throws NotPossibleForDeviceException
	 */
	public void close() throws IOException, DeviceNotFoundException, NotPossibleForDeviceException
	{
		for (String createdEmulatorSerialNumber : createdEmulatorsSerialNumbers)
		{
			remoteAgentManager.eraseEmulator(createdEmulatorSerialNumber);
		}

		if (agentManager != null)
		{
			agentManager.close();
		}
	}

	public void connectToLocalhostServer(int port) throws RemoteException, NotBoundException
	{
		agentManager.connectToServer("localhost", port);
	}

	/**
	 * Gets the underlying {@link AgentManager AgentManager} instance ID.
	 * 
	 * @return Agent ID.
	 * @throws RemoteException
	 */
	public String getUnderlyingAgentId() throws RemoteException
	{
		String agentId = agentManager.getAgentId();
		return agentId;
	}

	/**
	 * 
	 * @return current environment agent manager
	 */
	public AgentManager getAgentManager()
	{
		return agentManager;
	}
}
