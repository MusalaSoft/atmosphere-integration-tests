package com.musala.atmosphere.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import com.musala.atmosphere.commons.sa.exceptions.DeviceNotFoundException;
import com.musala.atmosphere.commons.sa.exceptions.NotPossibleForDeviceException;
import com.musala.atmosphere.server.pool.PoolManager;

public class ServerIntegrationEnvironment
{
	private static final int AGENT_CONNECTION_CYCLE_WAIT = 300;

	private static final int DEVICE_PRESENCE_CYCLE_WAIT = 300;

	private ServerManager serverManager;

	private PoolManager poolManager;

	public ServerIntegrationEnvironment(int serverRmiPort) throws RemoteException
	{
		serverManager = new ServerManager(serverRmiPort);
		poolManager = PoolManager.getInstance();
	}

	public void close() throws IOException, DeviceNotFoundException, NotPossibleForDeviceException
	{
		serverManager.close();
	}

	public boolean isAgentWithIdConnected(String agentId)
	{
		List<String> connectedAgentIds = serverManager.getAllConnectedAgentIds();
		boolean connected = connectedAgentIds.contains(agentId);
		return connected;
	}

	public boolean hasAgentConnected()
	{
		boolean result = !serverManager.getAllConnectedAgentIds().isEmpty();
		return result;
	}

	public void waitForAgentToConnect()
	{
		while (!hasAgentConnected())
		{
			try
			{
				Thread.sleep(AGENT_CONNECTION_CYCLE_WAIT);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void waitForAgentConnection(String agentId)
	{
		while (!isAgentWithIdConnected(agentId))
		{
			try
			{
				Thread.sleep(AGENT_CONNECTION_CYCLE_WAIT);
			}
			catch (InterruptedException e)
			{
				// Wait was interrupted, no one cares. Nothing to do here.
				e.printStackTrace();
			}
		}
	}

	public void waitForDeviceToBeAvailable(String deviceId, String agentId)
	{
		while (!poolManager.isDevicePresent(deviceId, agentId))
		{
			try
			{
				Thread.sleep(DEVICE_PRESENCE_CYCLE_WAIT);
			}
			catch (InterruptedException e)
			{
				// Wait was interrupted, no one cares. Nothing to do here.
				e.printStackTrace();
			}
		}
	}

	public ServerManager getServerManager()
	{
		return serverManager;
	}
}
