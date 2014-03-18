package com.musala.atmosphere.client.device;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class DeviceGetScreenshotTest extends BaseIntegrationTest {
    private static final String PATH_TO_SCREENSHOT = "./Screenshot.png";

    @Before
    public void setUp() {
        initTestDevice(new DeviceParameters());
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void getScreenShotTest() {
        // getting screenshot without dumping it to file
        byte[] screenshot = testDevice.getScreenshot();

        assertNotNull("Getting screenshot returned 'null'", screenshot);

        // getting screenshot with dumping it to file
        assertTrue("Getting screenshot returned false.", testDevice.getScreenshot(PATH_TO_SCREENSHOT));
        File dumpedScreenshot = new File(PATH_TO_SCREENSHOT);

        assertTrue("Getting screenshot failed!", dumpedScreenshot.exists());
    }
}
