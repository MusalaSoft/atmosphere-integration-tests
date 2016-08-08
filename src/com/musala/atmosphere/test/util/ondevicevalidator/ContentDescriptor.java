package com.musala.atmosphere.test.util.ondevicevalidator;

/**
 * Class containing information about all OnDeviceValidator UI elements' content description.
 * 
 * @author yordan.petrov
 * 
 */
public enum ContentDescriptor {
    BATTERY_LEVEL_BOX("BatteryLevelBox"),
    BATTERY_STATUS_BOX("BatteryStatusBox"),
    POWER_CONNECTED_FLAG("PowerConnectedFlag"),
    BATTERY_LOW_FLAG("BatteryLowFlag"),
    AIRPLANE_MODE_FLAG("AirplaneModeFlag"),
    WIFI_FLAG("WiFiFlag"),
    CONNECTION_TYPE_BOX("ConnectionTypeBox"),
    INPUT_TEXT_BOX("InputTextBox"),
    ORIENTATION_AZIMUTH_BOX("OrientationAzimuthBox"),
    ORIENTATION_PITCH_BOX("OrientationPitchBox"),
    ORIENTATION_ROLL_BOX("OrientationRollBox"),
    ACCELERATION_X_BOX("AccelerationXBox"),
    ACCELERATION_Y_BOX("AccelerationYBox"),
    ACCELERATION_Z_BOX("AccelerationZBox"),
    MAGNETIC_FIELD_X_BOX("MagneticFieldXBox"),
    MAGNETIC_FIELD_Y_BOX("MagneticFieldYBox"),
    MAGNETIC_FIELD_Z_BOX("MagneticFieldZBox"),
    PROXIMITY_FIELD_BOX("ProximityBox"),
    AUTO_ROTATION_BUTTON("AutoRotationFlag"),
    SCREEN_ORIENTATION_BOX("ScreenOrientationBox"),
    SMS_SENDER_PHONE_BOX("SmsSenderPhone"),
    SMS_TEXT_BOX("SmsText"),
    GESTURE_VALIDATOR("GestureValidator"),
    SCROLL_VIEW_VALIDATOR("ScrollViewValidator"),
    HORIZONTAL_SCROLL_VIEW_VALIDATOR("HorizontalScrollViewValidator"),
    SCROLL_TO_BEGINNING_BUTTON("ScrollToBeginningButton"),
    SCROLL_TO_END_BUTTON("ScrollToEndButton"),
    CHANGING_TEXT_BUTTON_DESCRIPTOR("ChangingTextButton"),
    CAMERA_NUMBER_TEXT_BOX("DeviceCameraNumber"),
    CLEAR_TEXT_BOX("ClearTextTestBox"),
    CLEAR_TEXT_BUTTON("ClearTextButton"),
    TIME_PICKER("TimePickerButton"),
    TIME_PICKER_24_HOURS_FORMAT("TimePicker24HoursFormatButton"),
    SEND_NOTIFICATION_BUTTON("Send notification"),
    DATE_PICKER("SelectDateButton"),
    GPS_LOCATION("GpsLocation"),
    PROVIDER_DISCONNECT_MONITOR("TestProviderDisconnectMonitor"),
    CONTENT_TEXT_BOX("ContentTextBox"),
    EMPTY_TEXT_BOX("EmptyTextBox"),
    EMPTY_FIRST_LINE_TEXT_BOX("EmptyFirstLineTextBox"),
    PASTE_CONTAINER_TEXT_BOX("PasteTextBox");

    private String value;

    private ContentDescriptor(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
