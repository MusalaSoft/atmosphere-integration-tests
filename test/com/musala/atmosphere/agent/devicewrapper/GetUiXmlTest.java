package com.musala.atmosphere.agent.devicewrapper;

import static com.musala.atmosphere.test.util.ondevicevalidator.OnDeviceValidatorAssert.setTestDevice;
import static org.junit.Assert.assertTrue;

import java.io.RandomAccessFile;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.BaseIntegrationTest;
import com.musala.atmosphere.client.Screen;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;

public class GetUiXmlTest extends BaseIntegrationTest {
    private static final String SCREEN_XML_FILE_NAME = "screen_xml.xml";

    private static RandomAccessFile xmlDumpFile;

    @BeforeClass
    public static void setUp() throws Exception {
        initTestDevice(new DeviceSelectorBuilder().build());
        setTestDevice(testDevice);
        Screen screen = testDevice.getActiveScreen();
        screen.exportToXml(SCREEN_XML_FILE_NAME);

        xmlDumpFile = new RandomAccessFile(SCREEN_XML_FILE_NAME, "r");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (xmlDumpFile != null) {
            xmlDumpFile.close();
        }

        releaseDevice();
    }

    @Test
    public void getUiXmlTest() throws Exception {
        String expectedBeginning = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String expectedRootTagStart = "<hierarchy";
        String expectedClosingTag = "</hierarchy>";
        long xmlDumpLength = xmlDumpFile.length();

        assertTrue("Received screen XML is empty.", xmlDumpLength > 0);

        String beginning = xmlDumpFile.readLine();
        String rootTag = xmlDumpFile.readLine();

        assertTrue("UI screen XML must starts with a <?xml ...> tag.", beginning.equals(expectedBeginning));
        assertTrue("UI screen XML root tag must starts with a <hierarchy ...> tag.",
                   rootTag.startsWith(expectedRootTagStart));

        long offset = xmlDumpLength - expectedClosingTag.length() - 2;
        xmlDumpFile.seek(offset);

        String rootClosingTag = xmlDumpFile.readLine();
        assertTrue("UI screen XML must ends with a closing tag for the <hierarchy ..> tag.",
                   rootClosingTag.equals(expectedClosingTag));
    }
}
