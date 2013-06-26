package com.musala.atmosphere.client.builder;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.exceptions.MissingServerAnnotationException;
import com.musala.atmosphere.client.exceptions.ServerConnectionFailedException;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.Pair;
import com.musala.atmosphere.commons.sa.exceptions.ADBridgeFailException;
import com.musala.atmosphere.commons.sa.exceptions.DeviceNotFoundException;
import com.musala.atmosphere.commons.sa.exceptions.NotPossibleForDeviceException;
import com.musala.atmosphere.server.ServerIntegrationEnvironmentCreator;

public class BuilderIntegrationTest
{
	private static final int SERVER_RMI_PORT = 1980;

	private ServerIntegrationEnvironmentCreator serverEnvironment;

	@Before
	public void setUp() throws Exception, ADBridgeFailException
	{
		serverEnvironment = new ServerIntegrationEnvironmentCreator(SERVER_RMI_PORT);
	}

	@Test(expected = MissingServerAnnotationException.class)
	public void builderGetInstanceTest1()
	{
		class SampleTestNoAnnotation
		{
			public void getBuilderInstance()
			{
				Builder testerbuilder = Builder.getInstance();
			}
		}
		SampleTestNoAnnotation myTest = new SampleTestNoAnnotation();
		myTest.getBuilderInstance();
	}

	@Test(expected = ServerConnectionFailedException.class)
	public void builderGetInstanceTest2()
	{
		@Server(ip = "10.1111.4000.15234", port = SERVER_RMI_PORT)
		class SampleTestWrongIp
		{
			public void getBuilderInstance()
			{
				Builder testerbuilder = Builder.getInstance();
			}
		}
		SampleTestWrongIp myTest = new SampleTestWrongIp();
		myTest.getBuilderInstance();
	}

	@Test(expected = ServerConnectionFailedException.class)
	public void builderGetInstanceTest3()
	{
		@Server(ip = "random_string", port = SERVER_RMI_PORT)
		class SampleTestWrongIp
		{
			public void getBuilderInstance()
			{
				Builder testerbuilder = Builder.getInstance();
			}
		}
		SampleTestWrongIp myTest = new SampleTestWrongIp();
		myTest.getBuilderInstance();
	}

	@Test(expected = ServerConnectionFailedException.class)
	public void builderGetInstanceTest4()
	{
		@Server(ip = "localhost", port = 65535)
		class SampleTestWrongPort
		{
			public void getBuilderInstance()
			{
				Builder testerbuilder = Builder.getInstance();
			}
		}
		SampleTestWrongPort myTest = new SampleTestWrongPort();
		myTest.getBuilderInstance();
	}

	@Test
	public void builderGetInstanceTest5()
	{
		final String serverIp = "localhost";
		final int serverPort = SERVER_RMI_PORT;

		@Server(ip = serverIp, port = serverPort)
		class SampleTest
		{
			public Pair<String, Integer> getBuilderInstance()
			{
				Builder testerBuilder = Builder.getInstance();
				return new Pair<String, Integer>(testerBuilder.getServerIp(), testerBuilder.getServerRmiPort());
			}
		}

		SampleTest myTest = new SampleTest();
		Pair<String, Integer> reflectedAnnotation = myTest.getBuilderInstance();
		assertEquals("IP's don't match!", serverIp, reflectedAnnotation.getKey());
		assertEquals("Port's don't match!", serverPort, (int) reflectedAnnotation.getValue());

		// Testing for data consistency
		SampleTest myTest2 = new SampleTest();
		reflectedAnnotation = myTest.getBuilderInstance();
		assertEquals("IP's don't match!", serverIp, reflectedAnnotation.getKey());
		assertEquals("Port's don't match!", serverPort, (int) reflectedAnnotation.getValue());

		SampleTest myTest3 = new SampleTest();
		reflectedAnnotation = myTest.getBuilderInstance();
		assertEquals("IP's don't match!", serverIp, reflectedAnnotation.getKey());
		assertEquals("Port's don't match!", serverPort, (int) reflectedAnnotation.getValue());
	}

	@After
	public void tearDown() throws IOException, DeviceNotFoundException, NotPossibleForDeviceException
	{
		serverEnvironment.close();
	}
}
