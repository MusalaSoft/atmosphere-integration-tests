package com.musala.atmosphere.test.util.ondevicevalidator.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.geometry.Bounds;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;

/**
 * Base Matcher class used for checking if a given UI element has expected bounds.
 * 
 * @author yordan.petrov
 * 
 */
public class BoundsMatcher extends BaseMatcher<UiElement> {
    protected Object expected;

    public BoundsMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object obj) {
        if (obj instanceof UiElement && expected instanceof Bounds) {
            Bounds expectedBounds = (Bounds) obj;

            UiElement uiElement = (UiElement) obj;
            UiElementSelector uiElementSelector = uiElement.getElementSelector();
            Bounds actualBounds = uiElementSelector.getBoundsValue(CssAttribute.BOUNDS);

            return actualBounds.equals(expectedBounds);
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(expected.toString());
    }
}
