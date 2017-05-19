package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertElementExists;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
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
public class DeviceInstallApkTest extends BaseIntegrationTest {
    private static final String TEST_APPLICATION_APK_NAME = "TestApplication.apk";

    private static final String APK_FILE_PATH = String.format("%s/apks/%s",
                                                              System.getProperty("user.dir"),
                                                              TEST_APPLICATION_APK_NAME);

    private static final String TEST_APPLICATION_PACKAGE_NAME = "com.musala.atmosphere.testapplication";

    private static final int DEFAULT_TIMEOUT = 5000;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);

        testDevice.uninstallApplication(TEST_APPLICATION_PACKAGE_NAME);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(TEST_APPLICATION_PACKAGE_NAME);
        testDevice.uninstallApplication(TEST_APPLICATION_PACKAGE_NAME);

        releaseDevice();
    }

    @Test
    public void testInstallApk() {
        assertTrue("Failed to install the test application.", testDevice.installAPK(APK_FILE_PATH));

        assertTrue("Failed to start the test application after the installation.",
                   testDevice.startApplication(TEST_APPLICATION_PACKAGE_NAME));

        UiElementSelector testApplicationTextViewSelector = new UiElementSelector();
        testApplicationTextViewSelector.addSelectionAttribute(CssAttribute.TEXT, "SampleText");

        assertElementExists("The application was installed successfully, but the main activity is not the same as expected.",
                            testApplicationTextViewSelector,
                            DEFAULT_TIMEOUT);
    }
}
