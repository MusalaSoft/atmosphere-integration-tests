package com.musala.atmosphere.testsuites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.musala.atmosphere.agent.Agent;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.server.Server;
import com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert;

/**
 * JUnit test suite with all running integration tests.
 *
 * @author valyo.yolovski
 *
 */
@RunWith(Suite.class)
public class AtmosphereIntegrationTestsSuite {
    private final static int SERVER_WEBSOCKET_PORT = 8025;

    private final static int WAIT_FOR_CONNECTING_TIMEOUT = 25_000;// 10_000; // 145_000

    private final static long SCREEN_OFF_TIMEOUT = 180_000;

    private final static String SERVER_IP = "localhost";

    private static Agent agent;

    private static Server server;

    @com.musala.atmosphere.client.util.Server(ip = SERVER_IP, port = SERVER_WEBSOCKET_PORT, connectionRetryLimit = 0)
    private static class GettingBuilderClass {
        public GettingBuilderClass() {
        }

        public Builder getBuilder() {
            Builder classDeviceBuilder = Builder.getInstance();
            return classDeviceBuilder;
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Start and run server.
        server = new Server(SERVER_IP, SERVER_WEBSOCKET_PORT);
        server.run();

        // Start and connect agent to the server.
        agent = new Agent();
        agent.connectToServer(SERVER_IP, SERVER_WEBSOCKET_PORT);
        String agentId = agent.getId();
        server.waitForGivenAgentToConnect(agentId);
        // Waits for deive's wrapping and connection
        // TODO Find a better way to handle this
        Thread.sleep(WAIT_FOR_CONNECTING_TIMEOUT);

        // Install OnDeviceValidator on all available devices.
        GettingBuilderClass builderGet = new GettingBuilderClass();
        Builder deviceBuilder = builderGet.getBuilder();

        // Attempts to select an emulator
        DeviceSelectorBuilder emulatorSelectorBuilder = new DeviceSelectorBuilder();
        //DeviceSelector emulatorSelector = emulatorSelectorBuilder.deviceType(DeviceType.EMULATOR_ONLY).build();
        Device device = null;
        /*try {
            device = deviceBuilder.getDevice(emulatorSelector);
            OnDeviceValidatorAssert.setTestDevice(device);
            OnDeviceValidatorAssert.setupOndeviceValidator();
            deviceBuilder.releaseDevice(device);
        } catch (NoAvailableDeviceFoundException e) {
            // Nothing to do here
        }*/

        // Attempts to select a real device
        DeviceSelector realDeviceSelector = emulatorSelectorBuilder.deviceType(DeviceType.DEVICE_PREFERRED).build();
        try {
            device = deviceBuilder.getDevice(realDeviceSelector);
            OnDeviceValidatorAssert.setTestDevice(device);
            OnDeviceValidatorAssert.setupOndeviceValidator();
            device.setScreenOffTimeout(SCREEN_OFF_TIMEOUT);
            deviceBuilder.releaseDevice(device);
        } catch (NoAvailableDeviceFoundException e) {
            // Nothing to do here
        }

        if (device == null) {
            //throw new NoAvailableDeviceFoundException("No devices are present on the current agent.");
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Master tear down.
        server.exit();
        agent.stop();
    }

    public static Agent getAgent() {
        return agent;
    }

    public static Server getServer() {
        return server;
    }
}
