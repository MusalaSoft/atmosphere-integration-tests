package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;

// FIXME I need heavy refactoring!

public class ServiceConnectionTest extends BaseIntegrationTest
{
	private static final String deviceSerialNumber = "emulator-5554";

	private static final String PATH_TO_SERVICE_APK = "C:\\prj\\ARDC\\Source\\AtmosphereIntegrationTests\\AtmophereService.apk";

	private static AgentIntegrationEnvironmentCreator agentEnvironment = AtmosphereIntegrationTestsSuite.getAgentIntegrationEnvironmentCreator();

	private static IWrapDevice wrappedTestDevice;

	private static final String STOP_ATMOSPHERE_SERVICE = "adb am broadcast -a com.musala.atmosphere.service.SERVICE_CONTROLL --es command stop";

	@BeforeClass
	public static void setUp() throws Exception
	{

		wrappedTestDevice = agentEnvironment.getFirstAvailableEmulatorDeviceWrapper();
	}

	@Test
	public void testServiceConnectionToRunningService() throws RemoteException
	{
		assertTrue("Service communication has not been initialized.", wrappedTestDevice.validateServiceCommunication());
	}

}
