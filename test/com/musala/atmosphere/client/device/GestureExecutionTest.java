package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInputTextBoxIsFocused;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.UiElementAttributes;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.geometry.Bounds;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.gesture.Anchor;
import com.musala.atmosphere.commons.gesture.Gesture;
import com.musala.atmosphere.commons.gesture.Timeline;

/**
 * Modified by georgi.gaydarov, originally by
 * 
 * @author yordan.petrov
 * 
 */
public class GestureExecutionTest extends BaseIntegrationTest
{
	private final static String WIDGET_MAIN_LAYOUT = "MainLinearLayout";

	private final static String INPUT_TEXT_BOX = "InputTextBox";

	@BeforeClass
	public static void setUp()
	{
		initTestDevice(new DeviceParameters());
		installValidatorApplication();
	}

	@Test
	public void testTap() throws InterruptedException, ActivityStartingException, UiElementFetchingException
	{
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(1000);
		// test tapping
		UiElement inputTextBox = getElementByContentDescriptor(INPUT_TEXT_BOX);
		UiElementAttributes attributes = inputTextBox.getElementAttributes();
		Bounds boxBounds = attributes.getBounds();
		int xCoord = boxBounds.getCenter().getX();
		int yCoord = boxBounds.getCenter().getY();
		Anchor tapPoint = new Anchor(xCoord, yCoord, 0);
		Timeline tapTimeline = new Timeline();
		tapTimeline.add(tapPoint);
		Gesture tapGesture = new Gesture();
		tapGesture.add(tapTimeline);
		testDevice.executeGesture(tapGesture);
		assertInputTextBoxIsFocused("Input text box not focused.");
	}
}