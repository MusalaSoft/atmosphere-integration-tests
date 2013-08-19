package com.musala.atmosphere.test.util.ondevicevalidator;

/**
 * Class containing information about all OnDeviceValidator UI elements' content description.
 * 
 * @author yordan.petrov
 * 
 */
public enum ContentDescriptior
{
	BATTERY_LEVEL_BOX("BatteryLevelBox"), BATTERY_STATUS_BOX("BatteryStatusBox"), POWER_CONNECTED_FLAG(
			"PowerConnectedFlag"), BATTERY_LOW_FLAG("BatteryLowFlag"), AIRPLANE_MODE_FLAG("AirplaneModeFlag"), INPUT_TEXT_BOX(
			"InputTextBox"), ORIENTATION_AZIMUTH_BOX("OrientationAzimuthBox"), ORIENTATION_PITCH_BOX(
			"OrientationPitchBox"), ORIENTATION_ROLL_BOX("OrientationRollBox");

	private String value;

	private ContentDescriptior(String value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return value;
	}
}
