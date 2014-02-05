package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallReceived;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallCanceled;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallOnHold;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertCallAccepted;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.commons.PhoneNumber;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;

public class CallTest extends BaseIntegrationTest
{
	private static final String SENDER_PHONE = "+012345";

	@BeforeClass
	public static void setUp() throws Exception
	{
		DeviceParameters testDeviceParameters = new DeviceParameters();
		testDeviceParameters.setDeviceType(DeviceType.EMULATOR_ONLY);
		initTestDevice(testDeviceParameters);
		setTestDevice(testDevice);
	}

	@After
	public void tearDown() throws Exception
	{
		testDevice.declineCall();
		Thread.sleep(3000);
	}

	@Test
	public void testReceiveCall() throws Exception
	{
		PhoneNumber phoneNumber = new PhoneNumber(SENDER_PHONE);
		assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
		Thread.sleep(3000);
		assertCallReceived("Phone call was not received.", phoneNumber);
	}

	@Test
	public void testAcceptCall() throws Exception
	{
		PhoneNumber phoneNumber = new PhoneNumber(SENDER_PHONE);
		assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
		Thread.sleep(3000);
		assertTrue("Accepting call returned false.", testDevice.acceptCall(phoneNumber));
		Thread.sleep(3000);
		assertCallAccepted("Phone call was not accepted.", phoneNumber);
	}

	@Test
	public void testHoldCall() throws Exception
	{
		PhoneNumber phoneNumber = new PhoneNumber(SENDER_PHONE);
		assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
		Thread.sleep(3000);
		assertTrue("Holding call returned false.", testDevice.holdCall(phoneNumber));
		Thread.sleep(3000);
		assertCallOnHold("Phone call was not put on hold.", phoneNumber);
	}

	@Test
	public void testCancelCall() throws Exception
	{
		PhoneNumber phoneNumber = new PhoneNumber(SENDER_PHONE);
		assertTrue("Receiving call returned false.", testDevice.receiveCall(phoneNumber));
		Thread.sleep(3000);
		assertCallReceived("Phone call was not received.", phoneNumber);
		assertTrue("Canceling call returned false.", testDevice.cancelCall(phoneNumber));
		Thread.sleep(3000);
		assertCallCanceled("Phone call was not canceled.");
	}
}
