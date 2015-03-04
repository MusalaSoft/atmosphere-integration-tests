package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertUIElementOnScreen;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startNotificationTestActivity;
import static org.junit.Assume.assumeNotNull;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.NotificationBar;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.commons.sa.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class NotificationInteractionTest extends BaseIntegrationTest {

    private static final String NOTIFICATION_TITLE_TEXT = "Notification Title";

    private static final String NOTIFICATION_BUTTON_TEXT = "Open";

    private static NotificationBar notificationBar = null;

    private static Screen deviceActiveScreen = null;

    @BeforeClass
    public static void setUp()
        throws UiElementFetchingException,
            InterruptedException,
            ActivityStartingException,
            XPathExpressionException,
            InvalidCssQueryException,
            Exception {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_ONLY);
        testDeviceParams.setTargetApiLevel(19);

        try {
            initTestDevice(testDeviceParams);
            setTestDevice(testDevice);

            notificationBar = new NotificationBar(testDevice);
            notificationBar.clearAllNotifications();

            startNotificationTestActivity();

            UiElementSelector sendNotificationButtonSelector = new UiElementSelector();
            sendNotificationButtonSelector.addSelectionAttribute(CssAttribute.TEXT,
                                                                 ContentDescriptor.SEND_NOTIFICATION_BUTTON.toString());

            deviceActiveScreen = testDevice.getActiveScreen();
            UiElement sendNotificationButton = deviceActiveScreen.getElement(sendNotificationButtonSelector);
            sendNotificationButton.tap();
        } catch (NoAvailableDeviceFoundException e) {
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (testDevice != null) {
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        }
        releaseDevice();
    }

    @Test
    public void testTapOnButtonInNotification()
        throws XPathExpressionException,
            InvalidCssQueryException,
            UiElementFetchingException,
            ParserConfigurationException {
        assumeNotNull(testDevice);

        notificationBar.open();

        UiElementSelector notificationSelector = new UiElementSelector();
        notificationSelector.addSelectionAttribute(CssAttribute.TEXT, NOTIFICATION_TITLE_TEXT);
        UiElement notification = notificationBar.getNotificationBySelector(notificationSelector);

        notification.pinchOut();
        notification = notificationBar.getNotificationBySelector(notificationSelector);

        UiElementSelector notificationOpenButtonSelector = new UiElementSelector();
        notificationOpenButtonSelector.addSelectionAttribute(CssAttribute.TEXT, NOTIFICATION_BUTTON_TEXT);

        notification.tapOnChildElement(notificationOpenButtonSelector);

        // The tapping on the button opens the wait test activity and then the test checks if a button from the wait
        // test activity is on the screen to assert that the button was tapped successfully
        UiElementSelector changingTextButtonSelector = new UiElementSelector();
        changingTextButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                         ContentDescriptor.CHANGING_TEXT_BUTTON_DESCRIPTOR.toString());

        assertUIElementOnScreen("The button in the notification was not successfully tapped on.",
                                changingTextButtonSelector);
    }
}
