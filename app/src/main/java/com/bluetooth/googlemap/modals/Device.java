package com.bluetooth.googlemap.modals;

import android.bluetooth.BluetoothClass;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.HashMap;

public class Device implements Serializable {
    private String name;
    private String mac;
    private short signal;
    private LatLng location;
    private String btClass;

    public Device(String mac) {
        this.mac = mac;
    }

    public String getBtClass() {
        return btClass;
    }

    public void setBtClass(String btClass_) {
        btClass = btClass_;
    }

    public void setBtClass(BluetoothClass btClass) {
        int deviceClass = btClass.getDeviceClass();
        switch (deviceClass) {
            case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER:
                this.btClass = "AUDIO_VIDEO_CAMCORDER".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                this.btClass = "AUDIO_VIDEO_CAR_AUDIO".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                this.btClass = "AUDIO_VIDEO_HANDSFREE".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES:
                this.btClass = "AUDIO_VIDEO_HEADPHONES".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO:
                this.btClass = "AUDIO_VIDEO_HIFI_AUDIO".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER:
                this.btClass = "AUDIO_VIDEO_LOUDSPEAKER".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE:
                this.btClass = "AUDIO_VIDEO_MICROPHONE".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO:
                this.btClass = "AUDIO_VIDEO_PORTABLE_AUDIO".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX:
                this.btClass = "AUDIO_VIDEO_SET_TOP_BOX".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED:
                this.btClass = "AUDIO_VIDEO_UNCATEGORIZED".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VCR:
                this.btClass = "AUDIO_VIDEO_VCR".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA:
                this.btClass = "AUDIO_VIDEO_VIDEO_CAMERA".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING:
                this.btClass = "AUDIO_VIDEO_VIDEO_CONFERENCING".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER:
                this.btClass = "AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY:
                this.btClass = "AUDIO_VIDEO_VIDEO_GAMING_TOY".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR:
                this.btClass = "AUDIO_VIDEO_VIDEO_MONITOR".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                this.btClass = "AUDIO_VIDEO_WEARABLE_HEADSET".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.COMPUTER_DESKTOP:
                this.btClass = "COMPUTER_DESKTOP".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA:
                this.btClass = "COMPUTER_HANDHELD_PC_PDA".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.COMPUTER_LAPTOP:
                this.btClass = "COMPUTER_LAPTOP".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA:
                this.btClass = "COMPUTER_PALM_SIZE_PC_PDA".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.COMPUTER_SERVER:
                this.btClass = "COMPUTER_SERVER".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.COMPUTER_UNCATEGORIZED:
                this.btClass = "COMPUTER_UNCATEGORIZED".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.COMPUTER_WEARABLE:
                this.btClass = "COMPUTER_WEARABLE".toLowerCase().replace('_', ' ');
                break;
            case BluetoothClass.Device.HEALTH_BLOOD_PRESSURE:
                this.btClass = "HEALTH_BLOOD_PRESSURE".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.HEALTH_DATA_DISPLAY:
                this.btClass = "HEALTH_DATA_DISPLAY".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.HEALTH_GLUCOSE:
                this.btClass = "HEALTH_GLUCOSE".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.HEALTH_PULSE_OXIMETER:
                this.btClass = "HEALTH_PULSE_OXIMETER".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.HEALTH_PULSE_RATE:
                this.btClass = "HEALTH_PULSE_RATE".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.HEALTH_THERMOMETER:
                this.btClass = "HEALTH_THERMOMETER".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.HEALTH_UNCATEGORIZED:
                this.btClass = "HEALTH_UNCATEGORIZED".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.HEALTH_WEIGHING:
                this.btClass = "HEALTH_WEIGHING".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.PHONE_CELLULAR:
                this.btClass = "PHONE_CELLULAR".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.PHONE_CORDLESS:
                this.btClass = "PHONE_CORDLESS".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.PHONE_ISDN:
                this.btClass = "PHONE_ISDN".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY:
                this.btClass = "PHONE_MODEM_OR_GATEWAY".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.PHONE_SMART:
                this.btClass = "PHONE_SMART".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.PHONE_UNCATEGORIZED:
                this.btClass = "PHONE_UNCATEGORIZED".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.TOY_CONTROLLER:
                this.btClass = "TOY_CONTROLLER".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE:
                this.btClass = "TOY_DOLL_ACTION_FIGURE".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.TOY_GAME:
                this.btClass = "TOY_GAME".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.TOY_ROBOT:
                this.btClass = "TOY_ROBOT".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.TOY_UNCATEGORIZED:
                this.btClass = "TOY_UNCATEGORIZED".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.TOY_VEHICLE:
                this.btClass = "TOY_VEHICLE".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.WEARABLE_GLASSES:
                this.btClass = "WEARABLE_GLASSES".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.WEARABLE_HELMET:
                this.btClass = "WEARABLE_HELMET".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.WEARABLE_JACKET:
                this.btClass = "WEARABLE_JACKET".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.WEARABLE_PAGER:
                this.btClass = "WEARABLE_PAGER".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.WEARABLE_UNCATEGORIZED:
                this.btClass = "WEARABLE_UNCATEGORIZED".toLowerCase().replace('_', ' ');
                break;

            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                this.btClass = "WEARABLE_WRIST_WATCH".toLowerCase().replace('_', ' ');
                break;
            default:
                this.btClass = "N/D";
                break;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return mac;
    }

    public short getSignal() {
        return signal;
    }

    public void setSignal(short signal) {
        this.signal = signal;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
    public void setLocation(String location) {
        String[] tests = location.split(",");
        double latitude = Double.parseDouble(tests[0].split("\\(")[1]);
        double longitude = Double.parseDouble(tests[1].replace(")", ""));
        this.location = new LatLng(latitude, longitude);
    }

    public HashMap<String, String> getInfos() {
        HashMap<String, String> infos = new HashMap<>();
        infos.put("Name", name);
        infos.put("MAC", mac);
        infos.put("Location", location.toString());
        infos.put("Signal", signal + " dBm");
        infos.put("Class", btClass);
        return infos;
    }

}
