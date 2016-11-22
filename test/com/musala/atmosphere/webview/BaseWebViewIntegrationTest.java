package com.musala.atmosphere.webview;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWebViewActivity;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.WebView;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;

/**
 *
 * @author dimcho.nedev
 *
 */
public class BaseWebViewIntegrationTest extends BaseIntegrationTest {
    protected static Screen screen;

    protected static WebView webView;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder deviceSelectorBuilder = new DeviceSelectorBuilder().minApi(19).maxApi(23);

        try {
            DeviceSelector deviceSelector = deviceSelectorBuilder.deviceType(DeviceType.DEVICE_PREFERRED).build();
            initTestDevice(deviceSelector);
            setTestDevice(testDevice);
            screen = testDevice.getActiveScreen();
            startWebViewActivity();
            webView = screen.getWebView(VALIDATOR_APP_PACKAGE);
        } catch (NoAvailableDeviceFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Before
    public void setUpTest() throws Exception {
        Assume.assumeNotNull(BaseIntegrationTest.testDevice);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Assume.assumeNotNull(BaseIntegrationTest.testDevice);
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }
}
