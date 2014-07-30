package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startNotificationTestActivity;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeNotNull;

import java.util.List;

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

public class NotificationBarTest extends BaseIntegrationTest {

    private static NotificationBar notificationBar = null;

    private static final String NOTIFICATION_TITLE_TEXT = "Notification Title";

    private static final String NOTIFICATION_TITLE_TEXT_XPATH_QUERY = "//*[@text='Notification Title']";

    private static final String EXISTING_NOTIFICATION_CSS_QUERY = "[text=Notification Title]";

    private static final String UNEXISTING_NOTIFICATION_SELECTOR_TEXT = "unexisting notification";

    @BeforeClass
    public static void setUp()
        throws XPathExpressionException,
            UiElementFetchingException,
            InvalidCssQueryException,
            ActivityStartingException,
            InterruptedException {
        DeviceParameters testDeviceParams = new DeviceParameters();
        testDeviceParams.setDeviceType(DeviceType.DEVICE_ONLY);
        testDeviceParams.setApiLevel(19);

        try {
            initTestDevice(testDeviceParams);
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
    public static void tearDown() {
        if (testDevice != null) {
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        }
        releaseDevice();
    }

    public void verifyNotification(UiElement notification) throws Exception {
        List<UiElement> notificationElements = notification.getChildren(NOTIFICATION_TITLE_TEXT_XPATH_QUERY);

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
    public void testGetUnexistingNotification()
        throws XPathExpressionException,
            InvalidCssQueryException,
            UiElementFetchingException,
            ParserConfigurationException {
        assumeNotNull(testDevice);

        notificationBar.getNotificationByText(UNEXISTING_NOTIFICATION_SELECTOR_TEXT);
    }
}
