package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInputText;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

/**
 * @author valyo.yolovski
 */
public class InputTextTest extends BaseIntegrationTest
{
	private final static String INPUT_TEXT_BOX = "InputTextBox";

	@BeforeClass
	public static void setUp() throws Exception
	{
		initTestDevice(new DeviceParameters());
	}

	@Test
	public void inputTextTestOne() throws Exception
	{
		installValidatorApplication();
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY, true);
		Thread.sleep(1000);
		String textToInput = "Hi! Кирилица. €%@$§№%()456*/0,.";
		UiElement inputTextBox = getElementByContentDescriptor(INPUT_TEXT_BOX);
		inputTextBox.inputText(textToInput);
		assertInputText("Inputting text failed.", textToInput);
	}

	@Test
	public void inputTextTestTwo() throws Exception
	{
		testDevice.pressButton(HardwareButton.HOME);
		installValidatorApplication();
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY, true);
		Thread.sleep(1000);
		String textToInput = "Letters."; // Text to input.
		int inputInterval = 500;// Time interval between the input of each letter in ms.
		UiElement inputTextBox = getElementByContentDescriptor(INPUT_TEXT_BOX);
		inputTextBox.inputText(textToInput, inputInterval);
		assertInputText("Inputting text failed.", textToInput);
	}
}
