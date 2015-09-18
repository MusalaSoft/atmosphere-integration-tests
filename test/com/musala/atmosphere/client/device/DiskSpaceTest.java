package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startDiskSpaceActivity;
import static org.junit.Assert.assertEquals;

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
 * @author konstantin.ivanov
 *
 */
public class DiskSpaceTest extends BaseIntegrationTest {

    private static final String SHOW_FREE_SPACE_BUTTON_RESOURCE_ID = "com.musala.atmosphere.ondevice.validator:id/buttonShowFreeSpace";

    private static final String FREE_SPACE_RESULT_RESOURCE_ID = "com.musala.atmosphere.ondevice.validator:id/freeSpaceTextView";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        startDiskSpaceActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testGetFreeDiskSpace() throws Exception {
        Screen screen = testDevice.getActiveScreen();

        UiElementSelector showFreeSpaceButtonSelector = new UiElementSelector();
        showFreeSpaceButtonSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, SHOW_FREE_SPACE_BUTTON_RESOURCE_ID);
        UiElement showFreeSpaceButtonElement = screen.getElement(showFreeSpaceButtonSelector);
        showFreeSpaceButtonElement.tap();

        UiElementSelector freeSpaceTextBoxSelector = new UiElementSelector();
        freeSpaceTextBoxSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, FREE_SPACE_RESULT_RESOURCE_ID);
        UiElement freeSpaceTextBox = screen.getElement(freeSpaceTextBoxSelector);
        Long expectedFreeSpace = Long.valueOf(freeSpaceTextBox.getText());
        Long freeSpace = testDevice.getAvailableDiskSpace();

        assertEquals("The actual free disk space does not match the expected one.", expectedFreeSpace, freeSpace);
    }
}
