package com.musala.atmosphere.webview;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 *
 * @author konstantin.ivanov
 *
 */
public class ClearWebElementTextTest extends BaseWebViewIntegrationTest {

    private static final String WEB_ELEMENT_ID = "form";

    @Test
    public void testClearText() {
        UiWebElement textField = webView.findElement(WebElementSelectionCriterion.ID, WEB_ELEMENT_ID);
        boolean isCleared = textField.clearText();
        assertTrue("The text was not cleared", isCleared);
    }
}
