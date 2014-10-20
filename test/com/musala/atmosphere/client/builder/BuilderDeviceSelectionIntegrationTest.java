package com.musala.atmosphere.client.builder;

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
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceOs;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceParameters;
import com.musala.atmosphere.commons.cs.clientbuilder.DeviceType;
import com.musala.atmosphere.commons.sa.IAgentManager;
import com.musala.atmosphere.commons.sa.IWrapDevice;
import com.musala.atmosphere.commons.sa.exceptions.NoAvailableDeviceFoundException;
import com.musala.atmosphere.commons.util.Pair;
import com.musala.atmosphere.server.pool.PoolManager;

/**
 * 
 * @author valyo.yolovski
 * 
 */
public class BuilderDeviceSelectionIntegrationTest {
    private final static int SERVER_MANAGER_RMI_PORT = 2099;

    private final static String AGENT_ID = "mockagent";

    private final static String DEVICE1_SN = "mockdevice1";

    private final static String DEVICE1_MODEL = "awesomemockdevice1";

    private final static String DEVICE2_SN = "mockdevice2";

    private final static String DEVICE2_MODEL = "awesomemockdevice2";

    private final static String DEVICE3_SN = "mockdevice3";

    private final static String DEVICE3_MODEL = "awesomemockdevice3";

    private static DeviceInformation mockedDeviceInfoOne = null;

    private Builder builder;

    @Server(ip = "localhost", port = SERVER_MANAGER_RMI_PORT, connectionRetryLimit = 10)
    static class GettingDeviceSampleClass {
        public static Builder getBuilderInstance() {
            return Builder.getInstance();
        }
    }

    @BeforeClass
    public static void setUp() throws Exception {
        IAgentManager mockedAgentManager = mock(IAgentManager.class);
        when(mockedAgentManager.getAgentId()).thenReturn(AGENT_ID);

        IWrapDevice mockedDeviceOne = mock(IWrapDevice.class);
        mockedDeviceInfoOne = new DeviceInformation();
        mockedDeviceInfoOne.setSerialNumber(DEVICE1_SN);
        mockedDeviceInfoOne.setModel(DEVICE1_MODEL);
        mockedDeviceInfoOne.setOs("4.2.1");
        mockedDeviceInfoOne.setEmulator(true);
        mockedDeviceInfoOne.setRam(511);
        mockedDeviceInfoOne.setResolution(new Pair<>(801, 601));
        mockedDeviceInfoOne.setDpi(121);
        mockedDeviceInfoOne.setApiLevel(19);
        when(mockedDeviceOne.route(eq(RoutingAction.GET_DEVICE_INFORMATION))).thenReturn(mockedDeviceInfoOne);

        IWrapDevice mockedDeviceTwo = mock(IWrapDevice.class);
        DeviceInformation mockedDeviceInfoTwo = new DeviceInformation();
        mockedDeviceInfoTwo.setSerialNumber(DEVICE2_SN);
        mockedDeviceInfoTwo.setModel(DEVICE2_MODEL);
        mockedDeviceInfoTwo.setOs("4.2.2");
        mockedDeviceInfoTwo.setEmulator(true);
        mockedDeviceInfoTwo.setRam(512);
        mockedDeviceInfoTwo.setResolution(new Pair<>(802, 602));
        mockedDeviceInfoTwo.setDpi(122);
        when(mockedDeviceTwo.route(eq(RoutingAction.GET_DEVICE_INFORMATION))).thenReturn(mockedDeviceInfoTwo);

        IWrapDevice mockedDeviceThree = mock(IWrapDevice.class);
        DeviceInformation mockedDeviceInfoThree = new DeviceInformation();
        mockedDeviceInfoThree.setSerialNumber(DEVICE3_SN);
        mockedDeviceInfoThree.setModel(DEVICE3_MODEL);
        mockedDeviceInfoThree.setOs("4.2.3");
        mockedDeviceInfoThree.setEmulator(false);
        mockedDeviceInfoThree.setRam(513);
        mockedDeviceInfoThree.setResolution(new Pair<>(803, 603));
        mockedDeviceInfoThree.setDpi(123);
        when(mockedDeviceThree.route(eq(RoutingAction.GET_DEVICE_INFORMATION))).thenReturn(mockedDeviceInfoThree);

        Registry mockRegistry = mock(Registry.class);
        when(mockRegistry.lookup(DEVICE1_SN)).thenReturn(mockedDeviceOne);
        when(mockRegistry.lookup(DEVICE2_SN)).thenReturn(mockedDeviceTwo);
        when(mockRegistry.lookup(DEVICE3_SN)).thenReturn(mockedDeviceThree);

        PoolManager poolManager = PoolManager.getInstance();

        poolManager.addDevice(DEVICE1_SN, mockRegistry, mockedAgentManager);
        poolManager.addDevice(DEVICE2_SN, mockRegistry, mockedAgentManager);
        poolManager.addDevice(DEVICE3_SN, mockRegistry, mockedAgentManager);
    }

