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

/**
 * 
 * @author denis.bialev
 *
 */
public class GetElementChildrenByXPathTest extends BaseIntegrationTest {
    private static final String UNEXPECTED_ELEMENT_FOUND_MESSAGE = "Unexpectedly children matching the given attributes were found.";

    private static final String ELEMENT_PROPERTY_MISSMATCH_MESSAGE = "The expected children class name is different from the received.";

    private static final String ANDROID_WIDGET_BUTTON = "android.widget.Button";

    private static final String BUTTON_QUERY = "//*[@className='android.widget.Button']";

    private static final String RELATIVE_LAYOUT_QUERY = "//*[@className='android.widget.RelativeLayout']";

    private static final String IMAGE_BUTTON_QUERY = "//*[@className='android.widget.ImageButton']";

    private static final String ALL_ELEMENTS_QUERY = "//*";

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
    public void testGetChildrenByXPathWhenNoMatchesFound() throws Exception {
        UiElement parent = screen.getElementByXPath(RELATIVE_LAYOUT_QUERY);

        List<UiElement> children = parent.getChildrenByXPath(IMAGE_BUTTON_QUERY);

        assertTrue(UNEXPECTED_ELEMENT_FOUND_MESSAGE, children.isEmpty());
    }

    @Test
    public void testGetChildrenByXPathThatMatchSelectorAttributes() throws Exception {
        int expectedChildrenCount = 3;
        UiElement parent = screen.getElementByXPath(RELATIVE_LAYOUT_QUERY);

        List<UiElement> children = parent.getChildrenByXPath(BUTTON_QUERY);

        assertEquals(MISSMATCH_CHILDREN_COUNT_ERROR_MESSAGE, expectedChildrenCount, children.size());

        for (UiElement child : children) {
            assertEquals(ELEMENT_PROPERTY_MISSMATCH_MESSAGE,
                         ANDROID_WIDGET_BUTTON,
                         child.getProperties().getClassName());
        }
    }

    @Test
    public void testGetAllChildrenByXPath() throws Exception {
        int expectedChildrenCount = 9;
        UiElement parent = screen.getElementByXPath(RELATIVE_LAYOUT_QUERY);

        List<UiElement> children = parent.getChildrenByXPath(ALL_ELEMENTS_QUERY);

        assertEquals(MISSMATCH_CHILDREN_COUNT_ERROR_MESSAGE, expectedChildrenCount, children.size());
    }
}
