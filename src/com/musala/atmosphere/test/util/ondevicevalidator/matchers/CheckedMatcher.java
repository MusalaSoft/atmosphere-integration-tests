package com.musala.atmosphere.test.util.ondevicevalidator.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.UiElementAttributes;

/**
 * Base Matcher class used for checking if a given UI element is checked.
 * 
 * @author yordan.petrov
 * 
 */
public class CheckedMatcher extends BaseMatcher<UiElement>
{
	@Override
	public boolean matches(Object obj)
	{
		if (obj instanceof UiElement)
		{
			UiElement uiElement = (UiElement) obj;
			UiElementAttributes uiElementAttributes = uiElement.getElementAttributes(false);
			return uiElementAttributes.isChecked();
		}
		return false;
	}

	@Override
	public void describeTo(Description description)
	{
		description.appendText("checked");
	}
}
