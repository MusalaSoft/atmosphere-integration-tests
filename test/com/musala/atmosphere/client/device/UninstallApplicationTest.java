package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setupOndeviceValidator;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

public class UninstallApplicationTest extends BaseIntegrationTest {

    private static final String ATMOSPHERE_VALIDATOR_BAR_TEXT = "Atmosphere Validator";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);

        initTestDevice(testDeviceParams);
        setTestDevice(testDevice);

        testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
    }

    @AfterClass
    public static void tearDown() {
        setupOndeviceValidator();
        releaseDevice();
    }

    @Test(expected = ActivityStartingException.class)
    public void testUninstallApplication() throws Exception {
        Boolean result = testDevice.uninstallApplication(VALIDATOR_APP_PACKAGE);

        assertTrue("uninstallApplication returned false.", result);

        UiElementSelector validatorBarSelector = new UiElementSelector();
        validatorBarSelector.addSelectionAttribute(CssAttribute.TEXT, ATMOSPHERE_VALIDATOR_BAR_TEXT);

        Screen deviceActiveScreen = testDevice.getActiveScreen();
        deviceActiveScreen.waitUntilElementGone(validatorBarSelector, 10000);

        testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
    }
}
