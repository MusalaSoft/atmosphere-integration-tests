package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startDetectAudioTestActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

/**
 * 
 * @author denis.bialev
 *
 */
public class DetectAudioTest extends BaseIntegrationTest {

    private static final String PLAY_SOUND_TEXT = "Play Sound";

    private static final String STOP_SOUND_TEXT = "Stop Sound";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        startDetectAudioTestActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testIsAudioPlaying() throws Exception {
        Screen screen = testDevice.getActiveScreen();

        screen.tapElementWithText(PLAY_SOUND_TEXT);
        assertTrue("Failed to detect the currently playing audio.", testDevice.isAudioPlaying());

        screen.tapElementWithText(STOP_SOUND_TEXT);
        assertFalse("An audio is detected when no audio is currently playing.", testDevice.isAudioPlaying());
    }
}
