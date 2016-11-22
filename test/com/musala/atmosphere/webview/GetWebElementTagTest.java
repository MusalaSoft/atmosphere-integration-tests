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
public class GetWebElementTagTest extends BaseWebViewIntegrationTest {
    private static final String WEB_ELEMENT_ID = "fname";

    @Test
    public void testGetWebElementTag() {
        String expectedTag = "input";
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, WEB_ELEMENT_ID);
        assertTrue("The given tag was different than expected.", webElement.getTagName().equals(expectedTag));
    }
}
