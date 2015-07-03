package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertDataIsClear;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertDataIsNotClear;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startApplicationDataTestActivity;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

/**
 * 
 * @author yavor.stankov
 *
 */
public class ClearDataTest extends BaseIntegrationTest {
    private static final String CREATE_DATA_BUTTON_CONTENT_DESCRIPTOR = "CreateDataButton";

    private static final String APPLICATION_DATA_SIZE_VIEW_CONTENT_DESCRIPTOR = "ApplicationDataSize";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);

        setTestDevice(testDevice);
        startMainActivity();
    }

    @AfterClass
    public static void tearDownTest() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);

        releaseDevice();
    }

    @Test
    public void testClearData() throws Exception {
        Screen screen = testDevice.getActiveScreen();

        UiElementSelector createDataButtonSelector = new UiElementSelector();
        createDataButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                       CREATE_DATA_BUTTON_CONTENT_DESCRIPTOR);

        UiElement createDataButton = screen.getElement(createDataButtonSelector);
        createDataButton.tap();

        startApplicationDataTestActivity();

        UiElementSelector dataSizeTextViewSelector = new UiElementSelector();
        dataSizeTextViewSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                       APPLICATION_DATA_SIZE_VIEW_CONTENT_DESCRIPTOR);

        UiElement dataSizeTextView = screen.getElement(dataSizeTextViewSelector);

        int dataSize = Integer.valueOf(dataSizeTextView.getText());

        assertDataIsNotClear("The application data is already clear.", dataSize);

        testDevice.clearApplicationData(VALIDATOR_APP_PACKAGE);

        startApplicationDataTestActivity();

        dataSizeTextView = screen.getElement(dataSizeTextViewSelector);

        dataSize = Integer.valueOf(dataSizeTextView.getText());

        assertDataIsClear("Failed to clear the application data.", dataSize);
    }
}
