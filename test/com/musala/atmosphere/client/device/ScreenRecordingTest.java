package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertElementText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;
import static org.junit.Assume.assumeNotNull;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 *
 * @author yavor.stankov
 *
 */
public class ScreenRecordingTest extends BaseIntegrationTest {
    private static final String RECORDS_DIRECTORY_NAME = "ScreenRecords";

    private static final String RECORDS_DIRECTORY_PATH = new File(RECORDS_DIRECTORY_NAME).getAbsolutePath();

    private static final long TIMEOUT_BETWEEN_INTERACTIONS = 15000;

    private static final int EXPECTED_RECORDED_FILES_COUNT = 1;

    private static final String EXPECTED_TEXT_RESULT = "Sample Text";

    private static final String COPY_BUTTON_CONTENT_DESCRIPTOR = "Copy";

    private static final String SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR = "Select all";

    private static File recordsDirectory;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY)
                                                                           .minApi(19);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        try {
            initTestDevice(testDeviceSelector);
            setTestDevice(testDevice);

            recordsDirectory = new File(RECORDS_DIRECTORY_PATH);
            deleteDirectory(recordsDirectory);

            startImeTestActivity();
        } catch (NoAvailableDeviceFoundException e) {
            // Nothing to do here
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (testDevice != null) {
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
            deleteDirectory(recordsDirectory);
        }

        releaseDevice();
    }

    @Test
    public void testScreenRecordTextInput() throws Exception {
        assumeNotNull(testDevice);
        testDevice.startScreenRecording();

        Thread.sleep(TIMEOUT_BETWEEN_INTERACTIONS);

        Screen screen = testDevice.getActiveScreen();

        UiElement copyTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());
        copyTextBox.longPress();

        Thread.sleep(TIMEOUT_BETWEEN_INTERACTIONS);

        UiElementSelector selectAllButtonSelector = new UiElementSelector();
        selectAllButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION,
                                                      SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR);

        UiElement selectAllButton = screen.getElement(selectAllButtonSelector);
        selectAllButton.tap();

        Thread.sleep(TIMEOUT_BETWEEN_INTERACTIONS);

        UiElementSelector copyButtonSelector = new UiElementSelector();
        copyButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, COPY_BUTTON_CONTENT_DESCRIPTOR);

        UiElement copyButton = screen.getElement(copyButtonSelector);
        copyButton.tap();

        Thread.sleep(TIMEOUT_BETWEEN_INTERACTIONS);

        UiElement pasteTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        pasteTextBox.pasteText();

        Thread.sleep(TIMEOUT_BETWEEN_INTERACTIONS);

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());

        assertElementText("Paste text failed! The text field content does not match the expected one.",
                          pasteTextBox,
                          EXPECTED_TEXT_RESULT);

        testDevice.stopScreenRecording();

        Assert.assertTrue("The screen records folder does not exist.", recordsDirectory.exists());

        int actualRecordedFilesCount = recordsDirectory.list().length;

        Assert.assertEquals("The number of recorded files is not the same as the expected!",
                            EXPECTED_RECORDED_FILES_COUNT,
                            actualRecordedFilesCount);
    }

    private static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();

            for (File file : files) {
                file.delete();
            }

            return directory.delete();
        }

        return false;
    }
}
