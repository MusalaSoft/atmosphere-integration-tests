package com.musala.atmosphere.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import com.musala.atmosphere.commons.sa.exceptions.DeviceNotFoundException;
import com.musala.atmosphere.commons.sa.exceptions.NotPossibleForDeviceException;

public class ServerIntegrationEnvironmentCreator
{
	private static final int AGENT_CONNECTION_CYCLE_WAIT = 300;

	private static final int DEVICE_PRESENCE_CYCLE_WAIT = 300;

	private PoolManager poolManager;

	public ServerIntegrationEnvironmentCreator(int serverRmiPort) throws RemoteException
	{
		poolManager = new PoolManager(serverRmiPort);
	}

	public PoolManager getPoolManager()
	{
		return poolManager;
	}

	public void close() throws IOException, DeviceNotFoundException, NotPossibleForDeviceException
	{
		poolManager.close();
	}

	public boolean isAgentWithIdConnected(String agentId)
	{
		List<String> connectedAgentIds = poolManager.getAllConnectedAgentIds();
		boolean connected = connectedAgentIds.contains(agentId);
		return connected;
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
		while (!poolManager.isSuchDeviceProxyPresent(agentId, deviceId))
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
}
