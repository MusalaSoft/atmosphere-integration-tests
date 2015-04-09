package com.musala.atmosphere.client.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;
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
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceOs;
import com.musala.atmosphere.commons.cs.deviceselection.DeviceType;
import com.musala.atmosphere.commons.cs.exception.DeviceNotFoundException;
import com.musala.atmosphere.commons.sa.IAgentManager;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.commons.sa.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.util.Pair;
import com.musala.atmosphere.server.data.db.ormlite.AgentDao;
import com.musala.atmosphere.server.data.provider.ormlite.DataSourceProvider;
import com.musala.atmosphere.server.pool.PoolManager;

/**
 * 
 * @author valyo.yolovski
 * 
 */
public class BuilderDeviceSelectionIntegrationTest {
    private final static int SERVER_MANAGER_RMI_PORT = 2099;

    private final static String AGENT_ID = "mockagent";

    private final static String DEVICE1_SERIAL_NUMBER = "mockdevice1";

    private final static String DEVICE1_MODEL = "awesomemockdevice1";

    private final static String DEVICE2_SERIAL_NUMBER = "mockdevice2";

    private final static String DEVICE2_MODEL = "awesomemockdevice2";

    private final static String DEVICE3_SERIAL_NUMBER = "mockdevice3";

    private final static String DEVICE3_MODEL = "awesomemockdevice3";

    private final static String TEST_IP_ADDRESS = "localhost";

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
            agentDao.add(AGENT_ID, TEST_IP_ADDRESS, SERVER_MANAGER_RMI_PORT);
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
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        Device receivedDevice = builder.getDevice(parameters);

