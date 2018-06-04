package com.musala.atmosphere;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.AfterClass;

import com.musala.atmosphere.agent.Agent;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.device.HardwareButton;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.exception.NoDeviceMatchingTheGivenSelectorException;
import com.musala.atmosphere.commons.exceptions.DeviceNotFoundException;
import com.musala.atmosphere.server.Server;
import com.musala.atmosphere.test.util.Constants;
import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;

/**
 *
 * @author valyo.yolovski
 *
 */
public class BaseIntegrationTest {

    @com.musala.atmosphere.client.util.Server(ip = Constants.SERVER_IP, port = Constants.SERVER_PORT, connectionRetryLimit = 0)
    private static class GettingBuilderClass {
        public GettingBuilderClass() {
        }

        public Builder getBuilder() {
            Builder classDeviceBuilder = Builder.getInstance();
            return classDeviceBuilder;
        }
    }

    protected final static String ONDEVICEVALIDATOR_FILE = "OnDeviceValidator-release.apk";

    protected final static String PATH_TO_APK_DIR = "./onDeviceComponents/";

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

    protected static void initTestDevice(DeviceSelector deviceSelector)
        throws DeviceNotFoundException,
            NoDeviceMatchingTheGivenSelectorException {
        try {
            GettingBuilderClass builderGet = new GettingBuilderClass();
            Builder deviceBuilder = builderGet.getBuilder();
            if (testDevice != null) {
                deviceBuilder.releaseDevice(testDevice);
            }

            testDevice = deviceBuilder.getDevice(deviceSelector);

            assertNotNull("Could not get a device.", testDevice);

            // Assert our device is awake
            if (!testDevice.isAwake()) {
                testDevice.pressButton(HardwareButton.POWER);
                Thread.sleep(WAKE_TIMEOUT);
            }

            // Assert our device is not locked
            if (testDevice.isLocked()) {
                testDevice.unlock();
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

        testDevice.installAPK(PATH_TO_APK, true);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void releaseDevice() throws DeviceNotFoundException {
        GettingBuilderClass builderGet = new GettingBuilderClass();
        Builder deviceBuilder = builderGet.getBuilder();

        if (testDevice != null) {
            deviceBuilder.releaseDevice(testDevice);
            testDevice = null;
        }
    }
}
