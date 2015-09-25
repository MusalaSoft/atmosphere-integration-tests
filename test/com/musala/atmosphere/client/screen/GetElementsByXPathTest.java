package com.musala.atmosphere.client.screen;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startUiElementsActivity;
import static org.junit.Assert.assertEquals;

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
import com.musala.atmosphere.commons.exceptions.UiElementFetchingException;

/**
 * 
 * @author denis.bialev
 *
 */
public class GetElementsByXPathTest extends BaseIntegrationTest {
    // TODO Find what invisible elements represent in UiAutomator and add tests for them

    private static final String BUTTON_CLASS_NAME = "android.widget.Button";

    private static final String BUTTON_QUERY = "//*[@className='android.widget.Button']";

    private static final String NON_EXISTENT_ELEMENT_QUERY = "//*[@text='nonExistent']";

    private static Screen screen;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder deviceSelectorBuilder = new DeviceSelectorBuilder();
        DeviceSelector deviceSelector = deviceSelectorBuilder.deviceType(DeviceType.DEVICE_PREFERRED).build();
        initTestDevice(deviceSelector);
        setTestDevice(testDevice);
        screen = testDevice.getActiveScreen();

        startUiElementsActivity();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);

        releaseDevice();
    }

    @Test
    public void testGetListOfVisibleUiElementsByXPath() throws Exception {
        int expectedElementsCount = 3;

        List<UiElement> foundElements = screen.getAllElementsByXPath(BUTTON_QUERY);

        assertEquals("The number of found elements is different than expected.",
                     expectedElementsCount,
                     foundElements.size());

        for (UiElement element : foundElements) {
            assertEquals("The found element has different field values than the selector.",
                         BUTTON_CLASS_NAME,
                         element.getProperties().getClassName());
        }
    }

    @Test(expected = UiElementFetchingException.class)
    public void testFindNonExistentElementsByXPath() throws Exception {
        screen.getAllElementsByXPath(NON_EXISTENT_ELEMENT_QUERY);
    }
}
