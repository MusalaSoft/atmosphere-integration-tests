package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScrollBackward;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScrollForward;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScrollToBeginning;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.assertScrollToEnd;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getElementByContentDescriptor;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.getScrollableView;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startHorizontalScrollActivity;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.ScrollableView;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.exceptions.ActivityStartingException;
import com.musala.atmosphere.client.exceptions.UiElementFetchingException;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.test.util.ondevicevalidator.ContentDescriptor;

public class HorizontalScrollTest extends BaseIntegrationTest {
    private static final String SCROLL_TO_TEXT = "Scroll here";

    @BeforeClass
    public static void setUp() throws UiElementFetchingException, InterruptedException, ActivityStartingException {
        initTestDevice(new DeviceParameters());
        setTestDevice(testDevice);

        startHorizontalScrollActivity();
    }

    @AfterClass
    public static void tearDown() throws UiElementFetchingException {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        releaseDevice();
    }

    @Test
    public void testHorizontalScrollToEnd() throws UiElementFetchingException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.HORIZONTAL_SCROLL_VIEW_VALIDATOR.toString());

        UiElement scrollToBeginningButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_BEGINNING_BUTTON.toString());
        scrollToBeginningButton.tap();

        assertScrollToBeginning("Failure detected, coul not scroll to beginning.");

        scrollableView.setAsHorizontalScrollableView();
        Boolean scrollTest = scrollableView.scrollToEnd(6);
        String failureMessage = "Failure detected, scroll to end returned false.";

        assertTrue(failureMessage, scrollTest);
        assertScrollToEnd(failureMessage);
    }

    @Test
    public void testHorizontalScrollToBeginning() throws UiElementFetchingException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.HORIZONTAL_SCROLL_VIEW_VALIDATOR.toString());

        UiElement scrollToEndButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_END_BUTTON.toString());
        scrollToEndButton.tap();

        assertScrollToEnd("Failure detected, could not scroll to end.");

        scrollableView.setAsHorizontalScrollableView();
        Boolean scrollTest = scrollableView.scrollToBeginning(6);

        String failureMessage = "Failure detected, scroll to beginning returned false.";

        assertTrue(failureMessage, scrollTest);
        assertScrollToBeginning(failureMessage);
    }

    @Test
    public void testScrollBackwardHorizontally() throws UiElementFetchingException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.HORIZONTAL_SCROLL_VIEW_VALIDATOR.toString());

        UiElement scrollToEndButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_END_BUTTON.toString());
        scrollToEndButton.tap();

        assertScrollToEnd("Failure detected, could not scroll to end.");

        scrollableView.setAsHorizontalScrollableView();
        Boolean scrollTest = scrollableView.scrollBackward();

        String failureMessage = "Failure detected, scroll backward returned false.";

        assertTrue(failureMessage, scrollTest);
        assertScrollBackward(failureMessage);
    }

    @Test
    public void testScrollForwardHorizontally() throws UiElementFetchingException {
        ScrollableView scrollableView = getScrollableView(ContentDescriptor.HORIZONTAL_SCROLL_VIEW_VALIDATOR.toString());
        UiElement scrollToBeginningButton = getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_BEGINNING_BUTTON.toString());
        scrollToBeginningButton.tap();

        assertScrollToBeginning("Failure detected, could not scroll to beginning.");

        scrollableView.setAsHorizontalScrollableView();
        Boolean scrollTest = scrollableView.scrollForward();

        String failureMessage = "Failure detected, scroll forward returned false.";

        assertTrue(failureMessage, scrollTest);
        assertScrollForward(failureMessage);
    }

    // TODO Refactor the test if needed when this functionality has more adequate behavior

    // @Test
    // public void testScrollIntoView() throws UiElementFetchingException {
    // ScrollableView scrollableView = getScrollableView(ContentDescriptor.HORIZONTAL_SCROLL_VIEW_VALIDATOR.toString());
    // UiElement scrollToBeginningButton =
    // getElementByContentDescriptor(ContentDescriptor.SCROLL_TO_BEGINNING_BUTTON.toString());
    // scrollToBeginningButton.tap();
    //
    // assertScrollToBeginning("Failure detected, could not scroll to beginning.");
    //
    // UiElementSelector innerViewSelector = new UiElementSelector();
    // innerViewSelector.addSelectionAttribute(CssAttribute.TEXT, SCROLL_TO_TEXT);
    //
    // assertUIElementNotOnScreen("Element is already visible", innerViewSelector);
    //
    // scrollableView.setAsHorizontalScrollableView();
    // Boolean scrollTest = scrollableView.scrollIntoView(innerViewSelector);
    //
    // String failureMessage = "Failure detected, scroll into view returned false.";
    //
    // assertTrue(failureMessage, scrollTest);
    // assertUIElementOnScreen(failureMessage, innerViewSelector);
    // }

}
