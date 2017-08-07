package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startDragActivity;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.geometry.Point;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

/**
 *
 * @author konstantin.ivanov
 *
 */
public class DragTest extends BaseIntegrationTest {

    private static final String IMAGE_ELEMENT_ID = "com.musala.atmosphere.ondevice.validator:id/myImage";

    private static final String TEXT_ELEMENT_ID = "com.musala.atmosphere.ondevice.validator:id/myText";

    private static final String MESSAGE_TEST_FAIL = "Drag and drop failed. The expected destination point is different from elements center point";

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED);
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        initTestDevice(testDeviceSelector);

        setTestDevice(testDevice);
    }

    @Before
    public void setUpTest() throws Exception {
        startDragActivity();
    }

    @After
    public void tearDownTest() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        releaseDevice();
    }

    @Test
    public void testDragToPoint() throws Exception {
        Screen deviceScreen = testDevice.getActiveScreen();

        UiElementSelector imageElementSelector = new UiElementSelector();
        imageElementSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, IMAGE_ELEMENT_ID);
        UiElement imageElement = deviceScreen.getElement(imageElementSelector);

        UiElementSelector textElementSelector = new UiElementSelector();
        textElementSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, TEXT_ELEMENT_ID);
        UiElement texElement = deviceScreen.getElement(textElementSelector);

        Point destinationPoint = texElement.getProperties().getBounds().getCenter();
        imageElement.drag(destinationPoint);

        imageElement = deviceScreen.getElement(imageElementSelector);
        Point actualPoint = imageElement.getProperties().getBounds().getCenter();
        assertEquals(MESSAGE_TEST_FAIL, destinationPoint, actualPoint);
    }

    @Test
    public void testDragToUiElement() throws Exception {
        Screen deviceScreen = testDevice.getActiveScreen();

        UiElementSelector imageElementSelector = new UiElementSelector();
        imageElementSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, IMAGE_ELEMENT_ID);
        UiElement imageElement = deviceScreen.getElement(imageElementSelector);

        UiElementSelector textElementSelector = new UiElementSelector();
        textElementSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, TEXT_ELEMENT_ID);
        UiElement textElement = deviceScreen.getElement(textElementSelector);

        imageElement.drag(textElement);

        imageElement = deviceScreen.getElement(imageElementSelector);
        textElement = deviceScreen.getElement(textElementSelector);

        Point expectedPoint = textElement.getProperties().getBounds().getCenter();
        Point actualPoint = imageElement.getProperties().getBounds().getCenter();

        assertEquals(MESSAGE_TEST_FAIL, expectedPoint, actualPoint);
    }

    @Test
    public void testDragToUiElementSelector() throws Exception {
        Screen deviceScreen = testDevice.getActiveScreen();

        UiElementSelector imageElementSelector = new UiElementSelector();
        imageElementSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, IMAGE_ELEMENT_ID);
        UiElement imageElement = deviceScreen.getElement(imageElementSelector);

        UiElementSelector textElementSelector = new UiElementSelector();
        textElementSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, TEXT_ELEMENT_ID);
        UiElement texElement = deviceScreen.getElement(textElementSelector);

        imageElement.drag(textElementSelector);

        imageElement = deviceScreen.getElement(imageElementSelector);
        texElement = deviceScreen.getElement(textElementSelector);

        Point expectedPoint = texElement.getProperties().getBounds().getCenter();
        Point actualPoint = imageElement.getProperties().getBounds().getCenter();

        assertEquals(MESSAGE_TEST_FAIL, expectedPoint, actualPoint);
    }

    @Test
    public void testDragToElementToText() throws Exception {
        Screen deviceScreen = testDevice.getActiveScreen();

        UiElementSelector imageElementSelector = new UiElementSelector();
        imageElementSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, IMAGE_ELEMENT_ID);
        UiElement imageElement = deviceScreen.getElement(imageElementSelector);

        UiElementSelector textElementSelector = new UiElementSelector();
        textElementSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, TEXT_ELEMENT_ID);
        UiElement textElement = deviceScreen.getElement(textElementSelector);

        imageElement.drag("Drag Test");

        imageElement = deviceScreen.getElement(imageElementSelector);

        Point expectedPoint = textElement.getProperties().getBounds().getCenter();
        Point actualPoint = imageElement.getProperties().getBounds().getCenter();
        assertEquals(MESSAGE_TEST_FAIL, expectedPoint, actualPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDragToInvalidPoint() throws Exception {
        Screen deviceScreen = testDevice.getActiveScreen();

        UiElementSelector textElementSelector = new UiElementSelector();
        textElementSelector.addSelectionAttribute(CssAttribute.RESOURCE_ID, TEXT_ELEMENT_ID);
        UiElement textElement = deviceScreen.getElement(textElementSelector);

        Point destination = new Point(2000, 2000);

        textElement.drag(destination);
    }
}
