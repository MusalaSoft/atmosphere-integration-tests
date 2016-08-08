package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallAccepted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallCanceled;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallOnHold;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallReceived;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.beans.PhoneNumber;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

public class CallTest extends BaseIntegrationTest {
    private static final String SENDER_PHONE = "+012345";

    private static final int RECEIVE_CALL_TIMEOUT = 3500;

    /**
     * The hold, cancel and accept phone call operations require much more time than receive to complete successfully.
     */
    private static final int OTHER_CALL_OPERATION_TIMEOUT = 10000;

    @BeforeClass
    public static void classSetUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);
        setTestDevice(testDevice);
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        testDevice.declineCall();
        releaseDevice();
    }

    @Before
    public void setUp() throws InterruptedException {
        testDevice.pressButton(HardwareButton.HOME);
        Thread.sleep(1000);
    }

    @After
    public void tearDown() throws Exception {
        testDevice.declineCall();
        Thread.sleep(OTHER_CALL_OPERATION_TIMEOUT);
    }

    @Test
    public void testReceiveCall() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber(SENDER_PHONE);
        assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
        Thread.sleep(RECEIVE_CALL_TIMEOUT);

        assertCallReceived("Phone call was not received.", phoneNumber);
    }

    @Test
    public void testAcceptCall() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber(SENDER_PHONE);
        assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
        Thread.sleep(RECEIVE_CALL_TIMEOUT);

        assertTrue("Accepting call returned false.", testDevice.acceptCall(phoneNumber));
        Thread.sleep(OTHER_CALL_OPERATION_TIMEOUT);

        assertCallAccepted("Phone call was not accepted.", phoneNumber);
    }

    @Test
    public void testHoldCall() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber(SENDER_PHONE);
        assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
        Thread.sleep(RECEIVE_CALL_TIMEOUT);

        assertTrue("Holding call returned false.", testDevice.holdCall(phoneNumber));
        Thread.sleep(OTHER_CALL_OPERATION_TIMEOUT);
        assertCallOnHold("Phone call was not put on hold.", phoneNumber);
    }

    @Test
    public void testCancelCall() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber(SENDER_PHONE);
        assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
        Thread.sleep(RECEIVE_CALL_TIMEOUT);

        assertCallReceived("Phone call was not received.", phoneNumber);
        assertTrue("Canceling call returned false.", testDevice.cancelCall(phoneNumber));
        Thread.sleep(OTHER_CALL_OPERATION_TIMEOUT);

        assertCallCanceled("Phone call was not canceled.");
    }
}
