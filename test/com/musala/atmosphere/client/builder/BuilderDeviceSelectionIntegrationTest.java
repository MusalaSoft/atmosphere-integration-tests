/*package com.musala.atmosphere.client.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.rmi.registry.Registry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.Device;
import com.musala.atmosphere.client.util.Server;
import com.musala.atmosphere.commons.DeviceInformation;
import com.musala.atmosphere.commons.RoutingAction;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceOs;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelector;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceSelectorBuilder;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.cs.exception.DeviceNotFoundException;
import com.musala.atmosphere.commons.cs.exception.ValidationException;
import com.musala.atmosphere.commons.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.sa.IAgentManager;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.commons.util.Pair;
import com.musala.atmosphere.server.data.db.ormlite.AgentDao;
import com.musala.atmosphere.server.data.provider.ormlite.DataSourceProvider;
import com.musala.atmosphere.server.pool.PoolManager;

*//**
 * 
 * @author valyo.yolovski
 * 
 *//*
public class BuilderDeviceSelectionIntegrationTest {
    private static final int INVALID_API_LEVEL = 66;

    private static final int NONEXISTING_DEVICE_SCREEN_DPI = 999;

    private static final int NONEXISTING_DEVICE_RESOLUTION = 999;

    private static final int NONEXISTING_DEVICE_RAM = 999;

    private static final String NONEXISTING_DEVICE_MODEL = "nonexisting_model";

    private static final String NONEXISTING_DEVICE_SERIAL_NUMBER = "nonexisting_serial_number";

    private final static int SERVER_MANAGER_RMI_PORT = 2099;

    private final static int AGENTMANAGER_RMI_PORT = 2000;

    private final static String AGENT_ID = "mockagent";

    private final static String DEVICE1_SERIAL_NUMBER = "mockdevice1";

    private final static String DEVICE1_MODEL = "awesomemockdevice1";

    private final static String DEVICE2_SERIAL_NUMBER = "mockdevice2";

    private final static String DEVICE2_MODEL = "awesomemockdevice2";

    private final static String DEVICE3_SERIAL_NUMBER = "mockdevice3";

    private final static String DEVICE3_MODEL = "awesomemockdevice3";

    private final static String TEST_IP_ADDRESS = "localhost";

    private static final String SCREEN_RECORD_COMPONENT_PATH = "/data/local/tmp";

    private static final String STOP_SCREENRECORD_SCRIPT_NAME = "stop_screenrecord.sh";

    private static final String STOP_SCREEN_RECORD_COMMAND = String.format("sh %s/%s",
                                                                           SCREEN_RECORD_COMPONENT_PATH,
                                                                           STOP_SCREENRECORD_SCRIPT_NAME);

    private static DeviceInformation mockedDeviceOneInformation;

    private static AgentDao agentDao;

    private Builder builder;

    @Server(ip = TEST_IP_ADDRESS, port = SERVER_MANAGER_RMI_PORT, connectionRetryLimit = 10)
    static class GettingDeviceSampleClass {
        public static Builder getBuilderInstance() {
            return Builder.getInstance();
        }
    }

    @BeforeClass
    public static void setUp() throws Exception {
        IAgentManager mockedAgentManager = mock(IAgentManager.class);
        when(mockedAgentManager.getAgentId()).thenReturn(AGENT_ID);

        DataSourceProvider dataSourceProvider = new DataSourceProvider();
        agentDao = dataSourceProvider.getAgentDao();

        if (agentDao != null) {
            agentDao.add(AGENT_ID, TEST_IP_ADDRESS, AGENTMANAGER_RMI_PORT);
        }

        IWrapDevice mockedDeviceOne = mock(IWrapDevice.class);
        mockedDeviceOneInformation = new DeviceInformation();
        mockedDeviceOneInformation.setSerialNumber(DEVICE1_SERIAL_NUMBER);
        mockedDeviceOneInformation.setModel(DEVICE1_MODEL);
        mockedDeviceOneInformation.setOs("4.2.1");
        mockedDeviceOneInformation.setEmulator(true);
        mockedDeviceOneInformation.setRam(511);
        mockedDeviceOneInformation.setResolution(new Pair<>(801, 601));
        mockedDeviceOneInformation.setDpi(121);
        mockedDeviceOneInformation.setApiLevel(7);
        when(mockedDeviceOne.route(eq(RoutingAction.GET_DEVICE_INFORMATION))).thenReturn(mockedDeviceOneInformation);

        IWrapDevice mockedDeviceTwo = mock(IWrapDevice.class);
        DeviceInformation mockedDeviceTwoInformation = new DeviceInformation();
        mockedDeviceTwoInformation.setSerialNumber(DEVICE2_SERIAL_NUMBER);
        mockedDeviceTwoInformation.setModel(DEVICE2_MODEL);
        mockedDeviceTwoInformation.setOs("4.2.2");
        mockedDeviceTwoInformation.setEmulator(true);
        mockedDeviceTwoInformation.setRam(512);
        mockedDeviceTwoInformation.setResolution(new Pair<>(802, 602));
        mockedDeviceTwoInformation.setDpi(122);
        mockedDeviceTwoInformation.setApiLevel(15);
        when(mockedDeviceTwo.route(eq(RoutingAction.GET_DEVICE_INFORMATION))).thenReturn(mockedDeviceTwoInformation);

        IWrapDevice mockedDeviceThree = mock(IWrapDevice.class);
        DeviceInformation mockedDeviceThreeInformation = new DeviceInformation();
        mockedDeviceThreeInformation.setSerialNumber(DEVICE3_SERIAL_NUMBER);
        mockedDeviceThreeInformation.setModel(DEVICE3_MODEL);
        mockedDeviceThreeInformation.setOs("4.2.3");
        mockedDeviceThreeInformation.setEmulator(false);
        mockedDeviceThreeInformation.setRam(513);
        mockedDeviceThreeInformation.setResolution(new Pair<>(803, 603));
        mockedDeviceThreeInformation.setDpi(123);
        mockedDeviceThreeInformation.setApiLevel(10);
        when(mockedDeviceThree.route(eq(RoutingAction.GET_DEVICE_INFORMATION))).thenReturn(mockedDeviceThreeInformation);

        Registry mockRegistry = mock(Registry.class);
        when(mockRegistry.lookup(DEVICE1_SERIAL_NUMBER)).thenReturn(mockedDeviceOne);
        when(mockRegistry.lookup(DEVICE2_SERIAL_NUMBER)).thenReturn(mockedDeviceTwo);
        when(mockRegistry.lookup(DEVICE3_SERIAL_NUMBER)).thenReturn(mockedDeviceThree);

        when(mockedDeviceOne.route(eq(RoutingAction.EXECUTE_SHELL_COMMAND), eq(STOP_SCREEN_RECORD_COMMAND))).thenReturn("");
        when(mockedDeviceTwo.route(eq(RoutingAction.EXECUTE_SHELL_COMMAND), eq(STOP_SCREEN_RECORD_COMMAND))).thenReturn("");
        when(mockedDeviceThree.route(eq(RoutingAction.EXECUTE_SHELL_COMMAND), eq(STOP_SCREEN_RECORD_COMMAND))).thenReturn("");

        PoolManager poolManager = PoolManager.getInstance();

        if (agentDao != null) {
            poolManager.addDevice(DEVICE1_SERIAL_NUMBER, mockRegistry, AGENT_ID);
            poolManager.addDevice(DEVICE2_SERIAL_NUMBER, mockRegistry, AGENT_ID);
            poolManager.addDevice(DEVICE3_SERIAL_NUMBER, mockRegistry, AGENT_ID);
        }
    }

    @Before
    public void setUpBeforeEachTest() {
        builder = GettingDeviceSampleClass.getBuilderInstance();
    }

    @After
    public void tearDownAfterEachTest() throws DeviceNotFoundException {
        builder.releaseAllDevices();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        PoolManager poolManager = PoolManager.getInstance();
        Class<?> packageManagerClass = PoolManager.class;
        Method deviceIdBuild = packageManagerClass.getDeclaredMethod("buildDeviceIdentifier",
                                                                     String.class,
                                                                     String.class);
        deviceIdBuild.setAccessible(true);

        poolManager.removeDevice((String) deviceIdBuild.invoke(null, AGENT_ID, DEVICE1_SERIAL_NUMBER));
        poolManager.removeDevice((String) deviceIdBuild.invoke(null, AGENT_ID, DEVICE2_SERIAL_NUMBER));
        poolManager.removeDevice((String) deviceIdBuild.invoke(null, AGENT_ID, DEVICE3_SERIAL_NUMBER));

        if (agentDao != null) {
            agentDao.remove(AGENT_ID);
        }
    }

    @Test
    public void testGetDeviceByNoPrefference() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder();
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        assertNotNull("Got null device.", receivedDevice);
    }

    @Test
    public void testGetDeviceBySerialNumber() {
        String wantedSerialNumber = DEVICE1_SERIAL_NUMBER;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().serialNumber(wantedSerialNumber);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation information = receivedDevice.getInformation();
        String realSerialNumber = information.getSerialNumber();

        assertEquals("Device serial number does not match requested serial number.",
                     wantedSerialNumber,
                     realSerialNumber);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingSerialNumber() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().serialNumber(NONEXISTING_DEVICE_SERIAL_NUMBER)
                                                                           .deviceType(DeviceType.DEVICE_ONLY);
        DeviceSelector deviceSelector = selectorBuilder.build();

        builder.getDevice(deviceSelector);
    }

    @Test
    public void testGetEmulatorDevice() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.EMULATOR_ONLY);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);
        DeviceInformation information = receivedDevice.getInformation();
        boolean isDeviceEmulator = information.isEmulator();

        assertTrue("Device is not an emulator.", isDeviceEmulator);
    }

    @Test
    public void testGetRealDevice() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation information = receivedDevice.getInformation();
        boolean isDeviceEmulator = information.isEmulator();

        assertFalse("Device is not real device.", isDeviceEmulator);
    }

    @Test
    public void testGetDeviceByOperatingSystem() {
        DeviceOs wantedOS = DeviceOs.JELLY_BEAN_MR1_4_2_2;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceOs(wantedOS);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation information = receivedDevice.getInformation();
        String realOS = information.getOS();

        assertEquals("Device OS does not match requested OS.", wantedOS.toString(), realOS);
    }

    // FIXME If a test device with OS DeviceOs.JELLY_BEAN_4_1 is connected to the server this test will fail.
    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingOperatingSystem() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceOs(DeviceOs.JELLY_BEAN_4_1);
        DeviceSelector deviceSelector = selectorBuilder.build();

        builder.getDevice(deviceSelector);
    }

    @Test
    public void testGetDeviceByModel() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceModel(DEVICE2_MODEL);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation information = receivedDevice.getInformation();
        String realModel = information.getModel();

        assertEquals("Device model does not match requested model.", DEVICE2_MODEL, realModel);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingModel() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY)
                                                                           .deviceModel(NONEXISTING_DEVICE_MODEL);
        DeviceSelector deviceSelector = selectorBuilder.build();

        builder.getDevice(deviceSelector);
    }

    @Test
    public void testGetDeviceByRAM() {
        int wantedRam = 513;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().ramCapacity(wantedRam);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation information = receivedDevice.getInformation();
        int realRam = information.getRam();

        assertEquals("Device RAM does not match requested RAM.", wantedRam, realRam);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingRam() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY)
                                                                           .ramCapacity(NONEXISTING_DEVICE_RAM);
        DeviceSelector deviceSelector = selectorBuilder.build();

        builder.getDevice(deviceSelector);
    }

    @Test
    public void testGetDeviceByResolutionWidth() {
        int wantedResolutionWidth = 601;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().screenWidth(wantedResolutionWidth);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation information = receivedDevice.getInformation();
        int realResolutionWidth = information.getResolution().getValue();

        assertEquals("Device resolution width does not match requested resolution width.",
                     wantedResolutionWidth,
                     realResolutionWidth);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingResolutionWidth() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY)
                                                                           .screenWidth(NONEXISTING_DEVICE_RESOLUTION);
        DeviceSelector deviceSelector = selectorBuilder.build();

        builder.getDevice(deviceSelector);
    }

    @Test
    public void testGetDeviceByResolutionHeight() {
        int wantedResolutionHeight = 801;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().screenHeight(wantedResolutionHeight);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation information = receivedDevice.getInformation();
        int realResolutionHeight = information.getResolution().getKey();

        assertEquals("Device resolution width does not match requested resolution height.",
                     wantedResolutionHeight,
                     realResolutionHeight);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingResolutionHeight() {
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY)
                                                                           .screenHeight(NONEXISTING_DEVICE_RESOLUTION);
        DeviceSelector deviceSelector = selectorBuilder.build();

        builder.getDevice(deviceSelector);
    }

    @Test
    public void testGetDeviceByDpi() {
        int wantedDpi = 122;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().screenDpi(wantedDpi);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation information = receivedDevice.getInformation();
        int realDPI = information.getDpi();

        assertEquals("Device DPI does not match requested DPI.", wantedDpi, realDPI);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingDpi() {

        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().deviceType(DeviceType.DEVICE_ONLY)
                                                                           .screenDpi(NONEXISTING_DEVICE_SCREEN_DPI);
        DeviceSelector deviceSelector = selectorBuilder.build();

        builder.getDevice(deviceSelector);
    }

    @Test
    public void testGetDeviceByApi() {
        int wantedApiVersion = 7;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().targetApi(wantedApiVersion);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation information = receivedDevice.getInformation();
        int realApiVersion = information.getApiLevel();

        assertEquals("Device Api version does not match the requested one.", wantedApiVersion, realApiVersion);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByInvalidApiVersion() {

        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().targetApi(INVALID_API_LEVEL);
        DeviceSelector deviceSelector = selectorBuilder.build();

        builder.getDevice(deviceSelector);
    }

    @Test
    public void testGetDeviceByGivenMinimumApiVersion() {
        int minApiVersion = 13;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().minApi(minApiVersion);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertTrue("Received device has lower API level than the set minimum.",
                   receivedDeviceInformation.getApiLevel() >= minApiVersion);
    }

    @Test
    public void testGetDeviceByGivenMaximumApiVersion() {
        int maxApiVersion = 19;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().maxApi(maxApiVersion);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertTrue("Received device has bigger API level than the set maximum.",
                   receivedDeviceInformation.getApiLevel() <= maxApiVersion);
    }

    @Test
    public void testGetDeviceByGivenRangeForApiVersion() {
        int maxApiVersion = 20;
        int minApiVersion = 15;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().minApi(minApiVersion).maxApi(maxApiVersion);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertTrue("Received device has bigger API level than the set maximum.",
                   receivedDeviceInformation.getApiLevel() <= maxApiVersion);
        assertTrue("Received device has lower API level than the set minimum.",
                   receivedDeviceInformation.getApiLevel() >= minApiVersion);
    }

    @Test
    public void testGetDeviceByGivenRangeAndTargetForApiVersion() {
        int maxApiVersion = 20;
        int minApiVersion = 5;
        int targetApiVersion = 10;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().minApi(minApiVersion)
                                                                           .maxApi(maxApiVersion)
                                                                           .targetApi(targetApiVersion);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertEquals("Received device has different API level than the set target API level.",
                     receivedDeviceInformation.getApiLevel(),
                     targetApiVersion);
    }

    @Test
    public void testGetDeviceByGivenRangeAndInvalidTargetApiVersion() {
        int maxApiVersion = 16;
        int minApiVersion = 11;
        int targetApiVersion = 12;
        int expectedApiVersion = 15;
        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().minApi(minApiVersion)
                                                                           .maxApi(maxApiVersion)
                                                                           .targetApi(targetApiVersion);
        DeviceSelector deviceSelector = selectorBuilder.build();

        Device receivedDevice = builder.getDevice(deviceSelector);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertEquals("Received device is with different Api level than expected.",
                     expectedApiVersion,
                     receivedDeviceInformation.getApiLevel());
    }

    @Test(expected = ValidationException.class)
    public void testInvalidMaxApiInRange() throws Exception {
        int invalidMaxApiVersion = 7;
        int minApiVersion = 12;
        int targetApiVersion = 14;

        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().minApi(minApiVersion)
                                                                           .maxApi(invalidMaxApiVersion)
                                                                           .targetApi(targetApiVersion);
        selectorBuilder.build();
    }

    @Test(expected = ValidationException.class)
    public void testInvalidMinApiInRange() throws Exception {
        int maxApiVersion = 9;
        int invalidMinApiVersion = 13;
        int targetApiVersion = 7;

        DeviceSelectorBuilder selectorBuilder = new DeviceSelectorBuilder().minApi(invalidMinApiVersion)
                                                                           .maxApi(maxApiVersion)
                                                                           .targetApi(targetApiVersion);
        selectorBuilder.build();
    }
}
*/