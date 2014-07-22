package com.musala.atmosphere.testsuites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.musala.atmosphere.agent.Agent;
import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.commons.sa.exceptions.NoAvailableDeviceFoundException;
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
    private final static int SERVER_MANAGER_RMI_PORT = 2099;

    private final static int AGENTMANAGER_RMI_PORT = 2000;

    private final static int WAIT_FOR_CONNECTING_TIMEOUT = 3000;

    private final static String SERVER_IP = "localhost";

    private static Agent agent;

    private static Server server;

    @com.musala.atmosphere.client.util.Server(ip = SERVER_IP, port = SERVER_MANAGER_RMI_PORT, connectionRetryLimit = 0)
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
        server = new Server(SERVER_MANAGER_RMI_PORT);
        server.run();

        // Start and connect agent to the server.
        agent = new Agent(AGENTMANAGER_RMI_PORT);
        agent.connectToServer(SERVER_IP, SERVER_MANAGER_RMI_PORT);
        String agentId = agent.getId();
        server.waitForGivenAgentToConnect(agentId);
        Thread.sleep(WAIT_FOR_CONNECTING_TIMEOUT);

        // Install OnDeviceValidator on all available devices.
        GettingBuilderClass builderGet = new GettingBuilderClass();
        Builder deviceBuilder = builderGet.getBuilder();

        DeviceParameters emulatorDeviceParameters = new DeviceParameters();
        emulatorDeviceParameters.setDeviceType(DeviceType.EMULATOR_ONLY);

        Device device = deviceBuilder.getDevice(emulatorDeviceParameters);

        OnDeviceValidatorAssert.setTestDevice(device);
        OnDeviceValidatorAssert.setupOndeviceValidator();

        deviceBuilder.releaseAllDevices();

        DeviceParameters realDeviceParameters = new DeviceParameters();
        realDeviceParameters.setDeviceType(DeviceType.DEVICE_PREFERRED);

        // TODO: Remove this workaround when DeviceType.DevicePrefered selection is implemented.
        try {
            realDeviceParameters.setDeviceType(DeviceType.DEVICE_ONLY);
            device = deviceBuilder.getDevice(realDeviceParameters);
        } catch (NoAvailableDeviceFoundException e) {
            realDeviceParameters.setDeviceType(null);
            device = deviceBuilder.getDevice(realDeviceParameters);
        }

        OnDeviceValidatorAssert.setTestDevice(device);
        OnDeviceValidatorAssert.setupOndeviceValidator();

        deviceBuilder.releaseAllDevices();
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
