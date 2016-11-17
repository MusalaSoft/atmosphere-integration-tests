package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertElementText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertTextIsCleared;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 *
 * @author filareta.yordanova
 *
 */
public class CopyTextTest extends BaseIntegrationTest {
    private static final String TEXT_MISSMATCH_ERROR_MESSAGE = "Text field content does not match the expected text before performing copy operation.";

    private static final String FAILURE_MESSAGE = "Copy selected text failed! Text field content does not match the expected one.";

    private static final String EXPECTED_TEXT_RESULT = "Sample Text";

    private static final String EXPECTED_TEXT_BEFORE_COPY = "Paste Here";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
    }

    @Before
    public void setUpTest() throws Exception {
        startImeTestActivity();
    }

    @After
    public void tearDownTest() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Test
    public void testCopyTextWhenTextIsSelected() throws Exception {
        UiElement copyTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());
        copyTextBox.selectAllText();

        copyTextBox = getElementByContentDescriptor(ContentDescriptor.CONTENT_TEXT_BOX.toString());
        assertTrue(copyTextBox.copyText());

        UiElement pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());
        assertElementText(TEXT_MISSMATCH_ERROR_MESSAGE, pasteTextBox, EXPECTED_TEXT_BEFORE_COPY);

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());
        pasteTextBox.selectAllText();

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());
        pasteTextBox.pasteText();

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());
        assertElementText(FAILURE_MESSAGE, pasteTextBox, EXPECTED_TEXT_RESULT);
    }

    @Test
    public void testCopyTextWhenNoTextIsSelected() throws Exception {
        UiElement copyTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        assertTrue(copyTextBox.copyText());

        UiElement pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());
        assertElementText(TEXT_MISSMATCH_ERROR_MESSAGE, pasteTextBox, EXPECTED_TEXT_BEFORE_COPY);

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());
        pasteTextBox.selectAllText();

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());
        pasteTextBox.pasteText();

        pasteTextBox = getElementByContentDescriptor(ContentDescriptor.PASTE_CONTAINER_TEXT_BOX.toString());
        assertTextIsCleared(FAILURE_MESSAGE, ContentDescriptor.PASTE_CONTAINER_TEXT_BOX);
    }
}
