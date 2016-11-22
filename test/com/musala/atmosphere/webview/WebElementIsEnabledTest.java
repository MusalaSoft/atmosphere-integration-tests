package com.musala.atmosphere.webview;

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
public class WebElementIsEnabledTest extends BaseWebViewIntegrationTest {
    private static final String ENABLED_ELEMENT_ID = "fname";

    private static final String DISABLED_ELEMENT_ID = "lname";

    @Test
    public void testIsWebElementEnabled() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, ENABLED_ELEMENT_ID);
        assertTrue("The enabled element was registered as disabled element.", webElement.isEnabled());
    }

    @Test
    public void testIsWebElementDisabled() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, DISABLED_ELEMENT_ID);
        assertFalse("The disabled element was registered as enabled element.", webElement.isEnabled());
    }
}
