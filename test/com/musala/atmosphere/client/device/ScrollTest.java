package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScrollBackward;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScrollForward;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScrollToBeginning;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScrollToEnd;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertUIElementNotOnScreen;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertUIElementOnScreen;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getScrollableView;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startScrollActivity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.ScrollableView;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.InvalidCssQueryException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.client.uiutils.CssAttribute;
import com.musala.atmosphere.client.uiutils.UiElementSelector;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class ScrollTest extends BaseIntegrationTest {
    private static final String SCROLL_TO_TEXT = "Scroll here";

    private static final String TEXT_TO_TAP = "Neptune";

    private static final Integer MAX_SWIPES = 50;

    @BeforeClass
    public static void setUp()
        throws UiElementFetchingException,
            InterruptedException,
            ActivityStartingException,
            XPathExpressionException,
            InvalidCssQueryException {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);

        startScrollActivity();
    }

    @AfterClass
    public static void tearDown() throws UiElementFetchingException {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testScrollToEnd() throws UiElementFetchingException, XPathExpressionException, InvalidCssQueryException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.SCROLL_VIEW_VALIDATOR.toString());

        UiElement scrollToBeginningButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_BEGINNING_BUTTON.toString());
        scrollToBeginningButton.tap();

        assertScrollToBeginning("Failure detected, could not scroll to beginning.");

        Boolean scrollTest = scrollableView.scrollToEnd(5);
        String failureMessage = "Failure detected, scroll to end returned false.";

        assertTrue(failureMessage, scrollTest);
        assertScrollToEnd(failureMessage);
    }

    @Test
    public void testScrollToBeginning()
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.SCROLL_VIEW_VALIDATOR.toString());

        UiElement scrollToEndButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_END_BUTTON.toString());
        scrollToEndButton.tap();

        assertScrollToEnd("Failure detected, could not scroll to end.");

        Boolean scrollTest = scrollableView.scrollToBeginning(5);

        String failureMessage = "Failure detected, scroll to beginning returned false.";

        assertTrue(failureMessage, scrollTest);
        assertScrollToBeginning(failureMessage);
    }

    @Test
    public void testScrollBackward()
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.SCROLL_VIEW_VALIDATOR.toString());

        UiElement scrollToEndButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_END_BUTTON.toString());
        scrollToEndButton.tap();

        assertScrollToEnd("Failure detected, could not scroll to end.");

        Boolean scrollTest = scrollableView.scrollBackward();

        String failureMessage = "Failure detected, scroll backward returned false.";

        assertTrue(failureMessage, scrollTest);
        assertScrollBackward(failureMessage);
    }

    @Test
    public void testScrollForward()
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.SCROLL_VIEW_VALIDATOR.toString());
        UiElement scrollToBeginningButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_BEGINNING_BUTTON.toString());
        scrollToBeginningButton.tap();

        assertScrollToBeginning("Failure detected, could not scroll to beginning.");

        Boolean scrollTest = scrollableView.scrollForward();

        String failureMessage = "Failure detected, scroll forward returned false.";

        assertTrue(failureMessage, scrollTest);
        assertScrollForward(failureMessage);
    }

    @Test
    public void testScrollToElementBySelector()
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException,
            ParserConfigurationException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.SCROLL_VIEW_VALIDATOR.toString());
        UiElement scrollToEndButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_END_BUTTON.toString());
        scrollToEndButton.tap();

        assertScrollToEnd("Could not scroll to end.");

        UiElementSelector innerViewSelector = new UiElementSelector();
        innerViewSelector.addSelectionAttribute(CssAttribute.TEXT, SCROLL_TO_TEXT);

        assertUIElementNotOnScreen("Element is already visible", innerViewSelector);
        Boolean scrollTest = scrollableView.scrollToElementBySelector(MAX_SWIPES, innerViewSelector);

        assertTrue("Tap element to selector with scrolling returned false.", scrollTest);
        assertUIElementOnScreen("The UiElement is not on screen", innerViewSelector);
    }

    @Test
    public void testTapElementBySelectorWithScrolling()
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException,
            ParserConfigurationException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.SCROLL_VIEW_VALIDATOR.toString());
        UiElement scrollToEndButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_END_BUTTON.toString());
        scrollToEndButton.tap();

        assertScrollToEnd("Could not scroll to end.");

        UiElementSelector innerViewSelector = new UiElementSelector();
        innerViewSelector.addSelectionAttribute(CssAttribute.TEXT, SCROLL_TO_TEXT);

        assertUIElementNotOnScreen("Element is already visible", innerViewSelector);
        Boolean scrollTest = scrollableView.tapElementBySelectorWithScrolling(MAX_SWIPES, innerViewSelector);

        assertTrue("Tap element to selector with scrolling returned false.", scrollTest);
        assertUIElementOnScreen("The UiElement is not on screen", innerViewSelector);
    }

    @Test
    public void testTapElementBySelectorWithoutScrolling()
        throws UiElementFetchingException,
            XPathExpressionException,
            InvalidCssQueryException,
            ParserConfigurationException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.SCROLL_VIEW_VALIDATOR.toString());
        UiElement scrollToBeginningButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_BEGINNING_BUTTON.toString());
        scrollToBeginningButton.tap();

        assertScrollToBeginning("Could not scroll to beginning.");

        UiElementSelector innerViewSelector = new UiElementSelector();
        innerViewSelector.addSelectionAttribute(CssAttribute.TEXT, TEXT_TO_TAP);

        assertUIElementOnScreen("Element is not visible", innerViewSelector);
        Boolean scrollTest = scrollableView.tapElementBySelectorWithoutScrolling(innerViewSelector);

        assertTrue("Tap element by selector without scrolling returned false.", scrollTest);

        innerViewSelector = new UiElementSelector();
        innerViewSelector.addSelectionAttribute(CssAttribute.TEXT, SCROLL_TO_TEXT);
        assertUIElementNotOnScreen("Element is visible", innerViewSelector);
        scrollTest = scrollableView.tapElementBySelectorWithoutScrolling(innerViewSelector);

        assertFalse("Tap element by selector without scrolling returned true.", scrollTest);
    }
}
