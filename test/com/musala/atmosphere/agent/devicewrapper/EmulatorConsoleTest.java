package com.musala.atmosphere.agent.devicewrapper;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.agent.AgentManager;
import com.musala.atmosphere.commons.PowerProperties;
import com.musala.atmosphere.commons.beans.BatteryLevel;
import com.musala.atmosphere.commons.beans.BatteryState;
import com.musala.atmosphere.commons.beans.PowerSource;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.sa.EmulatorParameters;
import com.musala.atmosphere.commons.util.Pair;

public class EmulatorConsoleTest extends BaseIntegrationTest {
    private static final int EMULATOR_CREATION_DPI = 120;

    private static final long EMULATOR_CREATION_RAM = 256;

    private static final int EMULATOR_CREATION_RESOLUTION_H = 240;

    private static final int EMULATOR_CREATION_RESOLUTION_W = 360;

    @BeforeClass
    public static void setUp() throws Exception {
        Class<?> agentClass = agent.getClass();
        Field agentManagerField = agentClass.getDeclaredField("agentManager");
        agentManagerField.setAccessible(true);
        AgentManager agentManager = (AgentManager) agentManagerField.get(agent);

        if (!agentManager.isAnyEmulatorPresent()) {
            EmulatorParameters emulatorCreationParameters = new EmulatorParameters();
            emulatorCreationParameters.setDpi(EMULATOR_CREATION_DPI);
            emulatorCreationParameters.setRam(EMULATOR_CREATION_RAM);
            emulatorCreationParameters.setResolution(new Pair<Integer, Integer>(EMULATOR_CREATION_RESOLUTION_H,
                                                                                EMULATOR_CREATION_RESOLUTION_W));
        }

        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
    }

    @Test
    public void testSetBatteryLevel() throws Exception {
        final int initialBatteryLevel = 20;
        final int batteryLevel = 80;

        PowerProperties initialProps = new PowerProperties();
        initialProps.setBatteryLevel(new BatteryLevel(initialBatteryLevel));
        testDevice.setPowerProperties(initialProps);

        PowerProperties newProps = testDevice.getPowerProperties();
        int newBatteryLevel = newProps.getBatteryLevel().getLevel();
        assertEquals("Battery level doesn't match.", initialBatteryLevel, newBatteryLevel);

        newProps.setBatteryLevel(new BatteryLevel(batteryLevel));
        testDevice.setPowerProperties(newProps);
        PowerProperties newProps2 = testDevice.getPowerProperties();
        int newBatteryLevel2 = newProps2.getBatteryLevel().getLevel();
        assertEquals("Battery level doesn't match.", batteryLevel, newBatteryLevel2);
    }

    @Test
    public void testSetBatteryState() {
        BatteryState batteryState = BatteryState.NOT_CHARGING;
        PowerProperties properties = new PowerProperties();
        properties.setBatteryState(batteryState);
        testDevice.setPowerProperties(properties);
        PowerProperties newPowerProperties = testDevice.getPowerProperties();
        BatteryState actualBatteryState = newPowerProperties.getBatteryState();
        assertEquals("Failed setting battery state.", batteryState, actualBatteryState);
    }

    @Test
    public void testSetPowerState() throws Exception {
        PowerSource state = PowerSource.PLUGGED_AC; // Connected.
        PowerProperties properties = new PowerProperties();
        properties.setPowerSource(state);
        testDevice.setPowerProperties(properties);
        PowerProperties newProperties = testDevice.getPowerProperties();
        PowerSource newState = newProperties.getPowerSource();
        assertEquals("Power state doesn't match.", state, newState);
    }
}
