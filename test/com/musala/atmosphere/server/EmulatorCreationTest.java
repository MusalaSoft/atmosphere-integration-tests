package com.musala.atmosphere.server;

import com.musala.atmosphere.BaseIntegrationTest;

public class EmulatorCreationTest extends BaseIntegrationTest {
    /* FIXME : after fixing the logic with the emulator creation, fix this test */
    // private PoolManager poolManager = PoolManager.getInstance();
    //
    // // max timeout in seconds for waiting for a newly created emulator to be registered on the Agent
    // private static final int MAX_TIMEOUT = 240;
    //
    // private EmulatorParameters getEmulatorDeviceParameters() {
    // EmulatorParameters params = new EmulatorParameters();
    // params.setDpi(120);
    // params.setApiLevel(17);
    // params.setRam(256l);
    // Pair<Integer, Integer> resolution = new Pair<>(320, 240);
    // params.setResolution(resolution);
    // return params;
    // }
    //
    // /**
    // * Removes all created virtual devices and emulators during the execution of this test.
    // *
    // * @param initialListOfDevices
    // * - list of all connected to the Agent running emulators before the test run.
    // * @param listOfDevicesAfterTest
    // * - list of all connected devices to the Agent <u>after</u> the test is executed.
    // * @throws Exception
    // */
    // private void discardCreatedEmulators(List<String> initialListOfDevices, List<String> listOfDevicesAfterTest)
    // throws Exception {
    // int numberOfDevicesAfterTest = listOfDevicesAfterTest.size();
    //
    // for (int indexOfDevice = numberOfDevicesAfterTest - 1; indexOfDevice >= 0; indexOfDevice--) {
    // String currentDeviceId = listOfDevicesAfterTest.get(indexOfDevice);
    // if (!initialListOfDevices.contains(currentDeviceId)) {
    // poolManager.removeDevice(currentDeviceId, agent.getId());
    // }
    // }
    // }
    //
    // @Test
    // public void createEmulatorTest() throws Exception {
    // List<String> initialListOfDevices = poolManager.getAllUnderlyingDeviceProxyIds();
    // int initialNumberOfDevices = initialListOfDevices.size();
    // int expectedNumberOfDevices = initialNumberOfDevices + 1;
    //
    // // FIXME This timeout mechanism should be revised.
    // for (int timeout = MAX_TIMEOUT; timeout > 0; timeout--) {
    // Thread.sleep(1000);
    // if (poolManager.getAllUnderlyingDeviceProxyIds().size() != initialNumberOfDevices) {
    // break;
    // }
    // }
    //
    // int actualNumberOfDevices = poolManager.getAllUnderlyingDeviceProxyIds().size();
    // assertEquals("Number of expected and actual devices doesn't match.",
    // expectedNumberOfDevices,
    // actualNumberOfDevices);
    //
    // List<String> newListOfDevices = poolManager.getAllUnderlyingDeviceProxyIds();
    // discardCreatedEmulators(initialListOfDevices, newListOfDevices);
    // }
}
