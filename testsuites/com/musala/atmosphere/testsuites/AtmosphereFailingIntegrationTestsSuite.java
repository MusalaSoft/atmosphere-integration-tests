package com.musala.atmosphere.testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.musala.atmosphere.agent.devicewrapper.BatteryRelatedMethodsTest;
import com.musala.atmosphere.agent.devicewrapper.DeviceAcceleratoinTest;
import com.musala.atmosphere.agent.devicewrapper.DeviceOrientationTest;
import com.musala.atmosphere.agent.devicewrapper.EmulatorConsoleTest;
import com.musala.atmosphere.agent.devicewrapper.NetworkConnectionTest;
import com.musala.atmosphere.client.builder.BuilderIntegrationTest;
import com.musala.atmosphere.client.device.SetKeyguardTest;
import com.musala.atmosphere.server.EmulatorCreationTest;

/**
 * JUnit test suite with all failing integration tests.
 * 
 * @author valyo.yolovski
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({DeviceAcceleratoinTest.class, BatteryRelatedMethodsTest.class, DeviceOrientationTest.class,
        BuilderIntegrationTest.class, EmulatorConsoleTest.class, EmulatorCreationTest.class,
        NetworkConnectionTest.class})
public class AtmosphereFailingIntegrationTestsSuite extends AtmosphereIntegrationTestsSuite {
}
