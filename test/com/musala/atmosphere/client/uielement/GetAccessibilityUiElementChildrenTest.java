package com.musala.atmosphere.client.uielement;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startUiElementChildrenActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.ui.UiElementPropertiesContainer;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

/**
 * 
 * @author filareta.yordanova
 *
 */
public class GetAccessibilityUiElementChildrenTest extends BaseIntegrationTest {
    private static final String HEADER_VIEW_BUTTON_TEXT = "Header view";

    private static final String UNEXPECTED_ELEMENT_FOUND_MESSAGE = "Unexpectedly children matching the given attributes were found.";

    private static final String ELEMENT_PROPERTY_MISSMATCH_MESSAGE = "The expected children class name is different from the received.";

    private static final String ANDROID_WIDGET_LIST_VIEW = "android.widget.ListView";

    private static final String ANDROID_WIDGET_IMAGE_BUTTON = "android.widget.ImageButton";

    private static final String ANDROID_WIDGET_RELATIVE_LAYOUT = "android.widget.RelativeLayout";

    private static final String ANDROID_WIDGET_BUTTON = "android.widget.Button";

    private static final String MISSMATCH_CHILDREN_COUNT_ERROR_MESSAGE = "Expected children elements count is different from the actual one.";

    private static Screen screen;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder deviceSelectorBuilder = new DeviceSelectorBuilder();
        DeviceSelector deviceSelector = deviceSelectorBuilder.deviceType(DeviceType.DEVICE_PREFERRED).build();
        initTestDevice(deviceSelector);
        setTestDevice(testDevice);
        screen = testDevice.getActiveScreen();

        startUiElementChildrenActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);

        releaseDevice();
    }

    @Test
    public void testGetChildrenWhenNoMatchesFound() throws Exception {
        UiElementSelector parentSelector = new UiElementSelector();
        parentSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_RELATIVE_LAYOUT);
        UiElement parent = screen.getElement(parentSelector);

        UiElementSelector childSelector = new UiElementSelector();
        childSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_IMAGE_BUTTON);
        List<UiElement> children = parent.getChildren(childSelector);

        assertTrue(UNEXPECTED_ELEMENT_FOUND_MESSAGE, children.isEmpty());
    }

    @Test
    public void testGetChildrenThatMatchSelectorAttributes() throws Exception {
        int expectedChildrenCount = 3;
        UiElementSelector parentSelector = new UiElementSelector();
        parentSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_RELATIVE_LAYOUT);
        UiElement parent = screen.getElement(parentSelector);

        UiElementSelector childSelector = new UiElementSelector();
        childSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_BUTTON);
        List<UiElement> children = parent.getChildren(childSelector);

        assertEquals(MISSMATCH_CHILDREN_COUNT_ERROR_MESSAGE, expectedChildrenCount, children.size());

        for (UiElement child : children) {
            assertEquals(ELEMENT_PROPERTY_MISSMATCH_MESSAGE,
                         ANDROID_WIDGET_BUTTON,
                         child.getProperties().getClassName());
        }
    }

    @Test
    public void testGetDirectChildrenWhenChildrenAreLeafNodes() throws Exception {
        int expectedChildrenCount = 7;
        UiElementSelector parentSelector = new UiElementSelector();
        parentSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_LIST_VIEW);
        UiElement parent = screen.getElement(parentSelector);

        List<UiElement> children = parent.getDirectChildren();

        assertEquals(MISSMATCH_CHILDREN_COUNT_ERROR_MESSAGE, expectedChildrenCount, children.size());
    }

    @Test
    public void testGetDirectChildrenWhenChildrenAreNotLeafNodes() throws Exception {
        int expectedChildrenCount = 2;
        UiElementSelector parentSelector = new UiElementSelector();
        parentSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_RELATIVE_LAYOUT);
        UiElement parent = screen.getElement(parentSelector);

        List<UiElement> children = parent.getDirectChildren();

        assertEquals(MISSMATCH_CHILDREN_COUNT_ERROR_MESSAGE, expectedChildrenCount, children.size());
    }

    @Test
    public void testGetChildrenWithNoSpecificSelectionAttributesSet() throws Exception {
        int expectedChildrenCount = 9;
        UiElementSelector parentSelector = new UiElementSelector();
        parentSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_RELATIVE_LAYOUT);
        UiElement parent = screen.getElement(parentSelector);

        List<UiElement> children = parent.getChildren(new UiElementSelector());

        assertEquals(MISSMATCH_CHILDREN_COUNT_ERROR_MESSAGE, expectedChildrenCount, children.size());
    }

    @Test
    public void testGetDirectChildrenThatMatchSelectorAttributes() throws Exception {
        int expectedChildrenCount = 1;
        UiElementSelector parentSelector = new UiElementSelector();
        parentSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_RELATIVE_LAYOUT);
        UiElement parent = screen.getElement(parentSelector);

        UiElementSelector childSelector = new UiElementSelector();
        childSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_BUTTON);
        List<UiElement> children = parent.getDirectChildren(childSelector);

        assertEquals(MISSMATCH_CHILDREN_COUNT_ERROR_MESSAGE, expectedChildrenCount, children.size());

        UiElementPropertiesContainer childProperties = children.get(0).getProperties();
        assertEquals(ELEMENT_PROPERTY_MISSMATCH_MESSAGE, ANDROID_WIDGET_BUTTON, childProperties.getClassName());
    }

    @Test
    public void testGetDirectChildrenWhenNoChildMatchesSelectionAttributes() throws Exception {
        UiElementSelector parentSelector = new UiElementSelector();
        parentSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_RELATIVE_LAYOUT);
        UiElement parent = screen.getElement(parentSelector);

        UiElementSelector childSelector = new UiElementSelector();
        childSelector.addSelectionAttribute(CssAttribute.CLASS_NAME, ANDROID_WIDGET_BUTTON);
        childSelector.addSelectionAttribute(CssAttribute.TEXT, HEADER_VIEW_BUTTON_TEXT);
        List<UiElement> children = parent.getDirectChildren(childSelector);

        assertTrue(UNEXPECTED_ELEMENT_FOUND_MESSAGE, children.isEmpty());
    }
}
