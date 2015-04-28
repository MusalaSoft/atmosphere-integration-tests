package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertElementText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.DeviceInformation;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author yavor.stankov
 * 
 */
public class ScreenRecordingTest extends BaseIntegrationTest {
    private static final String LOCAL_FILE_PATH = System.getProperty("user.dir");

    private static final long PULL_FILE_TIMEOUT = 3000;

    private static final long TIMEOUT_BETWEEN_INTERACTIONS = 40000;

    private static final int NUMBER_OF_EXPECTED_FILES = 2;

    private static final String EXPECTED_TEXT_RESULT = "Sample Text";

    private static final String COPY_BUTTON_CONTENT_DESCRIPTOR = "Copy";

    private static final String SELECT_ALL_BUTTON_CONTENT_DESCRIPTOR = "Select all";

    private File screenRecordDirectory;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY)
                                                                           .minApi(19);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
        startImeTestActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @After
    public void tearDownTest() throws InterruptedException, IOException {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);

        deleteDirectory(screenRecordDirectory);
    }

    @Test
    public void testScreenRecordTextInput() throws Exception {
        DeviceInformation deviceInformation = testDevice.getInformation();
        String serialNumber = deviceInformation.getSerialNumber();
        String path = String.format("%s/screen_records-%s", LOCAL_FILE_PATH, serialNumber);

        testDevice.startScreenRecording(path);

        Thread.sleep(TIMEOUT_BETWEEN_INTERACTIONS);

        Screen screen = testDevice.getActiveScreen();

        UiElement copyTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());
        copyTextBox.longPress();

        Thread.sleep(TIMEOUT_BETWEEN_INTERACTIONS);

        screen.updateScreen();

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

        screen.updateScreen();

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());

        assertElementText("Paste text failed! The text field content does not match the expected one.",
                          pasteTextBox,
                          EXPECTED_TEXT_RESULT);

        testDevice.stopScreenRecording();

        Thread.sleep(PULL_FILE_TIMEOUT);

        screenRecordDirectory = new File(path);
        String[] recordedFiles = screenRecordDirectory.list();
        int numberOfRecordedFiles = recordedFiles.length;

        Assert.assertEquals("The number of recorded files is not the same as axpected!",
                            NUMBER_OF_EXPECTED_FILES,
                            numberOfRecordedFiles);
    }

    private boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();

            if (files != null) {

                for (File file : files) {
                    file.delete();
                }
            }
        }

        return (directory.delete());
    }
}
