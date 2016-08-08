package com.musala.atmosphere.test.util.ondevicevalidator;

import static com.musala.atmosphere.test.util.ondevicevalidator.matchers.OnDeviceValidatorUIMatchersFactory.checked;
import static com.musala.atmosphere.test.util.ondevicevalidator.matchers.OnDeviceValidatorUIMatchersFactory.enabled;
import static com.musala.atmosphere.test.util.ondevicevalidator.matchers.OnDeviceValidatorUIMatchersFactory.focused;
import static com.musala.atmosphere.test.util.ondevicevalidator.matchers.OnDeviceValidatorUIMatchersFactory.hasBounds;
import static com.musala.atmosphere.test.util.ondevicevalidator.matchers.OnDeviceValidatorUIMatchersFactory.hasText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.geometry.Bounds;

/**
 * Class containing core assert methods that can be used for constructing more complex ones.
 * 
 * @author yordan.petrov
 * 
 */
public class UIAssert {
    /**
     * Asserts that a given UI element is focused.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param uiElement
     *        - the UI element used in the assertion.
     */
    public static void assertIsFocused(String message, UiElement uiElement) {
        assertThat(message, uiElement, is(focused()));
    }

    /**
     * Asserts that a given UI element is not focused.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param uiElement
     *        - the UI element used in the assertion.
     */
    public static void assertNotFocused(String message, UiElement uiElement) {
        assertThat(message, uiElement, not(focused()));
    }

    /**
     * Asserts that a given UI element is checked.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param uiElement
     *        - the UI element used in the assertion.
     */
    public static void assertIsChecked(String message, UiElement uiElement) {
        assertThat(message, uiElement, is(checked()));
    }

    /**
     * Asserts that a given UI element is not checked.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param uiElement
     *        - the UI element used in the assertion.
     */
    public static void assertNotChecked(String message, UiElement uiElement) {
        assertThat(message, uiElement, not(checked()));
    }

    /**
     * Asserts that a given UI element is enabled.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param uiElement
     *        - the UI element used in the assertion.
     */
    public static void assertIsEnabled(String message, UiElement uiElement) {
        assertThat(message, uiElement, is(enabled()));
    }

    /**
     * Asserts that a given UI element is not enabled.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param uiElement
     *        - the UI element used in the assertion.
     */
    public static void assertNotEnabled(String message, UiElement uiElement) {
        assertThat(message, uiElement, not(enabled()));
    }

    /**
     * Asserts that a given UI element is selected.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param uiElement
     *        - the UI element used in the assertion.
     */
    public static void assertIsSelected(String message, UiElement uiElement) {
        assertThat(message, uiElement, is(focused()));
    }

    /**
     * Asserts that a given UI element is not selected.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param uiElement
     *        - the UI element used in the assertion.
     */
    public static void assertNotSelected(String message, UiElement uiElement) {
        assertThat(message, uiElement, not(focused()));
    }

    /**
     * Asserts that a given UI element has the expected text.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param uiElement
     *        - the UI element used in the assertion.
     * @param expected
     *        - the expected text.
     */
    public static void assertText(String message, UiElement uiElement, String expected) {
        assertThat(message, uiElement, hasText(expected));
    }

    /**
     * Asserts that a given UI element has the expected bounds.
     * 
     * @param message
     *        - message to be displayed if assertion fails.
     * @param uiElement
     *        - the UI element used in the assertion.
     * @param expected
     *        - expected bounds.
     */
    public static void assertBounds(String message, UiElement uiElement, Bounds expected) {
        assertThat(message, uiElement, hasBounds(expected));
    }
}
