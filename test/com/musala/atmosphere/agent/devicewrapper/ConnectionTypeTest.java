package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static junit.framework.Assert.assertEquals;

import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.commons.ConnectionType;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class ConnectionTypeTest extends BaseIntegrationTest {
    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());

        setTestDevice(testDevice);

        startMainActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testGetConnectionType()
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        ConnectionType connectionType = testDevice.getConnectionType();
        UiElement connectionTypeBox = getElementByContentDescriptor(ContentDescriptor.CONNECTION_TYPE_BOX.toString());
        String connectionTypeBoxText = connectionTypeBox.getElementSelector().getStringValue(CssAttribute.TEXT);
        assertEquals("Getting connection type did not return the expected result.",
                     Integer.parseInt(connectionTypeBoxText),
                     connectionType.getId());
    }
}
