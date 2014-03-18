package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertReceivedSms;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.SmsMessage;
import com.musala.atmosphere.commons.beans.PhoneNumber;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

public class ReceiveSmsTest extends BaseIntegrationTest {
    private static final String SENDER_PHONE = "+012345";

    private static final String SMS_TEXT = "Test Sms receiving text!";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceParameters testDeviceParameters = new DeviceParameters();
        testDeviceParameters.setDeviceType(DeviceType.EMULATOR_ONLY);
        initTestDevice(testDeviceParameters);
        installValidatorApplication();
        testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
        Thread.sleep(3000);
        assertValidatorIsStarted();
    }

    @AfterClass
    public static void tearDown() {
        releaseDevice();
    }

    @Test
    public void testReceiveSms() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber(SENDER_PHONE);
        SmsMessage sms = new SmsMessage(phoneNumber, SMS_TEXT);
        assertTrue("Sending SMS returned false.", testDevice.receiveSms(sms));
        assertReceivedSms("Sms message was not received properly.", sms);
    }
}
