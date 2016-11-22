package com.musala.atmosphere.webview;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.commons.geometry.Point;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 *
 * @author denis.bialev
 *
 */
public class GetWebElementRelativePositionTest extends BaseWebViewIntegrationTest {
    private static final String WEB_ELEMENT_CLASS = "absolute";

    @Test
    public void getWebElementRelativePosition() {
        UiWebElement element = webView.findElement(WebElementSelectionCriterion.CLASS, WEB_ELEMENT_CLASS);
        Point expectedPosition = new Point(20, 80);
        assertEquals("The given position is different than expected.", expectedPosition, element.getRelativePosition());
    }
}
