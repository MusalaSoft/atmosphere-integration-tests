package com.musala.atmosphere.testsuites;

import org.junit.runners.Suite.SuiteClasses;

import com.musala.atmosphere.agent.devicewrapper.EmulatorCameraTest;
import com.musala.atmosphere.agent.devicewrapper.GetScreenshotTest;
import com.musala.atmosphere.agent.devicewrapper.ScreenOrientationTest;
import com.musala.atmosphere.agent.devicewrapper.TelephonyInformationTest;
import com.musala.atmosphere.client.device.BringTaskToFrontTest;
import com.musala.atmosphere.client.device.CallTest;
import com.musala.atmosphere.client.device.ChangeGpsLocationStateTest;
import com.musala.atmosphere.client.device.ClearDataTest;
import com.musala.atmosphere.client.device.ClearTextTest;
import com.musala.atmosphere.client.device.CopyTextTest;
import com.musala.atmosphere.client.device.CutTextTest;
import com.musala.atmosphere.client.device.DatePickerInteractionTest;
import com.musala.atmosphere.client.device.DeviceGetScreenshotTest;
import com.musala.atmosphere.client.device.DeviceInstallApkTest;
import com.musala.atmosphere.client.device.DisableMockLocationTest;
import com.musala.atmosphere.client.device.DiskSpaceTest;
import com.musala.atmosphere.client.device.DoubleTapTest;
import com.musala.atmosphere.client.device.DragTest;
import com.musala.atmosphere.client.device.ForceStopProcessTest;
import com.musala.atmosphere.client.device.GestureExecutionTest;
import com.musala.atmosphere.client.device.GetLastToastTest;
import com.musala.atmosphere.client.device.GetRunningTaskIdsTest;
import com.musala.atmosphere.client.device.HorizontalScrollTest;
import com.musala.atmosphere.client.device.InputTextTest;
import com.musala.atmosphere.client.device.LockUnlockTest;
import com.musala.atmosphere.client.device.LogCatFiltersTest;
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
import com.musala.atmosphere.client.device.RuntimePermissionTest;
import com.musala.atmosphere.client.device.ScreenOffTimeoutTest;
import com.musala.atmosphere.client.device.ScreenRecordingTest;
import com.musala.atmosphere.client.device.ScrollTest;
import com.musala.atmosphere.client.device.SelectAllTextTest;
import com.musala.atmosphere.client.device.SetIMEAsDefaultTest;
import com.musala.atmosphere.client.device.SetKeyguardTest;
import com.musala.atmosphere.client.device.StartActivityTest;
import com.musala.atmosphere.client.device.StartApplicationTest;
import com.musala.atmosphere.client.device.StopBackgroundProcessTest;
import com.musala.atmosphere.client.device.TapTest;
import com.musala.atmosphere.client.device.TimePickerInteractionTest;
import com.musala.atmosphere.client.device.UninstallApplicationTest;
import com.musala.atmosphere.client.device.WaitForExistsTest;
import com.musala.atmosphere.client.device.WaitForTaskUpdateTest;
import com.musala.atmosphere.client.device.WaitForWindowUpdateTest;
import com.musala.atmosphere.client.device.WaitUntilGoneTest;
import com.musala.atmosphere.client.screen.GetAccessibilityUiElementTest;
import com.musala.atmosphere.client.screen.GetAccessibilityUiElementsTest;
import com.musala.atmosphere.client.screen.GetElementByCssTest;
import com.musala.atmosphere.client.screen.GetElementByXPathTest;
import com.musala.atmosphere.client.screen.GetElementWhenPresentTest;
import com.musala.atmosphere.client.screen.GetElementsByXPathTest;
import com.musala.atmosphere.client.uielement.GetAccessibilityUiElementChildrenTest;
import com.musala.atmosphere.client.uielement.GetElementChildrenByXPathTest;
import com.musala.atmosphere.client.uielement.UiElementRevalidationTest;
import com.musala.atmosphere.webview.ClearWebElementTextTest;
import com.musala.atmosphere.webview.FindWebElementTest;
import com.musala.atmosphere.webview.GetSizeOfWebElementTest;
import com.musala.atmosphere.webview.GetWebElementRelativePositionTest;
import com.musala.atmosphere.webview.GetWebElementTagTest;
import com.musala.atmosphere.webview.GetWebElementTextTest;
import com.musala.atmosphere.webview.TapWebElementTest;
import com.musala.atmosphere.webview.WebElementGetCssValueTest;
import com.musala.atmosphere.webview.WebElementInputTextTest;
import com.musala.atmosphere.webview.WebElementIsDisplayedTest;
import com.musala.atmosphere.webview.WebElementIsEnabledTest;
import com.musala.atmosphere.webview.WebElementIsSelectedTest;
import com.musala.atmosphere.webview.WebElementWaitForConditionTest;

@SuiteClasses({ScreenOrientationTest.class, TapTest.class, StartActivityTest.class, LockUnlockTest.class,
        DeviceInstallApkTest.class, GetScreenshotTest.class, DeviceGetScreenshotTest.class, ReceiveSmsTest.class, CallTest.class,
        GestureExecutionTest.class, TelephonyInformationTest.class, StartApplicationTest.class, DoubleTapTest.class,
        PinchTest.class, LongPressTest.class, WaitForExistsTest.class, ScrollTest.class, HorizontalScrollTest.class,
        WaitForWindowUpdateTest.class, EmulatorCameraTest.class, WaitUntilGoneTest.class, ProcessRunningTest.class,
        ForceStopProcessTest.class, StopBackgroundProcessTest.class, GetElementWhenPresentTest.class,
        SetIMEAsDefaultTest.class, OpenNotificationTest.class, OpenQuickSettingsTest.class, NotificationBarTest.class,
        NotificationInteractionTest.class, UninstallApplicationTest.class, WaitForTaskUpdateTest.class,
        RuntimePermissionTest.class, BringTaskToFrontTest.class, MockLocationTest.class, DisableMockLocationTest.class,
        InputTextTest.class, ClearTextTest.class, SelectAllTextTest.class, PasteTextTest.class, CopyTextTest.class,
        CutTextTest.class, DatePickerInteractionTest.class, TimePickerInteractionTest.class,
        GetRunningTaskIdsTest.class, ScreenRecordingTest.class, SetKeyguardTest.class, ScreenOffTimeoutTest.class,
        GetLastToastTest.class, GetAccessibilityUiElementsTest.class, GetAccessibilityUiElementTest.class,
        GetAccessibilityUiElementChildrenTest.class, UiElementRevalidationTest.class, ClearDataTest.class,
        DragTest.class, FindWebElementTest.class, WebElementIsDisplayedTest.class,
        WebElementIsEnabledTest.class, WebElementIsSelectedTest.class, GetWebElementTagTest.class,
        WebElementInputTextTest.class, WebElementGetCssValueTest.class, WebElementWaitForConditionTest.class,
        ClearWebElementTextTest.class, GetSizeOfWebElementTest.class, TapWebElementTest.class,
        GetWebElementRelativePositionTest.class, GetWebElementTextTest.class, DiskSpaceTest.class,
        GetElementChildrenByXPathTest.class, GetElementsByXPathTest.class, GetElementByXPathTest.class,
        GetElementByCssTest.class, ChangeGpsLocationStateTest.class, LogCatFiltersTest.class})
public class AtmospherePassingIntegrationTestsSuite extends AtmosphereIntegrationTestsSuite {
}
