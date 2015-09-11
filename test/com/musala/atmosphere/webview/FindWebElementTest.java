package com.musala.atmosphere.webview;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startWebViewActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiWebElement;
import com.musala.atmosphere.client.WebView;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.webelement.exception.WebElementNotPresentException;
import com.musala.atmosphere.commons.webelement.selection.WebElementSelectionCriterion;

/**
 *
 * @author filareta.yordanova
 *
 */
public class FindWebElementTest extends BaseIntegrationTest {
    private static final String LINKS_PARENT_ID = "ul";

    private static final String LINK_CLASS = "link";

    private static final String CONTACTS_ID = "contacts";

    private static final String FOOTER_ID = "footer";

    private static final String EXISTING_ELEMENTS_XPATH_QUERY = "//input[@type='text']";

    private static final String ATTRIBUTE_MISSMATCH_ERROR_MESSAGE = "Actual web element attribute value does not match the expected one.";

    private static final String TEXT_MISSMATCH_ERROR_MESSAGE = "Actual web element text does not match the expected one.";

    private static final String NONEXISTENT_WEB_ELEMENT_TAG = "h2";

    private static final String EXISTING_WEB_ELEMENT_TAG = "input";

    private static final String EXISTING_WEB_ELEMENT_NAME = "gender";

    private static final String NONEXISTENT_WEB_ELEMENT_CLASS = "button";

    private static final String EXISTING_BUTTON_NAME = "submit";

    private static final String EXISTING_WEB_ELEMENT_ID = "info";

    private static Screen screen;

    private static WebView webView;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder deviceSelectorBuilder = new DeviceSelectorBuilder();
        DeviceSelector deviceSelector = deviceSelectorBuilder.deviceType(DeviceType.DEVICE_PREFERRED).build();
        initTestDevice(deviceSelector);
        setTestDevice(testDevice);
        screen = testDevice.getActiveScreen();

        startWebViewActivity();

        webView = screen.getWebView(VALIDATOR_APP_PACKAGE);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);

        releaseDevice();
    }

    @Test
    public void testFindElementWhenPresentByAttribute() {
        String expectedAttributeValue = "header";
        UiWebElement webElement = webView.findElement(WebElementSelectionCriterion.ID, EXISTING_WEB_ELEMENT_ID);
        assertEquals(ATTRIBUTE_MISSMATCH_ERROR_MESSAGE, expectedAttributeValue, webElement.getAttribute("class"));

        expectedAttributeValue = "btn";
        webElement = webView.findElement(WebElementSelectionCriterion.NAME, EXISTING_BUTTON_NAME);
        assertEquals(ATTRIBUTE_MISSMATCH_ERROR_MESSAGE, expectedAttributeValue, webElement.getAttribute("id"));
    }

    @Test(expected = WebElementNotPresentException.class)
    public void testFindElementWhenNotPresent() {
        webView.findElement(WebElementSelectionCriterion.CLASS, NONEXISTENT_WEB_ELEMENT_CLASS);
    }

    @Test
    public void testFindMultipleElementsByAttribute() {
        int expectedCount = 7;
        List<UiWebElement> elements = webView.findElements(WebElementSelectionCriterion.TAG, EXISTING_WEB_ELEMENT_TAG);
        assertEquals(ATTRIBUTE_MISSMATCH_ERROR_MESSAGE, expectedCount, elements.size());

        expectedCount = 2;
        elements = webView.findElements(WebElementSelectionCriterion.NAME, EXISTING_WEB_ELEMENT_NAME);
        assertEquals(ATTRIBUTE_MISSMATCH_ERROR_MESSAGE, expectedCount, elements.size());
    }

    @Test
    public void testFindMultipleElementsByXpath() {
        int expectedCount = 3;
        List<UiWebElement> elements = webView.findElements(WebElementSelectionCriterion.XPATH,
                                                           EXISTING_ELEMENTS_XPATH_QUERY);
        assertEquals(ATTRIBUTE_MISSMATCH_ERROR_MESSAGE, expectedCount, elements.size());
    }

    @Test
    public void testFindMultipleElementsWhenNotPresent() {
        List<UiWebElement> elements = webView.findElements(WebElementSelectionCriterion.TAG,
                                                           NONEXISTENT_WEB_ELEMENT_TAG);
        assertTrue("Unexpectedly elements matching the request were found on the current screen.", elements.isEmpty());
    }

    @Test
    public void testFindElementWhenMatchingElementsExistOnDifferentLevels() {
        String expectedName = "home";
        UiWebElement firstMatch = webView.findElement(WebElementSelectionCriterion.CLASS, LINK_CLASS);

        assertEquals(TEXT_MISSMATCH_ERROR_MESSAGE, expectedName, firstMatch.getAttribute("name"));
    }

    @Test
    public void testFindElementInWebElementWhenNotDirectChild() {
        String expectedName = "contacts";
        UiWebElement parentElement = webView.findElement(WebElementSelectionCriterion.ID, FOOTER_ID);
        UiWebElement descendant = parentElement.findElement(WebElementSelectionCriterion.ID, CONTACTS_ID);

        assertEquals(TEXT_MISSMATCH_ERROR_MESSAGE, expectedName, descendant.getAttribute("name"));
    }

    @Test
    public void testFindElementInWebElementWhenDirectChild() {
        String expectedAttributeValue = "ul-info";
        UiWebElement parentElement = webView.findElement(WebElementSelectionCriterion.ID, FOOTER_ID);
        UiWebElement child = parentElement.findElement(WebElementSelectionCriterion.TAG, LINKS_PARENT_ID);

        assertEquals(ATTRIBUTE_MISSMATCH_ERROR_MESSAGE, expectedAttributeValue, child.getAttribute("class"));
    }
}
