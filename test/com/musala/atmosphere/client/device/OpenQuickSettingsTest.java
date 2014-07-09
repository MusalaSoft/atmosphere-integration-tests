package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class OpenQuickSettingsTest extends BaseIntegrationTest {

    private static final int OPEN_QUICK_SETTINGS_TIMEOUT = 5000;

    private static final String QUICK_SETTINGS_RESOURCE_ID = "com.android.systemui:id/quick_settings_container";

    @BeforeClass
    public static void setUp() {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void testOpenQuickSettings()
        throws XPathExpressionException,
            UiElementFetchingException,
            InvalidCssQueryException {
        UiElementSelector quickSettingsSelector = new UiElementSelector();
        quickSettingsSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, QUICK_SETTINGS_RESOURCE_ID);
        Screen deviceActiveScreen = testDevice.getActiveScreen();
        try {
            deviceActiveScreen.getElement(quickSettingsSelector);
            fail("The quick settings were already opened.");
        } catch (UiElementFetchingException e) {

            testDevice.openQuickSettings();

            deviceActiveScreen.updateScreen();
            Boolean result = deviceActiveScreen.waitForElementExists(quickSettingsSelector, OPEN_QUICK_SETTINGS_TIMEOUT);

            assertTrue("The quick settings were not opened.", result);
        }
    }
}
