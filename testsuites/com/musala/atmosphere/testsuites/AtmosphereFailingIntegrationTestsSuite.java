package com.musala.atmosphere.testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.musala.atmosphere.agent.devicewrapper.BatteryRelatedMethodsTest;
import com.musala.atmosphere.agent.devicewrapper.DeviceOrientationTest;
import com.musala.atmosphere.agent.devicewrapper.DeviceProximityTest;
import com.musala.atmosphere.agent.devicewrapper.EmulatorConsoleTest;
import com.musala.atmosphere.agent.devicewrapper.NetworkConnectionTest;
import com.musala.atmosphere.client.device.LogCatFiltersTest;
import com.musala.atmosphere.client.device.ScreenRecordingTest;
import com.musala.atmosphere.client.device.SwipeTest;
import com.musala.atmosphere.client.device.WifiConnectionQualityTest;
import com.musala.atmosphere.server.EmulatorCreationTest;
import com.musala.atmosphere.server.PoolEventHandlerTest;
import com.musala.atmosphere.service.AtmosphereServiceConnectionTest;

/**
 * JUnit test suite with all failing integration tests.
 *
 * @author valyo.yolovski
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
         DeviceOrientationTest.class,
         DeviceProximityTest.class,
         WifiConnectionQualityTest.class,
         EmulatorConsoleTest.class,
         EmulatorCreationTest.class,
         NetworkConnectionTest.class,
         SwipeTest.class,
         AtmosphereServiceConnectionTest.class,
         PoolEventHandlerTest.class,
         LogCatFiltersTest.class,
         BatteryRelatedMethodsTest.class,
         ScreenRecordingTest.class,
        })
public class AtmosphereFailingIntegrationTestsSuite extends AtmosphereIntegrationTestsSuite {
}
