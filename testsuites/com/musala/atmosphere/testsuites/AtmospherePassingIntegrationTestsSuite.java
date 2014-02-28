package com.musala.atmosphere.testsuites;

import org.junit.runners.Suite.SuiteClasses;

import com.musala.atmosphere.agent.devicewrapper.BatteryRelatedMethodsTest;

//@SuiteClasses({ScreenOrientationTest.class, PoolEventHandlerTest.class, BatteryRelatedMethodsTest.class, TapTest.class,
//		LockUnlockTest.class, StartActivityTest.class, BuilderDeviceSelectionIntegrationTest.class, GetUiXmlTest.class,
//		DeviceInstallApkTest.class, GetScreenshotTest.class, DeviceGetScreenshotTest.class, ReceiveSmsTest.class,
//		CallTest.class, GestureExecutionTest.class, TelephonyInformationTest.class})
@SuiteClasses({BatteryRelatedMethodsTest.class})
public class AtmospherePassingIntegrationTestsSuite extends AtmosphereIntegrationTestsSuite {
}
