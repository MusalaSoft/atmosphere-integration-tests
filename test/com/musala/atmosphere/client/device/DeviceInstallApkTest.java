package com.musala.atmosphere.client.device;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.InstallException;
import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.agent.AgentManager;
import com.musala.atmosphere.agent.DevicePropertyStringConstants;
import com.musala.atmosphere.agent.util.FakeServiceAnswer;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;

public class DeviceInstallApkTest extends BaseIntegrationTest
{
	private final static int SERVERMANAGER_RMI_PORT = 2099;

	private final static String PATH_TO_APK_DIR = "./";

	private final static String NAME_OF_APK_FILE = "Eventrix.apk";

	private final static String PATH_TO_APK = PATH_TO_APK_DIR + NAME_OF_APK_FILE;

	private final static String MOCK_SERIAL_NUMBER = "mockedDevice";

	private final static Integer MOCK_DEVICE_DENSITY = 666;

	private IDevice mockDevice;

	@Server(ip = "localhost", port = SERVERMANAGER_RMI_PORT, connectionRetryLimit = 10)
	private class GettingDeviceSampleClass
	{
		public GettingDeviceSampleClass()
		{
		}

		public void getDeviceAndInstallApk(DeviceParameters parameters)
		{
			Builder builder = Builder.getInstance();
			Device device = builder.getDevice(parameters);
			device.installAPK(PATH_TO_APK);
		}
	}

	@Before
	public void setUp() throws Exception
	{
		agentIntegrationEnvironment = AtmosphereIntegrationTestsSuite.getAgentEnvironment();
		serverIntegrationEnvironment = AtmosphereIntegrationTestsSuite.getServerEnvironment();

		mockDevice = mock(IDevice.class);
		when(mockDevice.getSerialNumber()).thenReturn(MOCK_SERIAL_NUMBER);
		when(mockDevice.isEmulator()).thenReturn(false);
		when(mockDevice.arePropertiesSet()).thenReturn(true);
		when(mockDevice.isOffline()).thenReturn(false);

		FakeServiceAnswer fakeServiceAnswer = new FakeServiceAnswer();
		Mockito.doAnswer(fakeServiceAnswer).when(mockDevice).createForward(anyInt(), anyInt());

		Map<String, String> mockDeviceProperties = new HashMap<String, String>();
		mockDeviceProperties.put(	DevicePropertyStringConstants.PROPERTY_EMUDEVICE_LCD_DENSITY.toString(),
									Integer.toString(MOCK_DEVICE_DENSITY));
		when(mockDevice.getProperties()).thenReturn(mockDeviceProperties);

		AgentManager am = agentIntegrationEnvironment.getAgentManager();
		Method agentManagerRegisterNewDeviceMethod = AgentManager.class.getDeclaredMethod(	"registerDeviceOnAgent",
																							IDevice.class);
		agentManagerRegisterNewDeviceMethod.setAccessible(true);

		agentManagerRegisterNewDeviceMethod.invoke(am, mockDevice);
		agentIntegrationEnvironment.connectToLocalhostServer(SERVERMANAGER_RMI_PORT);

		String agentId = agentIntegrationEnvironment.getUnderlyingAgentId();
		serverIntegrationEnvironment.waitForAgentConnection(agentId);

		serverIntegrationEnvironment.waitForDeviceToBeAvailable(MOCK_SERIAL_NUMBER, agentId);
	}

	@After
	public void tearDown() throws Exception
	{
		AgentManager am = agentIntegrationEnvironment.getAgentManager();
		Method agentManagerUnregisterDeviceMethod = AgentManager.class.getDeclaredMethod(	"unregisterDeviceOnAgent",
																							IDevice.class);
		agentManagerUnregisterDeviceMethod.setAccessible(true);
		agentManagerUnregisterDeviceMethod.invoke(am, mockDevice);
	}

	@Test
	public void testTransferringApk() throws InstallException
	{
		when(mockDevice.installPackage(anyString(), anyBoolean())).thenAnswer(new Answer<String>()
		{
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable
			{
				String filePath = (String) invocation.getArguments()[0];

				File expectedFile = new File(PATH_TO_APK);
				File transferredFile = new File(filePath);

				// if sizes of files doesn't match there is error somewhere in the transferring mechanism
				assertEquals("Expected file size does not match.", expectedFile.length(), transferredFile.length());

				byte[] expectedContent = Files.readAllBytes(expectedFile.toPath());
				byte[] realContent = Files.readAllBytes(transferredFile.toPath());

				MessageDigest md = MessageDigest.getInstance("md5");
				String md51 = (new HexBinaryAdapter()).marshal(md.digest(expectedContent));
				String md52 = (new HexBinaryAdapter()).marshal(md.digest(realContent));

				assertEquals("Transferred file is not as expected.", md51, md52);

				return null;
			}
		});

		DeviceParameters parameters = new DeviceParameters();
		parameters.setSerialNumber(MOCK_SERIAL_NUMBER);
		GettingDeviceSampleClass userTest = new GettingDeviceSampleClass();
		userTest.getDeviceAndInstallApk(parameters);

		verify(mockDevice, times(1)).installPackage(anyString(), anyBoolean());
	}
}
