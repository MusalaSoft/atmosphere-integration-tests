package com.musala.atmosphere.client.builder;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.CommandFailedException;
import com.musala.atmosphere.commons.DeviceInformation;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceOs;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.commons.sa.IAgentManager;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.commons.sa.exceptions.DeviceNotFoundException;
import com.musala.atmosphere.commons.sa.exceptions.NotPossibleForDeviceException;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;
import com.musala.atmosphere.server.ServerManager;
import com.musala.atmosphere.server.pool.PoolManager;

/**
 * 
 * @author valyo.yolovski
 * 
 */
public class BuilderDeviceSelectionIntegrationTest
{
	private final static int SERVER_RMI_PORT = 1980;

	private final static String AGENT_ID = "mockagent";

	private final static String DEVICE1_SN = "mockdevice1";

	private final static String DEVICE2_SN = "mockdevice2";

	private final static String DEVICE3_SN = "mockdevice3";

	private final static String DEVICE4_SN = "mockdevice4";

	private final static String DEVICE5_SN = "mockdevice5";

	private static ServerIntegrationEnvironmentCreator serverEnvironment = null;

	@Server(ip = "localhost", port = SERVER_RMI_PORT)
	static class GettingDeviceSampleClass
	{
		public static Builder getBuilderInstance()
		{
			return Builder.getInstance();
		}
	}

	@BeforeClass
	public static void setUp() throws Exception
	{
		serverEnvironment = new ServerIntegrationEnvironmentCreator(SERVER_RMI_PORT);

		IAgentManager mockedAgentManager = mock(IAgentManager.class);
		when(mockedAgentManager.getAgentId()).thenReturn(AGENT_ID);

		IWrapDevice mockedDeviceOne = mock(IWrapDevice.class);
		DeviceInformation mockedDeviceInfoOne = new DeviceInformation();
		mockedDeviceInfoOne.setSerialNumber(DEVICE1_SN);
		mockedDeviceInfoOne.setOs("4.2.1");
		mockedDeviceInfoOne.setEmulator(true);
		mockedDeviceInfoOne.setRam(128);
		mockedDeviceInfoOne.setResolution(new Pair<>(600, 800));
		mockedDeviceInfoOne.setDpi(120);
		when(mockedDeviceOne.getDeviceInformation()).thenReturn(mockedDeviceInfoOne);

		IWrapDevice mockedDeviceTwo = mock(IWrapDevice.class);
		DeviceInformation mockedDeviceInfoTwo = new DeviceInformation();
		mockedDeviceInfoTwo.setSerialNumber(DEVICE2_SN);
		mockedDeviceInfoTwo.setOs("4.1");
		mockedDeviceInfoTwo.setEmulator(true);
		mockedDeviceInfoTwo.setRam(256);
		mockedDeviceInfoTwo.setResolution(new Pair<>(200, 200));
		mockedDeviceInfoTwo.setDpi(240);
		when(mockedDeviceTwo.getDeviceInformation()).thenReturn(mockedDeviceInfoTwo);

		IWrapDevice mockedDeviceThree = mock(IWrapDevice.class);
		DeviceInformation mockedDeviceInfoThree = new DeviceInformation();
		mockedDeviceInfoThree.setSerialNumber(DEVICE3_SN);
		mockedDeviceInfoThree.setOs("4.0.2");
		mockedDeviceInfoThree.setEmulator(false);
		mockedDeviceInfoThree.setRam(256);
		mockedDeviceInfoThree.setResolution(new Pair<>(400, 500));
		mockedDeviceInfoThree.setDpi(80);
		when(mockedDeviceThree.getDeviceInformation()).thenReturn(mockedDeviceInfoThree);

		IWrapDevice mockedDeviceFour = mock(IWrapDevice.class);
		DeviceInformation mockedDeviceInfoFour = new DeviceInformation();
		mockedDeviceInfoFour.setSerialNumber(DEVICE4_SN);
		mockedDeviceInfoFour.setOs("4.0.1");
		mockedDeviceInfoFour.setEmulator(false);
		mockedDeviceInfoFour.setRam(512);
		mockedDeviceInfoFour.setResolution(new Pair<>(200, 200));
		mockedDeviceInfoFour.setDpi(180);
		when(mockedDeviceFour.getDeviceInformation()).thenReturn(mockedDeviceInfoFour);

		IWrapDevice mockedDeviceFive = mock(IWrapDevice.class);
		DeviceInformation mockedDeviceInfoFive = new DeviceInformation();
		mockedDeviceInfoFive.setSerialNumber(DEVICE5_SN);
		mockedDeviceInfoFive.setOs("4.2.1");
		mockedDeviceInfoFive.setEmulator(true);
		mockedDeviceInfoFive.setRam(512);
		mockedDeviceInfoFive.setResolution(new Pair<>(200, 200));
		mockedDeviceInfoFive.setDpi(180);
		when(mockedDeviceFive.getDeviceInformation()).thenReturn(mockedDeviceInfoFive);

		ServerManager serverManager = serverEnvironment.getServerManager();

		PoolManager poolManager = PoolManager.getInstance(serverManager);

		poolManager.addDevice(DEVICE1_SN, mockedDeviceOne, mockedAgentManager, SERVER_RMI_PORT);

		poolManager.addDevice(DEVICE2_SN, mockedDeviceTwo, mockedAgentManager, SERVER_RMI_PORT);

		poolManager.addDevice(DEVICE3_SN, mockedDeviceThree, mockedAgentManager, SERVER_RMI_PORT);

		poolManager.addDevice(DEVICE4_SN, mockedDeviceFour, mockedAgentManager, SERVER_RMI_PORT);

		poolManager.addDevice(DEVICE5_SN, mockedDeviceFive, mockedAgentManager, SERVER_RMI_PORT);
	}

