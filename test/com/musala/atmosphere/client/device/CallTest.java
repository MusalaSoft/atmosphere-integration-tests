package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallAccepted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallCanceled;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallOnHold;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallReceived;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.beans.PhoneNumber;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

public class CallTest extends BaseIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(BaseIntegrationTest.class.getCanonicalName());

    private static final String SENDER_PHONE = "+012345";

    private static final String DIALER_TO_FOREGROUND_SHELL_COMMAND = "am start com.android.dialer/com.android.incallui.InCallActivity";

    private static final int RECEIVE_CALL_TIMEOUT = 3500;

    private static PhoneNumber phoneNumber;

    /**
     * The hold, cancel and accept phone call operations require much more time than receive to complete successfully.
     */
    private static final int OTHER_CALL_OPERATION_TIMEOUT = 10000;

    @BeforeClass
    public static void classSetUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY)
                                                                           .maxApi(21);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        try {
            initTestDevice(testDeviceSelector);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize a test device", e);
        }

        assumeNotNull(testDevice);
        setTestDevice(testDevice);
        phoneNumber = new PhoneNumber(SENDER_PHONE);
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        if (testDevice != null) {
            testDevice.pressButton(HardwareButton.HOME);
        }

        releaseDevice();
    }

    @After
    public void tearDown() throws Exception {
        if (testDevice != null) {
            testDevice.cancelCall(phoneNumber);
            Thread.sleep(OTHER_CALL_OPERATION_TIMEOUT);
        }
    }

    @Test
    public void testReceiveCall() throws Exception {
        assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
        Thread.sleep(RECEIVE_CALL_TIMEOUT);

        bringDialerToForeground();
        assertCallReceived("Phone call was not received.", phoneNumber);
    }

    @Test
    public void testAcceptCall() throws Exception {
        assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
        Thread.sleep(RECEIVE_CALL_TIMEOUT);

        assertTrue("Accepting call returned false.", testDevice.acceptCall(phoneNumber));
        Thread.sleep(OTHER_CALL_OPERATION_TIMEOUT);

        bringDialerToForeground();
        assertCallAccepted("Phone call was not accepted.", phoneNumber);
    }

    @Test
    public void testHoldCall() throws Exception {
        assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
        Thread.sleep(RECEIVE_CALL_TIMEOUT);
        assertTrue("Holding call returned false.", testDevice.holdCall(phoneNumber));
        Thread.sleep(OTHER_CALL_OPERATION_TIMEOUT);

        bringDialerToForeground();
        assertCallOnHold("Phone call was not put on hold.", phoneNumber);
    }

    @Test
    public void testCancelCall() throws Exception {
        assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
        Thread.sleep(RECEIVE_CALL_TIMEOUT);

        bringDialerToForeground();
        assertCallReceived("Phone call was not received.", phoneNumber);

        assertTrue("Canceling call returned false.", testDevice.cancelCall(phoneNumber));
        Thread.sleep(OTHER_CALL_OPERATION_TIMEOUT);

        bringDialerToForeground();
        assertCallCanceled("Phone call was not canceled.");
    }

    private void bringDialerToForeground() throws Exception {
        if (testDevice.getInformation().getApiLevel() >= 21) { // Lollipop
            testDevice.executeShellCommand(DIALER_TO_FOREGROUND_SHELL_COMMAND);
            Thread.sleep(OTHER_CALL_OPERATION_TIMEOUT);
        }
    }
}