    @Before
    public void setUpBeforeEachTest() {
        builder = GettingDeviceSampleClass.getBuilderInstance();
    }

    @After
    public void tearDownAfterEachTest() {
        builder.releaseAllDevices();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        PoolManager poolManager = PoolManager.getInstance();
        Class<?> pmc = Class.forName("com.musala.atmosphere.server.pool.PoolManager");
        Method deviceIdBuild = pmc.getDeclaredMethod("buildDeviceIdentifier", String.class, String.class);
        deviceIdBuild.setAccessible(true);

        poolManager.removeDevice((String) deviceIdBuild.invoke(null, AGENT_ID, DEVICE1_SN));
        poolManager.removeDevice((String) deviceIdBuild.invoke(null, AGENT_ID, DEVICE2_SN));
        poolManager.removeDevice((String) deviceIdBuild.invoke(null, AGENT_ID, DEVICE3_SN));
    }

    @Test
    public void testGetDeviceByNoPrefference() {
        DeviceParameters parameters = new DeviceParameters();

        Device receivedDevice = builder.getDevice(parameters);

        assertNotNull("Got null device.", receivedDevice);
    }

    @Test
    public void testGetDeviceBySerialNumber() {
        DeviceParameters parameters = new DeviceParameters();

        String wantedSerialNumber = DEVICE1_SN;
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
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setSerialNumber("nonexisting_serial_number");

        builder.getDevice(parameters);
    }

    @Test
    public void testGetEmulatorDevice() {
        DeviceParameters parameters = new DeviceParameters();

        parameters.setDeviceType(DeviceType.EMULATOR_ONLY);

        Device receivedDevice = builder.getDevice(parameters);
        DeviceInformation information = receivedDevice.getInformation();
        boolean isDeviceEmulator = information.isEmulator();

        assertTrue("Device is not an emulator.", isDeviceEmulator);
    }

    @Test
    public void testGetRealDevice() {
        DeviceParameters parameters = new DeviceParameters();

        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        boolean isDeviceEmulator = information.isEmulator();

        assertFalse("Device is not real device.", isDeviceEmulator);
    }

    @Test
    public void testGetDeviceByOperatingSystem() {
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
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setOs(DeviceOs.JELLY_BEAN_4_1);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByModel() {
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
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setModel("nonexisting_model");

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByRAM() {
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
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setRam(999);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByResolutionWidth() {
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
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setResolutionWidth(999);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByResolutionHeight() {
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
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setResolutionHeight(999);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByDPI() {
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
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setDpi(999);

        builder.getDevice(parameters);
    }

    @Test
    public void testGetDeviceByParametersCreatedFromMockedDeviceInfoOne() {

        DeviceParameters parameters = new DeviceParameters(mockedDeviceInfoOne);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();

        assertEquals("Device information does not match the initial information", information, mockedDeviceInfoOne);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByParametersCreatedFromChangedMockedDeviceInfoOne() {
        DeviceInformation changedMockedDeviceInfoOne = null;
        changedMockedDeviceInfoOne = new DeviceInformation();
        changedMockedDeviceInfoOne.setSerialNumber(DEVICE1_SN);
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

        DeviceParameters parameters = new DeviceParameters();

        int wantedApiVersion = 19;
        parameters.setApiLevel(wantedApiVersion);

        Device receivedDevice = builder.getDevice(parameters);

        DeviceInformation information = receivedDevice.getInformation();
        int realApiVersion = information.getApiLevel();

        assertEquals("Device Api version does not match the requested one.", wantedApiVersion, realApiVersion);
    }

    @Test(expected = NoAvailableDeviceFoundException.class)
    public void testGetDeviceByInvalidApiVersion() {
        DeviceParameters parameters = new DeviceParameters();
        parameters.setDeviceType(DeviceType.DEVICE_ONLY);

        parameters.setApiLevel(666);

        builder.getDevice(parameters);
    }
}
