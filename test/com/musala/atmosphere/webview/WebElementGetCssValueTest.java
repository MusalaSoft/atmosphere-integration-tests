package com.musala.atmosphere.webview;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 *
 * @author yavor.stankov
 *
 */
public class WebElementGetCssValueTest extends BaseWebViewIntegrationTest {
    private static final String WEB_ELEMENT_CLASS_NAME = "absolute";

    private static final String ERROR_MESSAGE = "The received attribute value is not the same as expected.";

    private static final String EXPECTED_POSITION_VALUE = "absolute";

    private static final String EXPECTED_TOP_VALUE = "80px";

    private static final String EXPECTED_LEFT_VALUE = "20px";

    private static final String EXPECTED_WIDTH_VALUE = "230px";

    private static final String EXPECTED_HEIGHT_VALUE = "30px";

    private static final String EXPECTED_BORDER_VALUE = "1px solid rgb(0, 0, 0)";

    @Test
    public void testGetCssValueTop() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_TOP_VALUE, element.getCssValue("top"));
    }

    @Test
    public void testGetCssValuePosition() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_POSITION_VALUE, element.getCssValue("position"));
    }

    @Test
    public void testGetCssValueLeft() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_LEFT_VALUE, element.getCssValue("left"));
    }

    @Test
    public void testGetCssValueWidth() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_WIDTH_VALUE, element.getCssValue("width"));
    }

    @Test
    public void testGetCssValueHeight() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_HEIGHT_VALUE, element.getCssValue("height"));
    }

    @Test
    public void testGetCssValueBorder() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS_NAME);
        assertEquals(ERROR_MESSAGE, EXPECTED_BORDER_VALUE, element.getCssValue("border"));
    }
}
