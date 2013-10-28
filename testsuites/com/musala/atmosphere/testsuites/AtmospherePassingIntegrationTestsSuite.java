package com.musala.atmosphere.testsuites;

import org.junit.runners.Suite.SuiteClasses;

import com.musala.atmosphere.server.PoolEventHandlerTest;
import com.musala.atmosphere.testsuites.AtmosphereIntegrationTestsSuite;
import com.musala.atmosphere.agent.devicewrapper.BatteryRelatedMethodsTest;
import com.musala.atmosphere.agent.devicewrapper.GetScreenshotTest;
import com.musala.atmosphere.agent.devicewrapper.GetUiXmlTest;
import com.musala.atmosphere.agent.devicewrapper.ScreenOrientationTest;
import com.musala.atmosphere.client.builder.BuilderDeviceSelectionIntegrationTest;
import com.musala.atmosphere.client.device.DeviceGetScreenshotTest;
import com.musala.atmosphere.client.device.DeviceInstallApkTest;
import com.musala.atmosphere.client.device.LockUnlockTest;
import com.musala.atmosphere.client.device.StartActivityTest;
import com.musala.atmosphere.client.device.TapTest;


@SuiteClasses({ScreenOrientationTest.class,
		PoolEventHandlerTest.class, BatteryRelatedMethodsTest.class, TapTest.class, LockUnlockTest.class,
		StartActivityTest.class, BuilderDeviceSelectionIntegrationTest.class, GetUiXmlTest.class,
		DeviceInstallApkTest.class, GetScreenshotTest.class, DeviceGetScreenshotTest.class})
//@SuiteClasses({BatteryRelatedMethodsTest.class})
public class AtmospherePassingIntegrationTestsSuite extends AtmosphereIntegrationTestsSuite
{
}
