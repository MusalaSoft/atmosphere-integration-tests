package com.musala.atmosphere;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.AfterClass;

import com.musala.atmosphere.agent.Agent;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.device.HardwareButton;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.server.Server;
import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;

/**
 * 
 * 
 * @author valyo.yolovski
 * 
 */
public class BaseIntegrationTest {

    @com.musala.atmosphere.client.util.Server(ip = "localhost", port = SERVER_MANAGER_RMI_PORT, connectionRetryLimit = 0)
    private static class GettingBuilderClass {
        public GettingBuilderClass() {
        }

        public Builder getBuilder() {
            Builder classDeviceBuilder = Builder.getInstance();
            return classDeviceBuilder;
        }
    }

    protected final static int SERVER_MANAGER_RMI_PORT = 2099;

    protected final static String ONDEVICEVALIDATOR_FILE = "OnDeviceValidator.apk";

    protected final static String PATH_TO_APK_DIR = "./";

    final static String PATH_TO_APK = PATH_TO_APK_DIR + ONDEVICEVALIDATOR_FILE;

    protected final static String VALIDATOR_APP_PACKAGE = "com.musala.atmosphere.ondevice.validator";

    protected final static String VALIDATOR_APP_ACTIVITY = ".MainActivity";

    protected static Device testDevice;

    protected static Agent agent = AtmosphereIntegrationTestsSuite.getAgent();

    protected static Server server = AtmosphereIntegrationTestsSuite.getServer();

    private final static Logger LOGGER = Logger.getLogger(BaseIntegrationTest.class);

    /*
     * Timeout constants for different type of operations.
     */
    private static final long WAKE_TIMEOUT = 1500;

    private static final long UNLOCK_TIMEOUT = 1000;

    private static final long HOME_TIMEOUT = 500;

    protected static void initTestDevice(DeviceParameters parameters) {
        try {
            GettingBuilderClass builderGet = new GettingBuilderClass();
            Builder deviceBuilder = builderGet.getBuilder();
            if (testDevice != null) {
                deviceBuilder.releaseDevice(testDevice);
            }

            testDevice = deviceBuilder.getDevice(parameters);
            assertNotNull("Could not get a device.", testDevice);

            // Assert our device is awake
            if (!testDevice.isAwake()) {
                testDevice.pressButton(HardwareButton.POWER);
                Thread.sleep(WAKE_TIMEOUT);
            }

            // Assert our device is not locked
            if (testDevice.isLocked()) {
                testDevice.setLocked(false);
                Thread.sleep(UNLOCK_TIMEOUT);
            }

            // Assert we start our test from the Home screen
            {
                testDevice.pressButton(HardwareButton.HOME);
                Thread.sleep(HOME_TIMEOUT);
            }
        } catch (InterruptedException e) {
            LOGGER.error("Device preparation for integration test has been interrupted.", e);
        }
    }

    protected static void installValidatorApplication() {
        assertNotNull("There is no allocated test device.", testDevice);

        testDevice.installAPK(PATH_TO_APK);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void releaseDevice() {
        GettingBuilderClass builderGet = new GettingBuilderClass();
        Builder deviceBuilder = builderGet.getBuilder();

        if (testDevice != null) {
            deviceBuilder.releaseDevice(testDevice);
            testDevice = null;
        }
    }
}
