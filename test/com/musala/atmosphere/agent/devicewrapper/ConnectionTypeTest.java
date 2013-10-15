package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static junit.framework.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.ConnectionType;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class ConnectionTypeTest extends com.musala.atmosphere.BaseIntegrationTest
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		initTestDevice(new DeviceParameters());
		installValidatorApplication();
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY, true);
		Thread.sleep(1000);
	}

	@Test
	public void testGetConnectionType() throws UiElementFetchingException
	{
		ConnectionType connectionType = testDevice.getConnectionType();
		UiElement connectionTypeBox = getElementByContentDescriptor(ContentDescriptor.CONNECTION_TYPE_BOX.toString());
		String connectionTypeBoxText = connectionTypeBox.getElementAttributes().getText();
		assertEquals(	"Getting connection type did not return the expected result.",
						Integer.parseInt(connectionTypeBoxText),
						connectionType.getId());
	}
}
