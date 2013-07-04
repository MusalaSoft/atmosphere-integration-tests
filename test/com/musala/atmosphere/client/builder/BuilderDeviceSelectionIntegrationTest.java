package com.musala.atmosphere.client.builder;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.CommandFailedException;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceOs;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.commons.sa.DeviceInformation;
import com.musala.atmosphere.commons.sa.IAgentManager;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.commons.sa.exceptions.DeviceNotFoundException;
import com.musala.atmosphere.commons.sa.exceptions.NotPossibleForDeviceException;
import com.musala.atmosphere.server.PoolItem;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;

public class BuilderDeviceSelectionIntegrationTest
{
	private final static int SERVER_RMI_PORT = 1980;

	private final static String AGENT_ID = "mockagent";

	private final static String DEVICE1_SN = "mockdevice1";

	private final static String DEVICE2_SN = "mockdevice2";

	private final static String DEVICE3_SN = "mockdevice3";

	private final static String DEVICE4_SN = "mockdevice4";

	private final static String DEVICE5_SN = "mockdevice5";

	private ServerIntegrationEnvironmentCreator serverEnvironment = null;

	private DeviceInformation mockedDeviceInfoOne = new DeviceInformation();

	private DeviceInformation mockedDeviceInfoTwo = new DeviceInformation();

	private DeviceInformation mockedDeviceInfoThree = new DeviceInformation();

	private DeviceInformation mockedDeviceInfoFour = new DeviceInformation();

	private DeviceInformation mockedDeviceInfoFive = new DeviceInformation();

	@Server(ip = "localhost", port = SERVER_RMI_PORT)
	class GettingDeviceSampleClass
	{
		private Builder builder = null;

		private Device device = null;

		private String[] possibleRmiIds;

		public GettingDeviceSampleClass(String... rmiIds)
		{
			possibleRmiIds = new String[rmiIds.length];
			for (int indexOfRmiId = 0; indexOfRmiId < rmiIds.length; indexOfRmiId++)
			{
				possibleRmiIds[indexOfRmiId] = AGENT_ID + " " + rmiIds[indexOfRmiId];
			}
		}

		public void getDevice(DeviceParameters parameters) throws RemoteException, CommandFailedException
		{
			builder = Builder.getInstance();
			String rmiId = serverEnvironment.getPoolManager().getDeviceProxyRmiId(parameters);

			boolean isRmiIdOk = false;
			for (String currentRmiId : possibleRmiIds)
			{
				isRmiIdOk = isRmiIdOk || rmiId.equals(currentRmiId);
			}

			assertTrue("Failed to receive RMI ID of the correct device.", isRmiIdOk);
			device = builder.getDevice(parameters);
		}
	}

	@Before
	public void setUp()
		throws RemoteException,
			NoSuchMethodException,
			SecurityException,
			IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException,
			NoSuchFieldException
	{
		serverEnvironment = new ServerIntegrationEnvironmentCreator(SERVER_RMI_PORT);

		IAgentManager mockedAgentManager = mock(IAgentManager.class);
		when(mockedAgentManager.getAgentId()).thenReturn(AGENT_ID);

		IWrapDevice mockedDeviceOne = mock(IWrapDevice.class);
		IWrapDevice mockedDeviceTwo = mock(IWrapDevice.class);
		IWrapDevice mockedDeviceThree = mock(IWrapDevice.class);
		IWrapDevice mockedDeviceFour = mock(IWrapDevice.class);
		IWrapDevice mockedDeviceFive = mock(IWrapDevice.class);

		mockedDeviceInfoOne.setSerialNumber(DEVICE1_SN);
		mockedDeviceInfoOne.setOs("4.2.1");
		mockedDeviceInfoOne.setEmulator(true);
		mockedDeviceInfoOne.setRam(128);
		mockedDeviceInfoOne.setResolution(new Pair<>(600, 800));
		mockedDeviceInfoOne.setDpi(120);

		mockedDeviceInfoTwo.setSerialNumber(DEVICE2_SN);
		mockedDeviceInfoTwo.setOs("4.1");
		mockedDeviceInfoTwo.setEmulator(true);
		mockedDeviceInfoTwo.setRam(256);
		mockedDeviceInfoTwo.setResolution(new Pair<>(200, 200));
		mockedDeviceInfoTwo.setDpi(240);

		mockedDeviceInfoThree.setSerialNumber(DEVICE3_SN);
		mockedDeviceInfoThree.setOs("4.0.2");
		mockedDeviceInfoThree.setEmulator(false);
		mockedDeviceInfoThree.setRam(256);
		mockedDeviceInfoThree.setResolution(new Pair<>(400, 500));
		mockedDeviceInfoThree.setDpi(80);

		mockedDeviceInfoFour.setSerialNumber(DEVICE4_SN);
		mockedDeviceInfoFour.setOs("4.0.1");
		mockedDeviceInfoFour.setEmulator(false);
		mockedDeviceInfoFour.setRam(512);
		mockedDeviceInfoFour.setResolution(new Pair<>(200, 200));
		mockedDeviceInfoFour.setDpi(180);

		mockedDeviceInfoFive.setSerialNumber(DEVICE5_SN);
		mockedDeviceInfoFive.setOs("4.2.1");
		mockedDeviceInfoFive.setEmulator(false);
		mockedDeviceInfoFive.setRam(512);
		mockedDeviceInfoFive.setResolution(new Pair<>(200, 200));
		mockedDeviceInfoFive.setDpi(180);

		when(mockedDeviceOne.getDeviceInformation()).thenReturn(mockedDeviceInfoOne);
		when(mockedDeviceTwo.getDeviceInformation()).thenReturn(mockedDeviceInfoTwo);
		when(mockedDeviceThree.getDeviceInformation()).thenReturn(mockedDeviceInfoThree);
		when(mockedDeviceFour.getDeviceInformation()).thenReturn(mockedDeviceInfoFour);
		when(mockedDeviceFive.getDeviceInformation()).thenReturn(mockedDeviceInfoFive);

		Field serverRmiRegistryField = serverEnvironment.getPoolManager().getClass().getDeclaredField("rmiRegistry");
		serverRmiRegistryField.setAccessible(true);
		Registry serverRegistry = (Registry) serverRmiRegistryField.get(serverEnvironment.getPoolManager());
		Field poolManagerPoolItemsList = serverEnvironment.getPoolManager().getClass().getDeclaredField("poolItems");
		poolManagerPoolItemsList.setAccessible(true);
		List<PoolItem> poolItemsList = (List<PoolItem>) poolManagerPoolItemsList.get(serverEnvironment.getPoolManager());

		poolItemsList.add(new PoolItem(DEVICE1_SN, mockedDeviceOne, mockedAgentManager, serverRegistry));
		poolItemsList.add(new PoolItem(DEVICE2_SN, mockedDeviceTwo, mockedAgentManager, serverRegistry));
		poolItemsList.add(new PoolItem(DEVICE3_SN, mockedDeviceThree, mockedAgentManager, serverRegistry));
		poolItemsList.add(new PoolItem(DEVICE4_SN, mockedDeviceFour, mockedAgentManager, serverRegistry));
		poolItemsList.add(new PoolItem(DEVICE5_SN, mockedDeviceFive, mockedAgentManager, serverRegistry));

	}

