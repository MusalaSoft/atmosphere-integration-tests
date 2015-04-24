package com.musala.atmosphere.test.util.ondevicevalidator.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.ui.UiElementPropertiesContainer;

/**
 * Base Matcher class used for checking if a given UI element has the expected text.
 * 
 * @author yordan.petrov
 * 
 */
public class TextMatcher extends BaseMatcher<UiElement> {
    protected Object expected;

    public TextMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object obj) {
        if (obj instanceof UiElement && expected instanceof String) {
            String expectedText = (String) expected;

            UiElement uiElement = (UiElement) obj;
            UiElementPropertiesContainer uiElementProperties = uiElement.getProperties();
            String actualText = uiElementProperties.getText();

            return actualText.equals(expectedText);
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(expected.toString());
    }
}
