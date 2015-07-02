package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startNotificationTestActivity;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeNotNull;

import java.util.List;

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

public class NotificationBarTest extends BaseIntegrationTest {

    private static NotificationBar notificationBar = null;

    private static final String NOTIFICATION_TITLE_TEXT = "Notification Title";

    private static final String NOTIFICATION_TITLE_TEXT_XPATH_QUERY = "//*[@text='Notification Title']";

    private static final String EXISTING_NOTIFICATION_CSS_QUERY = "[text=Notification Title]";

    private static final String UNEXISTING_NOTIFICATION_SELECTOR_TEXT = "unexisting notification";

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

            Screen deviceActiveScreen = testDevice.getActiveScreen();
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

    public void verifyNotification(UiElement notification) throws Exception {
        UiElementSelector selector = new UiElementSelector();
        selector.addSelectionAttribute(CssAttribute.TEXT, NOTIFICATION_TITLE_TEXT);
        List<UiElement> notificationElements = notification.getChildren(selector);

        assertNotNull("The method did not return the right notification.", notificationElements);
    }

    @Test
    public void testGetExistingNotificationByText() throws Exception {
        assumeNotNull(testDevice);

        UiElement notification = notificationBar.getNotificationByText(NOTIFICATION_TITLE_TEXT);

        verifyNotification(notification);
    }

    @Test
    public void testGetExistingNotificationBySelector() throws Exception {
        assumeNotNull(testDevice);

        UiElementSelector notificationSelector = new UiElementSelector();
        notificationSelector.addSelectionAttribute(CssAttribute.TEXT, NOTIFICATION_TITLE_TEXT);
        UiElement notification = notificationBar.getNotificationBySelector(notificationSelector);

        verifyNotification(notification);
    }

    @Test
    public void testGetExistingNotificationByXPathQuery() throws Exception {
        assumeNotNull(testDevice);

        UiElement notification = notificationBar.getNotificationByXPath(NOTIFICATION_TITLE_TEXT_XPATH_QUERY);

        verifyNotification(notification);
    }

    @Test
    public void testGetExistingNotificationByCssQuery() throws Exception {
        assumeNotNull(testDevice);

        UiElement notification = notificationBar.getNotificationByCssQuery(EXISTING_NOTIFICATION_CSS_QUERY);

        verifyNotification(notification);
    }

    @Test(expected = UiElementFetchingException.class)
    public void testGetUnexistingNotification() throws Exception {
        assumeNotNull(testDevice);

        notificationBar.getNotificationByText(UNEXISTING_NOTIFICATION_SELECTOR_TEXT);
    }
}
