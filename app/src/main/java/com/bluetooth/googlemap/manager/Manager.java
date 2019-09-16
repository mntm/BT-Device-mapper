package com.bluetooth.googlemap.manager;

import android.content.DialogInterface;
import android.database.Cursor;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.bluetooth.googlemap.controller.BTController;
import com.bluetooth.googlemap.controller.GPSController;
import com.bluetooth.googlemap.controller.MapController;
import com.bluetooth.googlemap.helpers.DatabaseHelper;
import com.bluetooth.googlemap.modals.Device;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.robertsimoes.shareable.Shareable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Manager extends Observable implements Observer {
    private static final Manager ourInstance = new Manager();

    private GoogleMap gMap;

    private MapController mapController;
    private BTController btController;
    private GPSController gpsController;

    private HashMap<String, Device> devices = new HashMap<>();

    private AppCompatActivity activity;

    private DatabaseHelper databaseHelper;

    private boolean locationHandled = false;

    private Manager() {
        this.gpsController = new GPSController();
        this.btController = new BTController();
        this.gpsController.addObserver(this);
        this.btController.addObserver(this);
    }

    public static Manager getInstance() {
        return ourInstance;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public boolean handleBluetooth(AppCompatActivity activity) {
        if (locationHandled) {
            this.btController.enable(activity);
            return this.performScan();
        }
        return locationHandled;
    }

    public void detachBluetooth(AppCompatActivity activity) {
        if (locationHandled) {
            this.btController.unregisterReceiver(activity);
        }
    }

    public boolean performScan() {
        return (locationHandled) && this.btController.performScan();
    }

    public void handleLocation(AppCompatActivity activity) {
        this.gpsController.enable(activity);
        this.gpsController.startLocationUpdates();
        locationHandled = true;
    }

    public void detachLocation() {
        this.gpsController.stopLocationUpdates();
        locationHandled = false;
    }

    public void setMap(GoogleMap map, AppCompatActivity activity) {
        this.gMap = map;
        this.mapController = new MapController(gMap, activity);
        this.populateMap();
        this.mapController.moveCamera(this.gpsController.getCurrentPosition());
    }

    private void populateMap() {
        HashMap<LatLng, List<Device>> tmp = new HashMap<>();
        for (Device d :
                this.devices.values()) {
            List<Device> devices = tmp.get(d.getLocation());
            if (devices == null) {
                devices = new ArrayList<>();
            }
            devices.add(d);
            tmp.put(d.getLocation(), devices);
        }

        for (List<Device> devices : tmp.values()) {
            this.mapController.pinWithMultipleDevice(devices);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BTController) {
            ArrayList<Device> ds = (ArrayList<Device>) arg;
            if (ds == null) return;
            for (Device d : ds) {
                d.setLocation(this.getCurrentPosition());
                this.devices.put(d.getAddress(), d);
                mapController.moveCamera(d.getLocation());
            }
            mapController.pinWithMultipleDevice(new ArrayList<Device>(this.devices.values()));
        }

        this.setChanged();
        this.notifyObservers();
    }

    public ArrayList<Device> getDevices() {
        return new ArrayList<>(this.devices.values());
    }

    public ArrayList<Device> getFavorites() {
        HashMap<String, Device> favorites_devices = new HashMap<>();
        databaseHelper = new DatabaseHelper(ourInstance.activity);
        Cursor data = databaseHelper.getData();
        while (data.moveToNext()) {
            Device temp = new Device(data.getString(3));
            temp.setBtClass(data.getString(1));
            temp.setName(data.getString(5));
            temp.setSignal(Short.parseShort(data.getString(4)));
            temp.setLocation(data.getString(2));
            favorites_devices.put(data.getString(0), temp);
        }

        return new ArrayList<>(favorites_devices.values());
    }

    public boolean isFavorite(Device device) {
        databaseHelper = new DatabaseHelper(ourInstance.activity);
        Cursor data = databaseHelper.getData();

        while (data.moveToNext()) {
            if (device.getAddress().equals(data.getString(3)))
                return true;
        }

        return false;
    }

    public void addFavorite(Device device) {
        if (!this.isFavorite(device)) {
            databaseHelper.addData(device.getBtClass(),
                    String.valueOf(device.getLocation()),
                    device.getAddress(),
                    String.valueOf(device.getSignal()),
                    device.getName());
        }
    }

    public void removeFavorite(Device device) {
        if (this.isFavorite(device)) {
            databaseHelper.deleteData(device.getAddress());
        }
    }

    public void showDetails(String mac) {

        final Device d = this.devices.get(mac);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        final StringBuilder sb = new StringBuilder();
        if (d != null) {
            for (String s : d.getInfos().keySet()) {
                sb.append(s).append(": ").append(d.getInfos().get(s)).append("\n");
            }
        }

        builder.setCancelable(true);
        builder.setTitle("Details");
        builder.setMessage(sb.toString());

        final AlertDialog alertDialog = builder.create();

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Direction",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Manager.getInstance().drawRouteOnMap(
                                Manager.getInstance().getCurrentPosition(),
                                d.getLocation()
                        );
                        alertDialog.dismiss();
                    }
                }
        );

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Share",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Shareable shareInstance = new Shareable.Builder(activity)
                                .message(sb.toString())
                                .socialChannel(Shareable.Builder.ANY)
                                .build();
                        shareInstance.share();

                        alertDialog.dismiss();
                    }
                }
        );

        if (Manager.getInstance().isFavorite(d)) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Remove Fav",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Manager.getInstance().removeFavorite(d);
                        }
                    }
            );
        } else {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Add to Fav",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Manager.getInstance().addFavorite(d);
                        }
                    }
            );
        }

        alertDialog.show();
    }

    private void drawRouteOnMap(LatLng currentPosition, LatLng location) {
        this.mapController.drawRouteOnMap(currentPosition, location);
    }

    public void setLocationManager(LocationManager lm) {
        this.gpsController.setLocationManager(lm);
    }

    public LatLng getCurrentPosition() {
        return this.gpsController.getCurrentPosition();
    }
}
