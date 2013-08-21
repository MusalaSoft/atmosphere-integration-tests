package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.agent.AgentIntegrationEnvironmentCreator;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;

public class GetUiXmlTest
{
	private static AgentIntegrationEnvironmentCreator agentEnvironment = AtmosphereIntegrationTestsSuite.getAgentIntegrationEnvironmentCreator();

	private static IWrapDevice deviceWrapper;

	@BeforeClass
	public static void setUp() throws Exception
	{
		deviceWrapper = agentEnvironment.getFirstAvailableDeviceWrapper();
	}

	@AfterClass
	public static void tearDown()
	{

	}

	@Test
	public void getUiXmlTest() throws Exception
	{
		String uiXmlDump = deviceWrapper.getUiXml();

		assertNotNull("UI XML dump response can never be 'null'.", uiXmlDump);

		assertTrue(	"UI XML dump must start with a <hierarchy ..> tag.",
					uiXmlDump.startsWith("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><hierarchy"));

		assertTrue(	"UI XML dump must end with a closing tag for the <hierarchy ..> tag.",
					uiXmlDump.endsWith("</hierarchy>"));
	}
}
