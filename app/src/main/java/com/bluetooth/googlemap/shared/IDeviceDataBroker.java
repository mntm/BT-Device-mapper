package com.bluetooth.googlemap.shared;

import com.bluetooth.googlemap.modals.Device;

import java.util.ArrayList;

public interface IDeviceDataBroker {
    void writeDevice(Device device);
    ArrayList<Device> getAll();

    ArrayList<Device> getFavorites();

    void addFavorite(Device device);

    void removeFavorite(Device device);
}
