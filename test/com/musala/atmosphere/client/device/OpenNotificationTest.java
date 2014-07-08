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

public class OpenNotificationTest extends BaseIntegrationTest {

    private static final int OPEN_NOTIFICATION_BAR_TIMEOUT = 5000;

    private static final String NOTIFICATION_BAR_RESOURCE_ID = "com.android.systemui:id/notification_panel";

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
    public void testOpenNotification()
        throws XPathExpressionException,
            UiElementFetchingException,
            InvalidCssQueryException {
        UiElementSelector notificationBarSelector = new UiElementSelector();
        notificationBarSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, NOTIFICATION_BAR_RESOURCE_ID);
        Screen deviceActiveScreen = testDevice.getActiveScreen();
        try {
            deviceActiveScreen.getElement(notificationBarSelector);
            fail("The notification bar was already opened");
        } catch (UiElementFetchingException e) {

            testDevice.openNotificationBar();

            deviceActiveScreen.updateScreen();
            Boolean result = deviceActiveScreen.waitForElementExists(notificationBarSelector,
                                                                     OPEN_NOTIFICATION_BAR_TIMEOUT);

            assertTrue("The notification bar was not opened.", result);
        }
    }
}
