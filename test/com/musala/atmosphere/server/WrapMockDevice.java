package com.musala.atmosphere.server;

import com.musala.atmosphere.agent.devicewrapper.AbstractWrapDevice;
import com.musala.atmosphere.agent.devicewrapper.IWrapDevice;
import com.musala.atmosphere.commons.DeviceInformation;
import com.musala.atmosphere.commons.RoutingAction;
import com.musala.atmosphere.commons.exceptions.CommandFailedException;
import com.musala.atmosphere.commons.util.Pair;

/**
 * A class which mocks the {@link AbstractWrapDevice} class.
 * 
 * @author dimcho.nedev
 *
 */
public class WrapMockDevice implements IWrapDevice {
	private static final long serialVersionUID = -5012012957489621889L;

	private DeviceInformation fakeDeviceInformation;

	protected WrapMockDevice() {
		fakeDeviceInformation = new DeviceInformation();
		fakeDeviceInformation.setApiLevel(23);
		fakeDeviceInformation.setCpu("cpu");
		fakeDeviceInformation.setDpi(300);
		fakeDeviceInformation.setManufacturer("Fake Manufacturer");
		fakeDeviceInformation.setModel("Fake Model");
		fakeDeviceInformation.setOs("Fake OS");
		fakeDeviceInformation.setRam(3072);
		fakeDeviceInformation.setResolution(new Pair<Integer, Integer>(1920, 1080));
		fakeDeviceInformation.setSerialNumber("DeviceSerialNo_0");
		fakeDeviceInformation.setEmulator(false);
		fakeDeviceInformation.setTablet(false);
		fakeDeviceInformation.setCamera(false);
	}

	@Override
	public Object route(RoutingAction action, Object... args) throws CommandFailedException {
		return fakeDeviceInformation;
	}

	@Override
	public void unbindWrapper() {
		// nothing to do here
	}
}
