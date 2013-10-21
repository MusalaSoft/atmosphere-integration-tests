package com.musala.atmosphere.testsuites;

import org.junit.runners.Suite.SuiteClasses;

import com.musala.atmosphere.agent.devicewrapper.BatteryRelatedMethodsTest;
import com.musala.atmosphere.agent.devicewrapper.DeviceAcceleratoinTest;
import com.musala.atmosphere.agent.devicewrapper.DeviceOrientationTest;
import com.musala.atmosphere.agent.devicewrapper.GetScreenshotTest;
import com.musala.atmosphere.agent.devicewrapper.GetUiXmlTest;
import com.musala.atmosphere.agent.devicewrapper.ScreenOrientationTest;
import com.musala.atmosphere.client.builder.BuilderDeviceSelectionIntegrationTest;
import com.musala.atmosphere.client.device.DeviceGetScreenshotTest;
import com.musala.atmosphere.client.device.DeviceInstallApkTest;
import com.musala.atmosphere.client.device.LockUnlockTest;
import com.musala.atmosphere.client.device.StartActivityTest;
import com.musala.atmosphere.client.device.TapTest;
import com.musala.atmosphere.server.PoolEventHandlerTest;

@SuiteClasses({DeviceOrientationTest.class, DeviceAcceleratoinTest.class, ScreenOrientationTest.class,
		PoolEventHandlerTest.class, BatteryRelatedMethodsTest.class, TapTest.class, LockUnlockTest.class,
		StartActivityTest.class, BuilderDeviceSelectionIntegrationTest.class, GetUiXmlTest.class,
		DeviceInstallApkTest.class, GetScreenshotTest.class, DeviceGetScreenshotTest.class})
// @SuiteClasses({InputTextTest.class})
public class AtmospherePassingIntegrationTestsSuite extends AtmosphereIntegrationTestsSuite
{
}
