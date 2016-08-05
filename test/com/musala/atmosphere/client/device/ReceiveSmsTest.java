package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertReceivedSms;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.SmsMessage;
import com.musala.atmosphere.commons.beans.PhoneNumber;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;

public class ReceiveSmsTest extends BaseIntegrationTest {
    private static final String SENDER_PHONE = "+012345";

    private static final String SMS_TEXT = "Test Sms receiving text!";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        try {
            initTestDevice(testDeviceSelector);
            setTestDevice(testDevice);

            startMainActivity();
            assertValidatorIsStarted();
        } catch (NoAvailableDeviceFoundException e) {
            // Nothing to do here
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (testDevice != null) {
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        }
        releaseDevice();
    }

    @Test
    public void testReceiveSms() throws Exception {
        assumeNotNull(testDevice);
        PhoneNumber phoneNumber = new PhoneNumber(SENDER_PHONE);
        SmsMessage sms = new SmsMessage(phoneNumber, SMS_TEXT);
        assertTrue("Sending SMS returned false.", testDevice.receiveSms(sms));
        assertReceivedSms("Sms message was not received properly.", sms);
    }
}
