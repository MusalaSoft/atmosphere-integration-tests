package com.musala.atmosphere.webview;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 *
 * @author yavor.stankov
 *
 */
public class WebElementInputTextTest extends BaseWebViewIntegrationTest {
    private static final String INPUT_TEXT_ELEMENT_ID = "fname";

    private static final String TEXT_TO_INPUT = "Atmosphere";

    @Test
    public void testWebElementInputText() {
        UiWebElement inputTextBox = webView.findElement(WebElementSelectionCriterion.ID, INPUT_TEXT_ELEMENT_ID);

        assertTrue("Failed to input text in the selected web element.", inputTextBox.inputText(TEXT_TO_INPUT));
    }
}
