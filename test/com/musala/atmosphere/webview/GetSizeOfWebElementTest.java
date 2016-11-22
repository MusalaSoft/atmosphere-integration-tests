package com.musala.atmosphere.webview;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.commons.util.Pair;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 *
 * @author konstantin.ivanov
 *
 */
public class GetSizeOfWebElementTest extends BaseWebViewIntegrationTest {

    private static final String WEB_ELEMENT_ID = "btnGetSize";

    private static final Pair<Integer, Integer> EXPECTED_WEB_ELEMENT_SIZE = new Pair<Integer, Integer>(90, 50);

    @Test
    public void testGetSizeOfWebElement() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, WEB_ELEMENT_ID);
        Pair<Integer, Integer> size = webElement.getSize();

        assertTrue("The size of the web element is different from the expected",
                   size.equals(EXPECTED_WEB_ELEMENT_SIZE));

    }
}
