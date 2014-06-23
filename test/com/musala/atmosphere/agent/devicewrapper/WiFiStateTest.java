package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertWiFiIsOff;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertWiFiIsOn;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setupAndStartMainActivity;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class WiFiStateTest extends BaseIntegrationTest {
    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());

        setTestDevice(testDevice);

        setupAndStartMainActivity();
    }

    @Test
    public void testSetWiFiOn() throws UiElementFetchingException {
        testDevice.setWiFi(true);

        assertWiFiIsOn("WiFi on the testing device is not turned on.");
    }

    @Test
    public void testSetWiFiOff() throws UiElementFetchingException {
        testDevice.setWiFi(false);

        assertWiFiIsOff("WiFi on the testing device is not turned off.");
    }

}
