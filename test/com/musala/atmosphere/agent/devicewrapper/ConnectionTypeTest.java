package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setupAndStartMainActivity;
import static junit.framework.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
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

        setupAndStartMainActivity();
    }

    @Test
    public void testGetConnectionType() throws UiElementFetchingException {
        ConnectionType connectionType = testDevice.getConnectionType();
        UiElement connectionTypeBox = getElementByContentDescriptor(ContentDescriptor.CONNECTION_TYPE_BOX.toString());
        String connectionTypeBoxText = connectionTypeBox.getElementSelector().getStringValue(CssAttribute.TEXT);
        assertEquals("Getting connection type did not return the expected result.",
                     Integer.parseInt(connectionTypeBoxText),
                     connectionType.getId());
    }
}
