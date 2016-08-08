package com.musala.atmosphere.test.util.ondevicevalidator.matchers;

import org.hamcrest.Matcher;

import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.geometry.Bounds;

public class OnDeviceValidatorUIMatchersFactory {

    public static Matcher<UiElement> focused() {
        return new FocusedMatcher();
    }

    public static Matcher<UiElement> checked() {
        return new CheckedMatcher();
    }

    public static Matcher<UiElement> enabled() {
        return new EnabledMatcher();
    }

    public static Matcher<UiElement> selected() {
        return new SelectedMatcher();
    }

    public static Matcher<UiElement> hasBounds(Bounds expected) {
        return new BoundsMatcher(expected);
    }

    public static Matcher<UiElement> hasText(String expected) {
        return new TextMatcher(expected);
    }
}
