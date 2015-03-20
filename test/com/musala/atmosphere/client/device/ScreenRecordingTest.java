package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInputText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startImeTestActivity;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

/**
 * 
 * @author yavor.stankov
 *
 */
public class ScreenRecordingTest extends BaseIntegrationTest {
    private static final String LOCAL_FILE_PATH = System.getProperty("user.dir");

    private static final String LOCAL_FILE_NAME = "screenrecord";

    private static final String LOCAL_FILE_FORMAT = ".mp4";

    private File screenRecordFile;

    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);
        startImeTestActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);

        releaseDevice();
    }

    @After
    public void tearDownTest() throws InterruptedException {
        screenRecordFile.delete();
    }

    @Test
    public void testScreenRecordTextInput() throws Exception {
        testDevice.startScreenRecording();

        String textToInput = "Screen recording!!!";

        UiElement inputTextBox = getElementByContentDescriptor(ContentDescriptor.EMPTY_TEXT_BOX.toString());
        inputTextBox.inputText(textToInput);

        assertInputText("Inputting text failed.", textToInput);

        String fullLocalFilePath = String.format("%s%s%s%s%s",
                                                 LOCAL_FILE_PATH,
                                                 File.separator,
                                                 LOCAL_FILE_NAME,
                                                 getCurrentTimeStamp(),
                                                 LOCAL_FILE_FORMAT);

        testDevice.stopScreenRecording(fullLocalFilePath);

        screenRecordFile = new File(fullLocalFilePath);
        assertTrue("There is no such file recorded.", screenRecordFile.exists());
    }

    private static String getCurrentTimeStamp() {
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HHmmss");
        long currentTime = new Date().getTime();
        String timeStamp = simpleDateFormatTime.format(currentTime);

        return timeStamp;
    }
}
