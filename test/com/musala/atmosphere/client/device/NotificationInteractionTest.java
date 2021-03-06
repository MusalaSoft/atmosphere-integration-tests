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
import com.musala.atmosphere.commons.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class NotificationInteractionTest extends BaseIntegrationTest {

    private static final String NOTIFICATION_RESOURCE_ID = "android:id/status_bar_latest_event_content";

    private static final String NOTIFICATION_BUTTON_TEXT = "Open";

    private static final String NOTIFICATION_BUTTON_TEXT_TO_UPPER = NOTIFICATION_BUTTON_TEXT.toUpperCase();

    private static final int GET_NOTIFICATION_TIMEOUT = 5_000;

    private static NotificationBar notificationBar = null;

    private static Screen deviceActiveScreen = null;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED)
                                                                           .minApi(19);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        try {
            initTestDevice(testDeviceSelector);
            setTestDevice(testDevice);

            notificationBar = new NotificationBar(testDevice);
            notificationBar.open();
            try {
                // If there are no notifications the Clear notifications button is not present
                notificationBar.clearAllNotifications();
            } catch (UiElementFetchingException e) {
                // Nothing to do here
            }

            notificationBar.close();

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

        int deviceApiLevel = testDevice.getInformation().getApiLevel();
        String notificationButtonText = deviceApiLevel >= 24 ? NOTIFICATION_BUTTON_TEXT_TO_UPPER : NOTIFICATION_BUTTON_TEXT;

        notificationBar.open();

        UiElementSelector notificationSelector = new UiElementSelector();
        notificationSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, NOTIFICATION_RESOURCE_ID);

        Screen screen = testDevice.getActiveScreen();
        screen.waitForElementExists(notificationSelector, GET_NOTIFICATION_TIMEOUT);
        notificationBar.getNotificationByText(notificationButtonText);
        UiElement notification = notificationBar.getNotificationsBySelector(notificationSelector).get(0);

        notification.pinchOut();
        notification = notificationBar.getNotificationsBySelector(notificationSelector).get(0);

        UiElementSelector notificationOpenButtonSelector = new UiElementSelector();
        notificationOpenButtonSelector.addSelectionAttribute(CssAttribute.TEXT, notificationButtonText);

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
