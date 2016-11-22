package com.musala.atmosphere.webview;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 *
 * @author denis.bialev
 *
 */
public class WebElementIsDisplayedTest extends BaseWebViewIntegrationTest {
    private static final String DISPLAYED_ELEMENT_ID = "fname";

    private static final String HIDDEN_ELEMENT_ID = "hiddenCountry";

    private static final String NEGATIVE_BOUNDS_ELEMENT_ID = "negativeBounds";

    private static final String HIDDEN_ELEMENT_IS_DISPLAYED_FAIL_MESSAGE = "The hidden element was registered as displayed.";

    private static final String EXPECTED_CSS_PROPERTY_FAIL_MESSAGE = "The found element does not contain the right CSS property.";

    private static final String DISPLAY_ELEMENT_ID = "displayText";

    private static final String VISIBIL_ELEMENT_ID = "visibilityText";

    private static final String ZERO_OPACITY_ELEMENT_CLASS = "container-div";

    @Test
    public void testIsVisibleWebElementDisplayed() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, DISPLAYED_ELEMENT_ID);
        assertTrue("The displayed element was registered as hidden element.", webElement.isDisplayed());
    }

    @Test
    public void testIsHiddenWebElementDisplayed() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, HIDDEN_ELEMENT_ID);
        assertFalse(HIDDEN_ELEMENT_IS_DISPLAYED_FAIL_MESSAGE, webElement.isDisplayed());
    }

    @Test
    public void testIsWebElementWithNegativeBoundsDisplayed() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, NEGATIVE_BOUNDS_ELEMENT_ID);
        assertFalse(HIDDEN_ELEMENT_IS_DISPLAYED_FAIL_MESSAGE, webElement.isDisplayed());
    }

    @Test
    public void testIsWebElementDisplayedPropertyDisplay() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, DISPLAY_ELEMENT_ID);
        String expectedValue = "none";
        String expectedProperty = "display";
        assertEquals(EXPECTED_CSS_PROPERTY_FAIL_MESSAGE, expectedValue, webElement.getCssValue(expectedProperty));
        assertFalse(HIDDEN_ELEMENT_IS_DISPLAYED_FAIL_MESSAGE, webElement.isDisplayed());
    }

    @Test
    public void testIsWebElementDisplayedPropertyVisibility() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, VISIBIL_ELEMENT_ID);
        String expectedValue = "hidden";
        String expectedProperty = "visibility";
        assertEquals(EXPECTED_CSS_PROPERTY_FAIL_MESSAGE, expectedValue, webElement.getCssValue(expectedProperty));
        assertFalse(HIDDEN_ELEMENT_IS_DISPLAYED_FAIL_MESSAGE, webElement.isDisplayed());
    }

    @Test
    public void testIsWebElementWithZeroOpacityDisplayed() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.CLASS, ZERO_OPACITY_ELEMENT_CLASS);
        String expectedValue = "0";
        String expectedProperty = "opacity";
        assertEquals(EXPECTED_CSS_PROPERTY_FAIL_MESSAGE, expectedValue, webElement.getCssValue(expectedProperty));
        assertFalse(HIDDEN_ELEMENT_IS_DISPLAYED_FAIL_MESSAGE, webElement.isDisplayed());
    }
}
