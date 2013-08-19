package com.musala.atmosphere.test.util.ondevicevalidator.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.UiElementAttributes;

/**
 * Base Matcher class used for checking if a given UI element us selected.
 * 
 * @author yordan.petrov
 * 
 */
public class SelectedMatcher extends BaseMatcher<UiElement>
{
	@Override
	public boolean matches(Object obj)
	{
		if (obj instanceof UiElement)
		{
			UiElement uiElement = (UiElement) obj;
			UiElementAttributes uiElementAttributes = uiElement.getElementAttributes();
			return uiElementAttributes.isSelected();
		}
		return false;
	}

	@Override
	public void describeTo(Description description)
	{
		description.appendText("selected");
	}
}
