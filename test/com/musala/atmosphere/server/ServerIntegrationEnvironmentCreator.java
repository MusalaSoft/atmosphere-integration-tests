package com.musala.atmosphere.server;

import java.io.IOException;
import java.rmi.RemoteException;

import com.musala.atmosphere.commons.sa.exceptions.DeviceNotFoundException;
import com.musala.atmosphere.commons.sa.exceptions.NotPossibleForDeviceException;

public class ServerIntegrationEnvironmentCreator
{
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
}