	@AfterClass
	public static void tearDown() throws IOException, DeviceNotFoundException, NotPossibleForDeviceException
	{
		serverEnvironment.close();
	}

	private boolean parametersMatchInformation(	DeviceParameters wantedDeviceParameters,
												com.musala.atmosphere.client.DeviceInformation realDeviceInformation)
	{

		DeviceType wantedDeviceType = wantedDeviceParameters.getDeviceType();
		if (wantedDeviceType != DeviceParameters.DEVICE_TYPE_NO_PREFERENCE)
		{
			boolean isDeviceEmulator = realDeviceInformation.isEmulator();

			if (wantedDeviceType == DeviceType.EMULATOR_ONLY && !isDeviceEmulator)
			{
				return false;
			}

			if (wantedDeviceType == DeviceType.DEVICE_ONLY && isDeviceEmulator)
			{
				return false;
			}
		}

		if (wantedDeviceParameters.getDpi() != DeviceParameters.DPI_NO_PREFERENCE)
		{
			int realDeviceDpi = realDeviceInformation.getDpi();
			int wantedDeviceDpi = wantedDeviceParameters.getDpi();
			if (realDeviceDpi != wantedDeviceDpi)
			{
				return false;
			}
		}
		if (wantedDeviceParameters.getOs() != DeviceParameters.DEVICE_OS_NO_PREFERENCE)
		{
			if (!wantedDeviceParameters.getOs().toString().equals(realDeviceInformation.getOS()))
			{
				return false;
			}
		}

		if (wantedDeviceParameters.getRam() != DeviceParameters.RAM_NO_PREFERENCE)
		{
			if (wantedDeviceParameters.getRam() != realDeviceInformation.getRam())
			{
				return false;
			}
		}

		boolean hasResolutionHeightPreference = (wantedDeviceParameters.getResolutionHeight() == DeviceParameters.RESOLUTION_HEIGHT_NO_PREFERENCE);
		boolean hasResolutionWidthPreference = (wantedDeviceParameters.getResolutionWidth() != DeviceParameters.RESOLUTION_WIDTH_NO_PREFERENCE);

		if (!hasResolutionHeightPreference && !hasResolutionWidthPreference)
		{
			Integer resolutionHeight = realDeviceInformation.getResolution().getKey();
			Integer resolutionWidth = realDeviceInformation.getResolution().getValue();

			boolean hasResolutionHeightsMatch = wantedDeviceParameters.getResolutionHeight() == resolutionHeight;
			boolean hasResolutionWidthMatch = wantedDeviceParameters.getResolutionWidth() == resolutionWidth;

			if (!hasResolutionHeightsMatch && !hasResolutionWidthMatch)
			{
				return false;
			}
		}
		return true;
	}

	@Test
	public void getMockedDeviceOneTest()
	{

		DeviceParameters parameters = new DeviceParameters();
		Builder builder = GettingDeviceSampleClass.getBuilderInstance();

		parameters.setRam(128);
		parameters.setDpi(120);

		Device receivedDevice = builder.getDevice(parameters);
		com.musala.atmosphere.client.DeviceInformation information = receivedDevice.getInformation();

		assertTrue(	"Wanted device and returned device parameters don't match.",
					parametersMatchInformation(parameters, information));

		builder.releaseDevice(receivedDevice);

	}

