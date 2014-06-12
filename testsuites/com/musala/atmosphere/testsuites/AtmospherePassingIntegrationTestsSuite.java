package com.musala.atmosphere.testsuites;

import org.junit.runners.Suite.SuiteClasses;

import com.musala.atmosphere.agent.devicewrapper.EmulatorCameraTest;
import com.musala.atmosphere.agent.devicewrapper.GetScreenshotTest;
import com.musala.atmosphere.agent.devicewrapper.GetUiXmlTest;
import com.musala.atmosphere.agent.devicewrapper.ScreenOrientationTest;
import com.musala.atmosphere.agent.devicewrapper.TelephonyInformationTest;
import com.musala.atmosphere.client.builder.BuilderDeviceSelectionIntegrationTest;
import com.musala.atmosphere.client.device.CallTest;
import com.musala.atmosphere.client.device.DeviceGetScreenshotTest;
import com.musala.atmosphere.client.device.DeviceInstallApkTest;
import com.musala.atmosphere.client.device.DoubleTapTest;
import com.musala.atmosphere.client.device.GestureExecutionTest;
import com.musala.atmosphere.client.device.HorizontalScrollTest;
import com.musala.atmosphere.client.device.LockUnlockTest;
import com.musala.atmosphere.client.device.LongPressTest;
import com.musala.atmosphere.client.device.PinchTest;
import com.musala.atmosphere.client.device.ReceiveSmsTest;
import com.musala.atmosphere.client.device.ScrollTest;
import com.musala.atmosphere.client.device.StartActivityTest;
import com.musala.atmosphere.client.device.StartApplicationTest;
import com.musala.atmosphere.client.device.SwipeTest;
import com.musala.atmosphere.client.device.TapTest;
import com.musala.atmosphere.client.device.WaitForExistsTest;
import com.musala.atmosphere.client.device.WaitForWindowUpdateTest;
import com.musala.atmosphere.server.PoolEventHandlerTest;

@SuiteClasses({ScreenOrientationTest.class, PoolEventHandlerTest.class, TapTest.class, LockUnlockTest.class,
        StartActivityTest.class, BuilderDeviceSelectionIntegrationTest.class, GetUiXmlTest.class,
        DeviceInstallApkTest.class, GetScreenshotTest.class, DeviceGetScreenshotTest.class, ReceiveSmsTest.class,
        GestureExecutionTest.class, TelephonyInformationTest.class, StartApplicationTest.class, DoubleTapTest.class,
        PinchTest.class, SwipeTest.class, CallTest.class, LongPressTest.class, WaitForExistsTest.class,
        ScrollTest.class, HorizontalScrollTest.class, WaitForWindowUpdateTest.class, EmulatorCameraTest.class})
// @SuiteClasses({EmulatorCameraTest.class})
public class AtmospherePassingIntegrationTestsSuite extends AtmosphereIntegrationTestsSuite {
}
