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

public class ReinstallApplicationTest extends BaseIntegrationTest {

    private final static String PATH_TO_APK_SECOND_CERTIFICATE = "./ondeviceComponents\\OnDeviceValidator-secondCertificate.apk";

    private final static String VALIDATOR_BAR_TEXT = "Atmosphere Validator";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_PREFERRED);

        initTestDevice(testDeviceParams);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        setupOndeviceValidator();
        releaseDevice();
    }

    public void reinstallApplication() throws ActivityStartingException {
        Boolean result = testDevice.reinstallApplication(VALIDATOR_APP_PACKAGE, PATH_TO_APK_SECOND_CERTIFICATE);

        assertTrue("The validator application did not reinstall correctly.", result);

        testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);

        UiElementSelector validatorBarSelector = new UiElementSelector();
        validatorBarSelector.addSelectionAttribute(CssAttribute.TEXT, VALIDATOR_BAR_TEXT);

        Screen deviceActiveScreen = testDevice.getActiveScreen();

        result = deviceActiveScreen.waitForElementExists(validatorBarSelector, 3000);

        assertTrue("The validator application did not open correctly after the reinstallation.", result);
    }

    @Test
    public void testReinstallInstalledApplication() throws ActivityStartingException {
        reinstallApplication();
    }

    @Test
    public void testReinstallUnistalledApplication() throws ActivityStartingException {
        testDevice.uninstallApplication(VALIDATOR_APP_PACKAGE);

        reinstallApplication();
    }
}
