package com.bluetooth.googlemap.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.bluetooth.googlemap.R;
import com.bluetooth.googlemap.manager.Manager;
import com.bluetooth.googlemap.modals.Device;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.robertsimoes.shareable.Shareable;

import java.util.ArrayList;
import java.util.List;


public class MapController implements RoutingListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    private Context ctx;
    private LatLng p1, p2;

    private String TAG = "MPC";

    public MapController(GoogleMap map, Context ctx) {
        this.map = map;
        this.ctx = ctx;
    }

    public void pinDevice(Device d) {
        StringBuilder sb = new StringBuilder();
        for (String s : d.getInfos().keySet()) {
            sb.append(s).append(": ").append(d.getInfos().get(s)).append("\n");
        }
        this.map.addMarker(new MarkerOptions()
                .position(d.getLocation())
                .title(d.getName())
                .snippet(sb.toString()));
    }

    public void moveCamera(LatLng position) {
        this.map.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    public void drawRouteOnMap(LatLng p1, LatLng p2) {
        this.map.clear();
        this.p1 = p1;
        this.p2 = p2;

        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.WALKING)
                .waypoints(p1, p2)
                .withListener(this)
                .key(ctx.getResources().getString(R.string.google_maps_key))
                .build();

        routing.execute();
    }

    public void pinWithMultipleDevice(List<Device> devices) {

        if (devices.size() == 0) return;
        StringBuilder sb = new StringBuilder();
        LatLng position = devices.get(0).getLocation();
        for (Device d : devices) {
            sb.append("----------------------\n");
            for (String s : d.getInfos().keySet()) {
                sb.append(s).append(": ").append(d.getInfos().get(s)).append("\n");
            }
        }
        MarkerOptions options = new MarkerOptions()
                .position(position)
                .title("Device present")
                .snippet(sb.toString());

        this.map.addMarker(options);
        this.map.setOnMarkerClickListener(this);

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Log.d(TAG, "onRoutingFailure");
        Log.e(TAG, e.getMessage(), e);
    }

    @Override
    public void onRoutingStart() {
        Log.d(TAG, "onRoutingStart");
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        Log.d(TAG, "onRoutingSuccess");

        List<Polyline> polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.width(10 + i * 3);
            polyOptions.color(Color.BLUE);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);
        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(this.p1);
        map.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(this.p2);
        map.addMarker(options);
    }

    @Override
    public void onRoutingCancelled() {
        Log.d(TAG, "onRoutingCancelled");
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx)
                .setCancelable(true)
                .setMessage(marker.getSnippet())
                .setTitle(marker.getTitle());

        final AlertDialog alertDialog = builder.create();

        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "Direction",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawRouteOnMap(
                                Manager.getInstance().getCurrentPosition(),
                                marker.getPosition()
                        );
                        alertDialog.dismiss();
                    }
                }
        );

        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, "Share",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Shareable shareInstance = new Shareable.Builder(ctx)
                                .message(marker.getSnippet())
                                .socialChannel(Shareable.Builder.ANY)
                                .build();
                        shareInstance.share();

                        alertDialog.dismiss();
                    }
                }
        );

        alertDialog.show();

        return true;
    }
}
