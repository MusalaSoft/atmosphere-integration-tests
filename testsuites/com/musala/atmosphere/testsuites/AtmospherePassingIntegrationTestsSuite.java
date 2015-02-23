package com.musala.atmosphere.testsuites;

import org.junit.runners.Suite.SuiteClasses;

import com.musala.atmosphere.agent.devicewrapper.DeviceProximityTest;
import com.musala.atmosphere.agent.devicewrapper.EmulatorCameraTest;
import com.musala.atmosphere.agent.devicewrapper.GetScreenshotTest;
import com.musala.atmosphere.agent.devicewrapper.GetUiXmlTest;
import com.musala.atmosphere.agent.devicewrapper.ScreenOrientationTest;
import com.musala.atmosphere.agent.devicewrapper.TelephonyInformationTest;
import com.musala.atmosphere.client.builder.BuilderDeviceSelectionIntegrationTest;
import com.musala.atmosphere.client.device.BringTaskToFrontTest;
import com.musala.atmosphere.client.device.CallTest;
import com.musala.atmosphere.client.device.ClearTextTest;
import com.musala.atmosphere.client.device.CopyTextTest;
import com.musala.atmosphere.client.device.CutTextTest;
import com.musala.atmosphere.client.device.DeviceGetScreenshotTest;
import com.musala.atmosphere.client.device.DeviceInstallApkTest;
import com.musala.atmosphere.client.device.DoubleTapTest;
import com.musala.atmosphere.client.device.ForceStopProcessTest;
import com.musala.atmosphere.client.device.GestureExecutionTest;
import com.musala.atmosphere.client.device.HorizontalScrollTest;
import com.musala.atmosphere.client.device.InputTextTest;
import com.musala.atmosphere.client.device.LongPressTest;
import com.musala.atmosphere.client.device.MockLocationTest;
import com.musala.atmosphere.client.device.NotificationBarTest;
import com.musala.atmosphere.client.device.NotificationInteractionTest;
import com.musala.atmosphere.client.device.OpenNotificationTest;
import com.musala.atmosphere.client.device.OpenQuickSettingsTest;
import com.musala.atmosphere.client.device.PasteTextTest;
import com.musala.atmosphere.client.device.PinchTest;
import com.musala.atmosphere.client.device.ProcessRunningTest;
import com.musala.atmosphere.client.device.ReceiveSmsTest;
import com.musala.atmosphere.client.device.ScrollTest;
import com.musala.atmosphere.client.device.SelectAllTextTest;
import com.musala.atmosphere.client.device.SetIMEAsDefaultTest;
import com.musala.atmosphere.client.device.StartActivityTest;
import com.musala.atmosphere.client.device.StartApplicationTest;
import com.musala.atmosphere.client.device.StopBackgroundProcessTest;
import com.musala.atmosphere.client.device.TapTest;
import com.musala.atmosphere.client.device.UninstallApplicationTest;
import com.musala.atmosphere.client.device.WaitForExistsTest;
import com.musala.atmosphere.client.device.WaitForWindowUpdateTest;
import com.musala.atmosphere.client.device.WaitUntilGoneTest;
import com.musala.atmosphere.client.screen.GetElementWhenPresentTest;
import com.musala.atmosphere.server.PoolEventHandlerTest;
import com.musala.atmosphere.service.AtmosphereServiceConnectionTest;

@SuiteClasses({ScreenOrientationTest.class, TapTest.class, StartActivityTest.class,
        BuilderDeviceSelectionIntegrationTest.class, GetUiXmlTest.class, DeviceInstallApkTest.class,
        GetScreenshotTest.class, DeviceGetScreenshotTest.class, ReceiveSmsTest.class, GestureExecutionTest.class,
        TelephonyInformationTest.class, StartApplicationTest.class, DoubleTapTest.class, PinchTest.class,
        AtmosphereServiceConnectionTest.class, CallTest.class, LongPressTest.class, WaitForExistsTest.class,
        ScrollTest.class, HorizontalScrollTest.class, WaitForWindowUpdateTest.class, EmulatorCameraTest.class,
        WaitUntilGoneTest.class, ProcessRunningTest.class, ForceStopProcessTest.class, StopBackgroundProcessTest.class,
        GetElementWhenPresentTest.class, SetIMEAsDefaultTest.class, OpenNotificationTest.class,
        OpenQuickSettingsTest.class, NotificationBarTest.class, NotificationInteractionTest.class,
        UninstallApplicationTest.class, DeviceProximityTest.class, BringTaskToFrontTest.class, MockLocationTest.class,
        PoolEventHandlerTest.class, InputTextTest.class, ClearTextTest.class, SelectAllTextTest.class,
        PasteTextTest.class, CopyTextTest.class, CutTextTest.class})
// @SuiteClasses({SetIMEAsDefaultTest.class, InputTextTest.class, ClearTextTest.class, SelectAllTextTest.class,
// PasteTextTest.class})
public class AtmospherePassingIntegrationTestsSuite extends AtmosphereIntegrationTestsSuite {
}
