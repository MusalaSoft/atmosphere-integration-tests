package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertEquals;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.NotificationBar;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.SmsMessage;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class NotificationBarTest extends BaseIntegrationTest {

    private static NotificationBar notificationBar = null;

    private static final String RANDOM_PHONE_NUMBER = "0888826246";

    private static final String RANDOM_TEXT_MESSAGE = "random text message";

    private static final String UNEXISTING_NOTIFICATION_SELECTOR = "unexisting notification";

    @BeforeClass
    public static void setUp() throws XPathExpressionException, UiElementFetchingException, InvalidCssQueryException {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);

        notificationBar = new NotificationBar(testDevice);
        notificationBar.clearAllNotifications();

        SmsMessage smsMessage = new SmsMessage(RANDOM_PHONE_NUMBER, RANDOM_TEXT_MESSAGE);
        testDevice.receiveSms(smsMessage);
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void testGetExistingNotification()
        throws XPathExpressionException,
            InvalidCssQueryException,
            UiElementFetchingException,
            ParserConfigurationException {
        UiElement notification = notificationBar.getNotificationByText(RANDOM_TEXT_MESSAGE);

        assertEquals("The getNotificationByText method did not return the right element.",
                     notification.getText(),
                     RANDOM_TEXT_MESSAGE);
    }

    @Test(expected = UiElementFetchingException.class)
    public void testGetUnexistingNotification()
        throws XPathExpressionException,
            InvalidCssQueryException,
            UiElementFetchingException,
            ParserConfigurationException {
        notificationBar.getNotificationByText(UNEXISTING_NOTIFICATION_SELECTOR);
    }
}
