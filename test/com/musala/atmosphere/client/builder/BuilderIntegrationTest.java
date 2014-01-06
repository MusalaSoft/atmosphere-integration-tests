package com.musala.atmosphere.client.builder;

import org.junit.Test;

import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.exceptions.MissingServerAnnotationException;
import com.musala.atmosphere.client.exceptions.ServerConnectionFailedException;
import com.musala.atmosphere.client.util.Server;

public class BuilderIntegrationTest
{
	private static final int SERVER_MANAGER_RMI_PORT = 2099;

	@Test(expected = MissingServerAnnotationException.class)
	public void missingSeverAnnotationTest()
	{
		class SampleTestNoAnnotation
		{
			public Builder getBuilderInstance()
			{
				Builder testerBuilder = Builder.getInstance();
				return testerBuilder;
			}
		}
		SampleTestNoAnnotation sampleBuilderClassInstance = new SampleTestNoAnnotation();
	}

	@Test(expected = ServerConnectionFailedException.class)
	public void wrongIpTest()
	{
		@Server(ip = "149.111.400.154", port = SERVER_MANAGER_RMI_PORT, connectionRetryLimit = 10)
		class SampleTestWrongIpOne
		{
			public Builder getBuilderInstance()
			{
				Builder testerBuilder = Builder.getInstance();
				return testerBuilder;
			}
		}
		SampleTestWrongIpOne sampleBuilderClassInstance = new SampleTestWrongIpOne();
		Builder builderInstance = sampleBuilderClassInstance.getBuilderInstance();
	}

	@Test(expected = ServerConnectionFailedException.class)
	public void wrongIpTestTwo()
	{
		@Server(ip = "random_string", port = SERVER_MANAGER_RMI_PORT, connectionRetryLimit = 10)
		class SampleTestWrongIpTwo
		{
			public Builder getBuilderInstance()
			{
				Builder testerBuilder = Builder.getInstance();
				return testerBuilder;
			}
		}
		SampleTestWrongIpTwo sampleBuilderClassInstance = new SampleTestWrongIpTwo();
		Builder builderInstance = sampleBuilderClassInstance.getBuilderInstance();
	}

	@Test(expected = ServerConnectionFailedException.class)
	public void wrongPortTest()
	{
		@Server(ip = "localhost", port = SERVER_MANAGER_RMI_PORT + 148, connectionRetryLimit = 10)
		class SampleTestWrongPort
		{
			public Builder getBuilderInstance()
			{
				Builder testerBuilder = Builder.getInstance();
				return testerBuilder;
			}
		}
		SampleTestWrongPort sampleBuilderClassInstance = new SampleTestWrongPort();
		Builder builderInstance = sampleBuilderClassInstance.getBuilderInstance();
	}
}
