package com.musala.atmosphere.webview;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 *
 * @author yavor.stankov
 *
 */
public class TapWebElementTest extends BaseWebViewIntegrationTest {
    private static final String ATTRIBUTE_VALUE_MISSMATCH_ERROR_MESSAGE = "Expected attribute value does not match the received one.";

    private static final String TAP_FAILED_MESSAGE = "Failed to tap the web element.";

    private static final String EXISTING_WEB_ELEMENT_TAG = "button";

    private static final String WEB_ELEMENT_ID = "btn";

    private static final String DYNAMIC_ATTRIBUTE_NAME = "testattribute";

    private static final String SUBMIT_BUTTON_DYNAMIC_ATTRIBUTE_VALUE = "tappedSubmit";

    private static final String CANCEL_BUTTON_DYNAMIC_ATTRIBUTE_VALUE = "tappedCancel";

    @Test
    public void testTapWebElement() {
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, WEB_ELEMENT_ID);
        webElement.tap();

        String dynamicAtributeValue = (String) webElement.getAttribute(DYNAMIC_ATTRIBUTE_NAME);

        assertEquals(TAP_FAILED_MESSAGE, SUBMIT_BUTTON_DYNAMIC_ATTRIBUTE_VALUE, dynamicAtributeValue);
    }

    @Test
    public void testTapWebElementInList() {
        String[] expectedResult = {"submit", "cancel", "getSizeTest", "create"};
        List<UiWebElement> webElements = webView.findElements(WebElementSelectionCriterion.TAG,
                                                              EXISTING_WEB_ELEMENT_TAG);

        assertEquals("Number of expected web elements found does not match the actual one.",
                     expectedResult.length,
                     webElements.size());
        int index = 0;
        for (UiWebElement webElement : webElements) {
            assertEquals(ATTRIBUTE_VALUE_MISSMATCH_ERROR_MESSAGE,
                         expectedResult[index],
                         webElement.getAttribute("name"));
            index++;
        }

        UiWebElement closeButton = webElements.get(1);
        closeButton.tap();

        String dynamicAtributeValue = (String) closeButton.getAttribute(DYNAMIC_ATTRIBUTE_NAME);

        assertEquals(TAP_FAILED_MESSAGE, CANCEL_BUTTON_DYNAMIC_ATTRIBUTE_VALUE, dynamicAtributeValue);
        assertEquals(ATTRIBUTE_VALUE_MISSMATCH_ERROR_MESSAGE, expectedResult[1], closeButton.getAttribute("name"));
    }
}
