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
public class WebElementIsSelectedTest extends BaseWebViewIntegrationTest {

    private static final String SELECTED_WEB_ELEMENT_ID = "male";

    private static final String NOT_SELECTED_WEB_ELEMENT_ID = "female";

    private static final String NOT_SELECTABLE_ELEMENT_ID = "fname";

    @Test
    public void testIsWebElementSelected() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, SELECTED_WEB_ELEMENT_ID);
        assertTrue("The selected element was registered as not selected element.", webElement.isSelected());
    }

    @Test
    public void testIsWebElementNotSelected() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, NOT_SELECTED_WEB_ELEMENT_ID);
        assertFalse("The not selected element was registered as selected element.", webElement.isSelected());
    }

    @Test
    public void testIsNotSelectableWebElementSelected() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, NOT_SELECTABLE_ELEMENT_ID);
        assertFalse("The not selectable element was registered as selected element.", webElement.isSelected());
    }
}
