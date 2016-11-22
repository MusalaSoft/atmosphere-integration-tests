package com.musala.atmosphere.webview;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 *
 * @author denis.bialev
 *
 */
public class GetWebElementTextTest extends BaseWebViewIntegrationTest {
    private static final String WEB_ELEMENT_ID = "info";

    private static final String BUTTON_WEB_ELEMENT_ID = "btn";

    private static final String SUB_TEXT_WEB_ELEMENT_ID = "subtext";

    private static final String NO_TEXT_WEB_ELEMENT_ID = "fname";

    private static final String DIFFERENT_TEXT_ASSERT_MESSAGE = "The received text from the element is defferent than expected.";

    @Test
    public void testGetWebElementText() throws Exception {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, WEB_ELEMENT_ID);
        String expectedText = "Personal info";
        assertTrue(DIFFERENT_TEXT_ASSERT_MESSAGE, webElement.getText().equals(expectedText));
    }

    @Test
    public void testGetWebElementButtonText() throws Exception {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, BUTTON_WEB_ELEMENT_ID);
        String expectedText = "Submit";
        assertTrue(DIFFERENT_TEXT_ASSERT_MESSAGE, webElement.getText().equals(expectedText));
    }

    @Test
    public void testGetTextWithSubtext() throws Exception {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, SUB_TEXT_WEB_ELEMENT_ID);
        String expectedText = "This text contains subscript text.";
        assertTrue(DIFFERENT_TEXT_ASSERT_MESSAGE, webElement.getText().equals(expectedText));
    }

    @Test
    public void testGetTextNoText() throws Exception {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, NO_TEXT_WEB_ELEMENT_ID);
        String expectedText = "";
        assertTrue(DIFFERENT_TEXT_ASSERT_MESSAGE, webElement.getText().equals(expectedText));
    }
}
