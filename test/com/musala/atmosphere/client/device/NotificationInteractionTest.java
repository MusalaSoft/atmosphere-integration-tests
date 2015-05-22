package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertUIElementOnScreen;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startNotificationTestActivity;
import static org.junit.Assume.assumeNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.NotificationBar;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class NotificationInteractionTest extends BaseIntegrationTest {

    private static final String NOTIFICATION_TITLE_TEXT = "Notification Title";

    private static final String NOTIFICATION_BUTTON_TEXT = "Open";

    private static NotificationBar notificationBar = null;

    private static Screen deviceActiveScreen = null;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY)
                                                                           .minApi(19);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        try {
            initTestDevice(testDeviceSelector);
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
    public void testTapOnButtonInNotification() throws Exception {
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
