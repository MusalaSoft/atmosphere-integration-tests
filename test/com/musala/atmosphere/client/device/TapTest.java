package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertInputTextBoxIsFocused;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByClass;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;

import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.UiElementAttributes;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.geometry.Point;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;

/**
 * 
 * @author yordan.petrov
 * 
 */
public class TapTest extends BaseIntegrationTest
{
	private final static String WIDGET_RELATIVE_LAYOUT = "android.widget.RelativeLayout";

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
		inputTextBox.tap();
		assertInputTextBoxIsFocused("Input text box not focused.");
	}

	@Test
	public void testRelativeTap() throws InterruptedException, ActivityStartingException, UiElementFetchingException
	{
		testDevice.startActivity(VALIDATOR_APP_PACKAGE, VALIDATOR_APP_ACTIVITY);
		Thread.sleep(1000);
		// test relative tapping
		UiElement widgetRelativeLayout = getElementByClass(WIDGET_RELATIVE_LAYOUT);
		UiElementAttributes widgetRelativeLayoutAttributes = widgetRelativeLayout.getElementAttributes();
		UiElement batteryStatusBox = getElementByContentDescriptor(INPUT_TEXT_BOX);
		UiElementAttributes batteryStatusBoxAttributes = batteryStatusBox.getElementAttributes();

		Point batteryStatusBoxUpperLeftCorner = batteryStatusBoxAttributes.getBounds().getUpperLeftCorner();
		Point BatteryStatusRelativeUpperLeftCorner = widgetRelativeLayoutAttributes.getBounds()
																					.getRelativePoint(batteryStatusBoxUpperLeftCorner);

		widgetRelativeLayout.tap(BatteryStatusRelativeUpperLeftCorner, false);

		assertInputTextBoxIsFocused("Input text box not focused.");
	}
}