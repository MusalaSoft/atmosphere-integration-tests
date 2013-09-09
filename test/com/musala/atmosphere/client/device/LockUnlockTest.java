package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertValidatorIsStarted;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

/**
 * 
 * @author georgi.gaydarov
 * 
 */
public class LockUnlockTest extends BaseIntegrationTest
{
	private final static String VALIDATOR_APP_PACKAGE = "com.musala.atmosphere.ondevice.validator";

	private final static String VALIDATOR_APP_ACTIVITY = "MainActivity";

	@BeforeClass
	public static void setUp() throws Exception
	{
		initTestDevice(new DeviceParameters());
		installValidatorApplication();
	}

	@Test
	public void testUnlockLockDevice() throws Exception
	{
		testDevice.unlock();
		testDevice.pressButton(HardwareButton.HOME);

		assertFalse("Device shouldn't be locked after .unlock().", testDevice.isLocked());
		assertTrue("Device should be awake after .unlock().", testDevice.isAwake());

		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(1000);

		assertValidatorIsStarted();

		testDevice.lock();
		Thread.sleep(1500);
		try
		{
			assertValidatorIsStarted();
			fail("The validation element should not be available when the device is locked.");
		}
		catch (UiElementFetchingException e)
		{
			// this should be thrown.
		}

		assertTrue("Device should be locked after .lock().", testDevice.isLocked());
		assertFalse("Device shouldn't be awake after .lock().", testDevice.isAwake());
	}
}