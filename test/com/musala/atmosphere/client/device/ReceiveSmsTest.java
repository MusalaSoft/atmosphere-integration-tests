package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertReceivedSms;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startMainActivity;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.SmsMessage;
import com.musala.atmosphere.commons.beans.PhoneNumber;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;

public class ReceiveSmsTest extends BaseIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(ReceiveSmsTest.class.getCanonicalName());

    private static final String SENDER_PHONE = "+012345";

    private static final String SMS_TEXT = "Test Sms receiving text!";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector testDeviceSelector = selectorBuilder.build();

        try {
            initTestDevice(testDeviceSelector);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize a test device", e);
        }

        assumeNotNull(testDevice);

        setTestDevice(testDevice);
        startMainActivity();
        assertValidatorIsStarted();
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
