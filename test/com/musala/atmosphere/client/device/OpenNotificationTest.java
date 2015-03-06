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
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.commons.sa.exceptions.NoAvailableDeviceFoundException;

public class OpenNotificationTest extends BaseIntegrationTest {

    private static final String NOTIFICATION_BAR_RESOURCE_ID = "com.android.systemui:id/notification_panel";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_ONLY);
        testDeviceParams.setMinApiLevel(19);

        try {
            initTestDevice(testDeviceParams);
        } catch (NoAvailableDeviceFoundException e) {
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Test
    public void testOpenNotification()
        throws XPathExpressionException,
            UiElementFetchingException,
            InvalidCssQueryException {
        assumeNotNull(testDevice);

        setTestDevice(testDevice);

        UiElementSelector notificationBarSelector = new UiElementSelector();
        notificationBarSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, NOTIFICATION_BAR_RESOURCE_ID);

        Screen deviceActiveScreen = testDevice.getActiveScreen();

        try {
            deviceActiveScreen.getElement(notificationBarSelector);
            fail("The notification bar was already opened");
        } catch (UiElementFetchingException e) {

            testDevice.openNotificationBar();

            try {
                deviceActiveScreen.updateScreen();
                deviceActiveScreen.getElement(notificationBarSelector);
            } catch (UiElementFetchingException exception) {
                fail("The notification bar was not opened.");
            }
        }
    }
}