	@After
	public void tearDown() throws IOException, DeviceNotFoundException, NotPossibleForDeviceException
	{
		serverEnvironment.close();
	}

	@Test
	public void getMockedDeviceOneTest()
		throws RemoteException,
			CommandFailedException,
			NoSuchFieldException,
			SecurityException
	{
		DeviceParameters parameters = new DeviceParameters();
		parameters.setRam(128);
		parameters.setDpi(120);

		GettingDeviceSampleClass userTestClass = new GettingDeviceSampleClass(DEVICE1_SN);
		userTestClass.getDevice(parameters);
	}

	@Test
	public void getMockedDeviceTwoTest()
		throws RemoteException,
			CommandFailedException,
			NoSuchFieldException,
			SecurityException
	{
		DeviceParameters parameters = new DeviceParameters();
		parameters.setDeviceType(DeviceType.EMULATOR_PREFERRED);
		parameters.setOs(DeviceOs.JELLY_BEAN_4_1);
		parameters.setRam(256);

		GettingDeviceSampleClass userTestClass = new GettingDeviceSampleClass(DEVICE2_SN);
		userTestClass.getDevice(parameters);
	}

	@Test
	public void getMockedDeviceThreeTest()
		throws RemoteException,
			CommandFailedException,
			NoSuchFieldException,
			SecurityException
	{
		DeviceParameters parameters = new DeviceParameters();
		parameters.setDpi(80);
		parameters.setRam(256);

		GettingDeviceSampleClass userTestClass = new GettingDeviceSampleClass(DEVICE3_SN);
		userTestClass.getDevice(parameters);
	}

	@Test
	public void getMockedDeviceFourTest()
		throws RemoteException,
			CommandFailedException,
			NoSuchFieldException,
			SecurityException
	{
		DeviceParameters parameters = new DeviceParameters();
		parameters.setDeviceType(DeviceType.DEVICE_ONLY);
		parameters.setRam(512);

		GettingDeviceSampleClass userTestClass = new GettingDeviceSampleClass(DEVICE4_SN);
		userTestClass.getDevice(parameters);
	}

	@Test
	public void getMockedDeviceFiveTest()
		throws RemoteException,
			CommandFailedException,
			NoSuchFieldException,
			SecurityException
	{
		DeviceParameters parameters = new DeviceParameters();
		parameters.setDeviceType(DeviceType.DEVICE_PREFERRED);
		parameters.setRam(512);
		parameters.setResolutionHeight(200);
		parameters.setResolutionWidth(200);
		parameters.setDpi(180);
		parameters.setOs(DeviceOs.JELLY_BEAN_MR1_4_2_1);

		GettingDeviceSampleClass userTestClass = new GettingDeviceSampleClass(DEVICE5_SN);
		userTestClass.getDevice(parameters);
	}

	@Test
	public void getMockedDeviceFourOrFiveTest()
		throws RemoteException,
			CommandFailedException,
			NoSuchFieldException,
			SecurityException
	{
		DeviceParameters parameters = new DeviceParameters();
		parameters.setRam(512);

		GettingDeviceSampleClass userTestClass = new GettingDeviceSampleClass(DEVICE4_SN, DEVICE5_SN);
		userTestClass.getDevice(parameters);
	}

	@Test(expected = NoSuchElementException.class)
	public void getNoneExistingDeviceTest()
		throws RemoteException,
			CommandFailedException,
			NoSuchFieldException,
			SecurityException
	{

		DeviceParameters parameters = new DeviceParameters();
		parameters.setRam(9999);

		GettingDeviceSampleClass userTestClass = new GettingDeviceSampleClass();
		userTestClass.getDevice(parameters);
	}
}
