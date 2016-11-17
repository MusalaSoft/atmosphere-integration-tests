package com.musala.atmosphere.client.device;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.startLogCatActivity;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.client.UiElement;
import com.musala.atmosphere.client.device.log.LogCatLevel;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.ui.selector.CssAttribute;
import com.musala.atmosphere.commons.ui.selector.UiElementSelector;

/**
 *
 * @author filareta.yordanova
 *
 */
public class LogCatFiltersTest extends BaseIntegrationTest {
    private static final int WAIT_FOR_ELEMENT_TIMEOUT = 10000;

    private static final String LOG_FILE_NAME = "device.log";

    private static final String LOG_CAT_BUTTON_CONTENT_DECRIPTOR = "LogCatButton";

    private static final String DEFAULT_LOG_HEADER_BEGINNING = "--------- beginning";

    private static final String LOG_TAG = "LogCatActivity";

    private static final String LOG_CAT_LEVEL_TAG_FILTER_FORMAT = "%s/LogCatActivity";

    private static UiElement generateLogsButton;

    private String logFileAbsolutePath;

    @BeforeClass
    public static void setUp() throws Exception {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_PREFERRED)
                                                                           .maxApi(22); // FIXME: The LogCat format is different on API 23 and above
        DeviceSelector testDeviceSelector = selectorBuilder.build();
        try {
            initTestDevice(testDeviceSelector);
            setTestDevice(testDevice);

            startLogCatActivity();

            Screen screen = testDevice.getActiveScreen();
            UiElementSelector logsButtonSelector = new UiElementSelector();
            logsButtonSelector.addSelectionAttribute(CssAttribute.CONTENT_DESCRIPTION, LOG_CAT_BUTTON_CONTENT_DECRIPTOR);

            screen.waitForElementExists(logsButtonSelector, WAIT_FOR_ELEMENT_TIMEOUT);
            generateLogsButton = screen.getElement(logsButtonSelector);
        } catch (NoAvailableDeviceFoundException e) {
            // Nothing to do here
        }

    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (testDevice != null) {
            testDevice.forceStopProcess(VALIDATOR_APP_PACKAGE);
        }
        releaseDevice();
    }

    @Before
    public void setUpTest() {
        if (testDevice != null) {
            generateLogsButton.tap();
            logFileAbsolutePath = new File(".", LOG_FILE_NAME).getAbsolutePath();
        }
    }

    @After
    public void tearDownTest() {
        if (testDevice != null) {
            assertTrue("Log file was not deleted successfully.", new File(logFileAbsolutePath).delete());
        }
    }

    @Test
    public void testGetLogByLevelFilter() throws Exception {
        assumeNotNull(testDevice);
        testDevice.getDeviceLog(logFileAbsolutePath, LogCatLevel.ERROR);

        int prefixLength = 1;
        Set<String> filterConditions = new HashSet<>();
        filterConditions.add(LogCatLevel.ERROR.toString());
        filterConditions.add(LogCatLevel.FATAL.toString());

        assertLogFileValidity(logFileAbsolutePath, prefixLength, filterConditions);
    }

    @Test
    public void testGetLogByLevelTagPair() throws Exception {
        assumeNotNull(testDevice);
        testDevice.getDeviceLog(logFileAbsolutePath, LogCatLevel.ERROR, LOG_TAG);

        Set<String> filterConditions = new HashSet<>();
        String condition = String.format(LOG_CAT_LEVEL_TAG_FILTER_FORMAT, LogCatLevel.ERROR);
        filterConditions.add(condition);
        condition = String.format(LOG_CAT_LEVEL_TAG_FILTER_FORMAT, LogCatLevel.FATAL);
        filterConditions.add(condition);

        assertLogFileValidity(logFileAbsolutePath, condition.length(), filterConditions);
    }

    @Test
    public void testGetLogByTagFilterOnly() throws Exception {
        assumeNotNull(testDevice);
        testDevice.getDeviceLog(logFileAbsolutePath, LOG_TAG);

        Set<String> filterConditions = new HashSet<>();
        String condition = "";
        for (LogCatLevel level : LogCatLevel.values()) {
            if (!level.equals(LogCatLevel.SILENT)) {
                condition = String.format(LOG_CAT_LEVEL_TAG_FILTER_FORMAT, level);
                filterConditions.add(condition);
            }
        }

        assertLogFileValidity(logFileAbsolutePath, condition.length(), filterConditions);
    }

    // The assert method is used only for conditions from the same length.
    private void assertLogFileValidity(String path, int prefixLength, Set<String> conditions) throws Exception {
        BufferedReader fileReader = new BufferedReader(new FileReader(path));
        String currentLine = fileReader.readLine();

        while ((currentLine = fileReader.readLine()) != null) {
            if (!currentLine.startsWith(DEFAULT_LOG_HEADER_BEGINNING)) {
                String prefix = currentLine.substring(0, prefixLength);
                assertTrue("The prefix of the current log line does not match the criterion for the applied log filter.",
                           conditions.contains(prefix));
            }
        }

        fileReader.close();
    }
}
