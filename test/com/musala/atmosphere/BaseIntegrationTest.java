package com.musala.atmosphere;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;

import com.musala.atmosphere.agent.AgentIntegrationEnvironment;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.server.ServerIntegrationEnvironment;
import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;

/**
 * 
 * 
 * @author valyo.yolovski
 * 
 */
public class BaseIntegrationTest
{
	protected final static int SERVER_MANAGER_RMI_PORT = 2099;

	protected final static String ONDEVICEVALIDATOR_FILE = "OnDeviceValidator.apk";

	protected final static String PATH_TO_APK_DIR = "./";

	final static String PATH_TO_APK = PATH_TO_APK_DIR + ONDEVICEVALIDATOR_FILE;

	protected final static String VALIDATOR_APP_PACKAGE = "com.musala.atmosphere.ondevice.validator";

	protected final static String VALIDATOR_APP_ACTIVITY = "MainActivity";

	protected static Device testDevice;

	protected static AgentIntegrationEnvironment agentIntegrationEnvironment = AtmosphereIntegrationTestsSuite.getAgentEnvironment();

	protected static ServerIntegrationEnvironment serverIntegrationEnvironment = AtmosphereIntegrationTestsSuite.getServerEnvironment();

	protected static void initTestDevice(DeviceParameters parameters)
	{
		GettingBuilderClass builderGet = new GettingBuilderClass();
		Builder deviceBuilder = builderGet.getBuilder();
		if (testDevice != null)
		{
			deviceBuilder.releaseDevice(testDevice);
		}

		testDevice = deviceBuilder.getDevice(parameters);
		assertNotNull("Could not get a device.", testDevice);
	}

	@Server(ip = "localhost", port = SERVER_MANAGER_RMI_PORT)
	private static class GettingBuilderClass
	{
		public GettingBuilderClass()
		{
		}

		public Builder getBuilder()
		{
			Builder classDeviceBuilder = Builder.getInstance();
			return classDeviceBuilder;
		}
	}

	protected static void installValidatorApplication()
	{
		assertNotNull("There is no allocated test device.", testDevice);

		testDevice.installAPK(PATH_TO_APK);
		setTestDevice(testDevice);
	}

	@AfterClass
	public static void releaseDevice()
	{
		GettingBuilderClass builderGet = new GettingBuilderClass();
		Builder deviceBuilder = builderGet.getBuilder();

		if (testDevice != null)
		{
			deviceBuilder.releaseDevice(testDevice);
			testDevice = null;
		}
	}
}
