package com.musala.atmosphere.testsuites;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.musala.atmosphere.agent.AgentIntegrationEnvironment;
import com.musala.atmosphere.agent.devicewrapper.AbstractWrapDevice;
import com.musala.atmosphere.commons.sa.DeviceParameters;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.commons.util.Pair;
import com.musala.atmosphere.server.ServerIntegrationEnvironment;

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

    private static AgentIntegrationEnvironment agentEnvironment;

    private static ServerIntegrationEnvironment serverEnvironment;

    private static final int EMULATOR_CREATION_DPI = 120;

    private static final int EMULATOR_CREATION_RAM = 256;

    private static final int EMULATOR_CREATION_RESOLUTION_H = 320;

    private static final int EMULATOR_CREATION_RESOLUTION_W = 240;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Start agent and server.
        agentEnvironment = new AgentIntegrationEnvironment(AGENTMANAGER_RMI_PORT);
        serverEnvironment = new ServerIntegrationEnvironment(SERVER_MANAGER_RMI_PORT);

        // Connect agent to the server
        agentEnvironment.connectToLocalhostServer(SERVER_MANAGER_RMI_PORT);
        String agentId = agentEnvironment.getUnderlyingAgentId();
        serverEnvironment.waitForAgentConnection(agentId);

        // Create default emulator if non exists
        if (!agentEnvironment.isAnyEmulatorPresent()) {
            AbstractWrapDevice deviceWrapper = (AbstractWrapDevice) createDefaultEmulator();
            agentEnvironment.waitForDeviceOsToStart(deviceWrapper);
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Master tear down.
        serverEnvironment.close();
        agentEnvironment.close();
    }

    public static AgentIntegrationEnvironment getAgentEnvironment() {
        return agentEnvironment;
    }

    public static ServerIntegrationEnvironment getServerEnvironment() {
        return serverEnvironment;
    }

    private static IWrapDevice createDefaultEmulator() throws IOException, NotBoundException, TimeoutException {
        DeviceParameters emulatorCreationParameters = new DeviceParameters();
        emulatorCreationParameters.setDpi(EMULATOR_CREATION_DPI);
        emulatorCreationParameters.setRam(EMULATOR_CREATION_RAM);
        emulatorCreationParameters.setResolution(new Pair<Integer, Integer>(EMULATOR_CREATION_RESOLUTION_W,
                                                                            EMULATOR_CREATION_RESOLUTION_H));
        IWrapDevice deviceWrapper = agentEnvironment.startEmulator(emulatorCreationParameters);
        return deviceWrapper;
    }

}
