package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertReceivedSms;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.SmsMessage;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

public class ReceiveSmsTest extends BaseIntegrationTest
{
	private static final String SENDER_PHONE = "+012345";

	private static final String SMS_TEXT = "Test Sms receiving text!";

	@BeforeClass
	public static void setUp() throws Exception
	{
		initTestDevice(new DeviceParameters());
		installValidatorApplication();
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(3000);
		assertValidatorIsStarted();
	}

	@Test
	public void testReceiveSms() throws Exception
	{
		SmsMessage sms = new SmsMessage(SENDER_PHONE, SMS_TEXT);
		testDevice.receiveSms(sms);
		assertReceivedSms("Sms message was not received properly.", sms);

	}
}
