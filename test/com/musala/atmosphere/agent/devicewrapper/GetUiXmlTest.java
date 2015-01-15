package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.agent.DeviceManager;
import com.musala.atmosphere.commons.RoutingAction;
import com.musala.atmosphere.commons.sa.IWrapDevice;

public class GetUiXmlTest extends BaseIntegrationTest {
    private static IWrapDevice deviceWrapper;

    @BeforeClass
    public static void setUp() throws Exception {
        Class<?> agentClass = agent.getClass();
        Field deviceManagerField = agentClass.getDeclaredField("deviceManager");
        deviceManagerField.setAccessible(true);
        DeviceManager deviceManager = (DeviceManager) deviceManagerField.get(agent);
        deviceWrapper = deviceManager.getFirstAvailableDeviceWrapper();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Test
    public void getUiXmlTest() throws Exception {
        String uiXmlDump = (String) deviceWrapper.route(RoutingAction.GET_UI_XML_DUMP);

        assertNotNull("UI XML dump response can never be 'null'.", uiXmlDump);

        assertTrue("UI XML dump must start with a <hierarchy ..> tag.",
                   uiXmlDump.startsWith("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><hierarchy"));

        assertTrue("UI XML dump must end with a closing tag for the <hierarchy ..> tag.",
                   uiXmlDump.endsWith("</hierarchy>"));
    }
}
