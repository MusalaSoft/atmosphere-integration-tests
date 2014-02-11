package com.musala.atmosphere.test.util.ondevicevalidator.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;

/**
 * Base Matcher class used for checking if a given UI element has the expected text.
 *
 * @author yordan.petrov
 *
 */
public class TextMatcher extends BaseMatcher<UiElement>
{
	protected Object expected;

	public TextMatcher(Object expected)
	{
		this.expected = expected;
	}

	@Override
	public boolean matches(Object obj)
	{
		if (obj instanceof UiElement && expected instanceof String)
		{
			String expectedText = (String) expected;

			UiElement uiElement = (UiElement) obj;
			UiElementSelector uiElementSelector = uiElement.getElementSelector(false);
			String actualText = uiElementSelector.getStringValue(CssAttribute.TEXT);

			return actualText.equals(expectedText);
		}
		return false;
	}

	@Override
	public void describeTo(Description description)
	{
		description.appendText(expected.toString());
	}
}