	@Test
	public void getMockedDeviceTwoTest()
	{
		Builder builder = GettingDeviceSampleClass.getBuilderInstance();
		DeviceParameters parameters = new DeviceParameters();
		parameters.setDeviceType(DeviceType.EMULATOR_PREFERRED);
		parameters.setOs(DeviceOs.JELLY_BEAN_4_1);
		parameters.setRam(256);

		Device receivedDevice = builder.getDevice(parameters);
		com.musala.atmosphere.client.DeviceInformation information = receivedDevice.getInformation();

		assertTrue(	"Wanted device and returned device parameters don't match.",
					parametersMatchInformation(parameters, information));

		builder.releaseDevice(receivedDevice);
	}

	@Test
	public void getMockedDeviceThreeTest()
	{

		Builder builder = GettingDeviceSampleClass.getBuilderInstance();
		DeviceParameters parameters = new DeviceParameters();
		parameters.setDpi(80);
		parameters.setRam(256);

		Device receivedDevice = builder.getDevice(parameters);
		com.musala.atmosphere.client.DeviceInformation information = receivedDevice.getInformation();

		assertTrue(	"Wanted device and returned device parameters don't match.",
					parametersMatchInformation(parameters, information));

		builder.releaseDevice(receivedDevice);
	}

	@Test
	public void getMockedDeviceFourTest()
		throws RemoteException,
			CommandFailedException,
			NoSuchFieldException,
			SecurityException
	{
		Builder builder = GettingDeviceSampleClass.getBuilderInstance();
		DeviceParameters parameters = new DeviceParameters();
		parameters.setDeviceType(DeviceType.DEVICE_ONLY);
		parameters.setRam(512);

		Device receivedDevice = builder.getDevice(parameters);
		com.musala.atmosphere.client.DeviceInformation information = receivedDevice.getInformation();

		assertTrue(	"Wanted device and returned device parameters don't match.",
					parametersMatchInformation(parameters, information));
		builder.releaseDevice(receivedDevice);
	}

	@Test
	public void getMockedDeviceFiveTest()
	{
		Builder builder = GettingDeviceSampleClass.getBuilderInstance();

		DeviceParameters parameters = new DeviceParameters();
		parameters.setDeviceType(DeviceType.DEVICE_PREFERRED);
		parameters.setRam(512);
		parameters.setResolutionHeight(200);
		parameters.setResolutionWidth(200);
		parameters.setDpi(180);
		parameters.setOs(DeviceOs.JELLY_BEAN_MR1_4_2_1);

		Device receivedDevice = builder.getDevice(parameters);
		com.musala.atmosphere.client.DeviceInformation information = receivedDevice.getInformation();

		assertTrue(	"Wanted device and returned device parameters don't match.",
					parametersMatchInformation(parameters, information));

		builder.releaseDevice(receivedDevice);
	}

	@Test
	public void getMockedDeviceFourOrFiveTest()
		throws RemoteException,
			CommandFailedException,
			NoSuchFieldException,
			SecurityException
	{
		Builder builder = GettingDeviceSampleClass.getBuilderInstance();
		DeviceParameters parameters = new DeviceParameters();
		parameters.setRam(512);

		Device receivedDevice = builder.getDevice(parameters);
		com.musala.atmosphere.client.DeviceInformation information = receivedDevice.getInformation();

		assertTrue(	"Wanted device and returned device parameters don't match.",
					parametersMatchInformation(parameters, information));

		builder.releaseDevice(receivedDevice);
	}

	@Test(expected = NoSuchElementException.class)
	public void getNoneExistingDeviceTest()
		throws RemoteException,
			CommandFailedException,
			NoSuchFieldException,
			SecurityException
	{
		Builder builder = GettingDeviceSampleClass.getBuilderInstance();
		DeviceParameters parameters = new DeviceParameters();
		parameters.setRam(9999);

		Device receivedDevice = builder.getDevice(parameters);
		com.musala.atmosphere.client.DeviceInformation information = receivedDevice.getInformation();

		assertTrue(	"Wanted device and returned device parameters don't match.",
					parametersMatchInformation(parameters, information));

		builder.releaseDevice(receivedDevice);
	}
}
