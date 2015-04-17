package com.musala.atmosphere.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.TelephonyInformation;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

public class AtmosphereServiceConnectionTest extends BaseIntegrationTest {

    private UiElement startServiceButton;

    private UiElement stopServiceButton;

    @BeforeClass
    public static void masterSetUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector deviceSelector = selectorBuilder.build();
        initTestDevice(deviceSelector);
    }

    @Before
    public void setUp() throws Exception {

        startServiceActivity();

        Screen deviceScreen = testDevice.getActiveScreen();
        final UiElementSelector buttonSelector = new UiElementSelector();
        buttonSelector.addSelectionAttribute(CssAttribute.TEXT, "Start Service");
        deviceScreen.waitForElementExists(buttonSelector, 10000);
        deviceScreen.updateScreen();
        startServiceButton = deviceScreen.getElementByCSS("[text=Start Service]");
        stopServiceButton = deviceScreen.getElementByCSS("[text=Stop Service]");
    }

    private void startServiceActivity() throws Exception {
        final String SERVICE_PACKAGE_NAME = "com.musala.atmosphere.service";
        final String MAIN_ACTIVITY_NAME = "MainActivity";
        final String startServiceMainActivity = String.format("am start -n %s/.%s",
                                                              SERVICE_PACKAGE_NAME,
                                                              MAIN_ACTIVITY_NAME);

        testDevice.executeShellCommand(startServiceMainActivity);
    }

    @Test()
    public void testFailedConnection() throws InterruptedException {

        stopServiceButton.tap();
        Thread.sleep(1000);
        assertNull("Expected 'null' from request.", testDevice.getTelephonyInformation());
    }

    @Test
    public void testConnection() throws Exception {

        startServiceButton.tap();
        // try to get something through the ATMOSPHERE service
        TelephonyInformation firstInfo = testDevice.getTelephonyInformation();
        assertNotNull(firstInfo);

        // disconnect
        stopServiceButton.tap();

        // connect again
        startServiceButton.tap();

        TelephonyInformation secondInfo = testDevice.getTelephonyInformation();
        assertNotNull(secondInfo);

        assertEquals(firstInfo.getCallState(), secondInfo.getCallState());
        assertEquals(firstInfo.getDeviceSoftwareVersion(), secondInfo.getDeviceSoftwareVersion());
    }
}
