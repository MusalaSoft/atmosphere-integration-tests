package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;

import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.sa.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

public class OpenQuickSettingsTest extends BaseIntegrationTest {

    private static final String QUICK_SETTINGS_RESOURCE_ID = "com.android.systemui:id/quick_settings_container";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY)
                                                                           .minApi(19);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        try {
            initTestDevice(testDeviceSelector);
        } catch (NoAvailableDeviceFoundException e) {
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Test
    public void testOpenQuickSettings()
        throws XPathExpressionException,
            UiElementFetchingException,
            InvalidCssQueryException {
        assumeNotNull(testDevice);
        System.out.println("maika ti");
        setTestDevice(testDevice);

        UiElementSelector quickSettingsSelector = new UiElementSelector();
        quickSettingsSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, QUICK_SETTINGS_RESOURCE_ID);

        Screen deviceActiveScreen = testDevice.getActiveScreen();

        try {
            deviceActiveScreen.getElement(quickSettingsSelector);
            fail("The quick settings were already opened.");
        } catch (UiElementFetchingException e) {

            testDevice.openQuickSettings();

            try {
                deviceActiveScreen.updateScreen();
                deviceActiveScreen.getElement(quickSettingsSelector);
            } catch (UiElementFetchingException exception) {
                fail("The quick settings were not opened.");
            }
        }
    }
}