        assertNotNull("Got null device.", receivedDevice);
    }

    @Test
    public void testGetDeviceBySerialNumber() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        String wantedSerialNumber = DEVICE1_SERIAL_NUMBER;
        parameters.setSerialNumber(wantedSerialNumber);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        String realSerialNumber = information.getSerialNumber();

        assertEquals("Device serial number does not match requested serial number.",
                     wantedSerialNumber,
                     realSerialNumber);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingSerialNumber() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setSerialNumber("nonexisting_serial_number");

        builder.getDevice(parameters);
    }

    @Test
    public void testGetEmulatorDevice() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        parameters.setDeviceType(DeviceType.EMULATOR_ONLY);

        Device receivedDevice = builder.getDevice(parameters);
        DeviceInformation information = receivedDevice.getInformation();
        boolean isDeviceEmulator = information.isEmulator();

        assertTrue("Device is not an emulator.", isDeviceEmulator);
    }

    @Test
    public void testGetRealDevice() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        boolean isDeviceEmulator = information.isEmulator();

        assertFalse("Device is not real device.", isDeviceEmulator);
    }

    @Test
    public void testGetDeviceByOperatingSystem() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        DeviceOs wantedOS = DeviceOs.JELLY_BEAN_MR1_4_2_2;
        parameters.setOs(wantedOS);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        String realOS = information.getOS();

        assertEquals("Device OS does not match requested OS.", wantedOS.toString(), realOS);
    }

    // FIXME If a test device with OS DeviceOs.JELLY_BEAN_4_1 is connected to the server this test will fail.
    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingOperatingSystem() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setOs(DeviceOs.JELLY_BEAN_4_1);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByModel() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        String wantedModel = DEVICE2_MODEL;
        parameters.setModel(wantedModel);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        String realModel = information.getModel();

        assertEquals("Device model does not match requested model.", wantedModel, realModel);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingModel() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setModel("nonexisting_model");

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByRAM() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        int wantedRAM = 513;
        parameters.setRam(wantedRAM);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        int realRAM = information.getRam();

        assertEquals("Device RAM does not match requested RAM.", wantedRAM, realRAM);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingRAM() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setRam(999);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByResolutionWidth() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        int wantedResolutionWidth = 601;
        parameters.setResolutionWidth(wantedResolutionWidth);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        int realResolutionWidth = information.getResolution().getValue();

        assertEquals("Device resolution width does not match requested resolution width.",
                     wantedResolutionWidth,
                     realResolutionWidth);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingResolutionWidth() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setResolutionWidth(999);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByResolutionHeight() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        int wantedResolutionHeight = 801;
        parameters.setResolutionHeight(wantedResolutionHeight);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        int realResolutionHeight = information.getResolution().getKey();

        assertEquals("Device resolution width does not match requested resolution height.",
                     wantedResolutionHeight,
                     realResolutionHeight);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingResolutionHeight() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setResolutionHeight(999);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByDPI() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        int wantedDPI = 122;
        parameters.setDpi(wantedDPI);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        int realDPI = information.getDpi();

        assertEquals("Device DPI does not match requested DPI.", wantedDPI, realDPI);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByNonexistingDPI() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setDpi(999);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByParametersCreatedFromMockedDeviceInfoOne() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters(mockedDeviceOneInformation);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();

        assertEquals("Device information does not match the initial information",
                     information,
                     mockedDeviceOneInformation);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByParametersCreatedFromChangedMockedDeviceInfoOne() {
        assumeNotNull(agentDao);
        DeviceInformation changedMockedDeviceInfoOne = null;
        changedMockedDeviceInfoOne = new DeviceInformation();
        changedMockedDeviceInfoOne.setSerialNumber(DEVICE1_SERIAL_NUMBER);
        changedMockedDeviceInfoOne.setModel(DEVICE1_MODEL);
        changedMockedDeviceInfoOne.setOs("4.2.1");
        changedMockedDeviceInfoOne.setEmulator(false);
        changedMockedDeviceInfoOne.setRam(514);
        changedMockedDeviceInfoOne.setResolution(new Pair<>(801, 601));
        changedMockedDeviceInfoOne.setDpi(121);

        DeviceParameters parameters = new DeviceParameters(changedMockedDeviceInfoOne);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByApi() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        int wantedApiVersion = 7;
        parameters.setTargetApiLevel(wantedApiVersion);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        int realApiVersion = information.getApiLevel();

        assertEquals("Device Api version does not match the requested one.", wantedApiVersion, realApiVersion);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByInvalidApiVersion() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setTargetApiLevel(666);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByGivenMinimumApiVersion() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();
        int minApiVersion = 17;
        parameters.setMinApiLevel(minApiVersion);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertTrue("Received device has lower API level than the set minimum.",
                   receivedDeviceInformation.getApiLevel() >= minApiVersion);
    }

    @Test
    public void testGetDeviceByGivenMaximumApiVersion() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();
        int maxApiVersion = 19;
        parameters.setMaxApiLevel(maxApiVersion);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertTrue("Received device has bigger API level than the set maximum.",
                   receivedDeviceInformation.getApiLevel() <= maxApiVersion);
    }

    @Test
    public void testGetDeviceByGivenRangeForApiVersion() {
        assumeNotNull(agentDao);

        DeviceParameters parameters = new DeviceParameters();
        int maxApiVersion = 20;
        int minApiVersion = 15;
        parameters.setMaxApiLevel(maxApiVersion);
        parameters.setMinApiLevel(minApiVersion);
        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertTrue("Received device has bigger API level than the set maximum.",
                   receivedDeviceInformation.getApiLevel() <= maxApiVersion);
        assertTrue("Received device has lower API level than the set minimum.",
                   receivedDeviceInformation.getApiLevel() >= minApiVersion);
    }

    @Test
    public void testGetDeviceByGivenRangeAndTargetForApiVersion() {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        int maxApiVersion = 20;
        int minApiVersion = 5;
        int targetApiLevel = 10;

        parameters.setMaxApiLevel(maxApiVersion);
        parameters.setMinApiLevel(minApiVersion);
        parameters.setTargetApiLevel(targetApiLevel);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertEquals("Received device has different API level than the set target API level.",
                     receivedDeviceInformation.getApiLevel(),
                     targetApiLevel);
    }

    @Test
    public void testGetDeviceByGivenRangeAndInvalidTargetApiVersion() {
        assumeNotNull(agentDao);

        DeviceParameters parameters = new DeviceParameters();

        int maxApiVersion = 16;
        int minApiVersion = 11;
        int targetApiLevel = 12;
        int expectedApiVersion = 15;

        parameters.setMaxApiLevel(maxApiVersion);
        parameters.setMinApiLevel(minApiVersion);
        parameters.setTargetApiLevel(targetApiLevel);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertEquals("Received device is with different Api level than expected.",
                     expectedApiVersion,
                     receivedDeviceInformation.getApiLevel());
    }

    @Test
    public void testGetDeviceByGivenRangeAndTargetApiVersionSetInvalidMax() throws Exception {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        int maxApiVersion = 16;
        int minApiVersion = 12;
        int targetApiLevel = 14;
        int expectedApiVersion = 15;

        parameters.setMaxApiLevel(maxApiVersion);
        parameters.setMinApiLevel(minApiVersion);
        parameters.setTargetApiLevel(targetApiLevel);

        Device receiveDevice = builder.getDevice(parameters);

        DeviceInformation receivedDeviceInformation = receiveDevice.getInformation();

        assertEquals("Received device is with different Api level than expected.",
                     expectedApiVersion,
                     receivedDeviceInformation.getApiLevel());
        builder.releaseAllDevices();

        parameters.setMaxApiLevel(7);

        Device receivedDeviceChangedMaxApiLevel = builder.getDevice(parameters);

        DeviceInformation receivedDeviceChangedApiLevelInformation = receivedDeviceChangedMaxApiLevel.getInformation();

        assertEquals("Received device is with different Api level than expected.",
                     expectedApiVersion,
                     receivedDeviceChangedApiLevelInformation.getApiLevel());
    }

    @Test
    public void testGetDeviceByGivenRangeAndTargetSetInvalidMinApiVersion() throws Exception {
        assumeNotNull(agentDao);
        DeviceParameters parameters = new DeviceParameters();

        int maxApiVersion = 9;
        int minApiVersion = 7;
        int targetApiLevel = 7;
        int expectedApiVersion = 7;

        parameters.setMaxApiLevel(maxApiVersion);
        parameters.setMinApiLevel(minApiVersion);
        parameters.setTargetApiLevel(targetApiLevel);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation receivedDeviceInformation = receivedDevice.getInformation();

        assertEquals("Received device is with different Api level than expected.",
                     expectedApiVersion,
                     receivedDeviceInformation.getApiLevel());

        builder.releaseAllDevices();

        parameters.setMaxApiLevel(13);

        Device receiveDeviceChangedMaxApiLevel = builder.getDevice(parameters);

        DeviceInformation receivedDeviceChangedApiLevelInformation = receiveDeviceChangedMaxApiLevel.getInformation();

        assertEquals("Received device is with different Api level than expected after the change of max API version failed.",
                     expectedApiVersion,
                     receivedDeviceChangedApiLevelInformation.getApiLevel());
    }
}
