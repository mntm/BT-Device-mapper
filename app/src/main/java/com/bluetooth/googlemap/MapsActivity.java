package com.bluetooth.googlemap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bluetooth.googlemap.helpers.DatabaseHelper;
import com.bluetooth.googlemap.manager.ColorManager;
import com.bluetooth.googlemap.manager.Manager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Observable;
import java.util.Observer;

public class MapsActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, Observer {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static Context contextOfApplication;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private RecyclerView recyclerView;
    private DeviceAdapter deviceAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationManager lm;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private DatabaseHelper databaseHelper = new DatabaseHelper(this);

    private ColorManager colorManager = ColorManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextOfApplication = this;

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Manager.getInstance().setActivity(this);
        Manager.getInstance().addObserver(this);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Manager.getInstance().setLocationManager(lm);

        initView();

    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.left_open, R.string.left_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = findViewById(R.id.recycler_view);
        deviceAdapter = new DeviceAdapter();
        recyclerView.setAdapter(deviceAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.swap_theme:
                swapTheme();
                break;
            case R.id.favorites:
                showFavorites();
                break;

        }
        return true;
    }

    private void swapTheme() {
        Toast.makeText(this, "Theme as been swapped !", Toast.LENGTH_LONG).show();
        if (colorManager.isThemeDark) {
            changeColor(colorManager.BRIGHT_PRIMARY_DARK, colorManager.BRIGHT_BACKGROUND, colorManager.BRIGHT_PRIMARY, colorManager.BRIGHT_TEXT);
            colorManager.isThemeDark = false;
        } else {
            changeColor(colorManager.DARK_PRIMARY_DARK, colorManager.DARK_BACKGROUND, colorManager.DARK_PRIMARY, colorManager.DARK_TEXT);
            colorManager.isThemeDark = true;
        }
    }

    private void changeColor(int primaryDarkColor, int backgroundColor, int primaryColor, int textColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(primaryDarkColor);

        getWindow().setBackgroundDrawable(new ColorDrawable(backgroundColor));
        recyclerView.setBackground(new ColorDrawable(backgroundColor));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(primaryColor));

        for (int i = 0; i < this.deviceAdapter.getItemCount(); i++) {
            DeviceAdapter.DeviceHolder vh = (DeviceAdapter.DeviceHolder) recyclerView.findViewHolderForAdapterPosition(i);
            vh.itemView.setBackgroundColor(backgroundColor);
            vh.setTextColor(textColor);
        }
    }

    private void showFavorites() {
        View inflate = getLayoutInflater().inflate(R.layout.favorites_list, null);
        RecyclerView recycler = inflate.findViewById(R.id.recycler_view2);
        AlertDialog.Builder builder;
        if (colorManager.isThemeDark) {
            builder = new AlertDialog.Builder(MapsActivity.this, R.style.AlertDialogStyleDark);
        } else {
            builder = new AlertDialog.Builder(MapsActivity.this, R.style.AlertDialogStyleLight);
        }
        builder.setTitle("Favorites")
        .setCancelable(true)
        .setView(inflate)
        .setNegativeButton(android.R.string.ok, null);


        AlertDialog dialog = builder.create();

        DeviceAdapter deviceAdapter1 = new DeviceAdapter();
        deviceAdapter1.setDeviceList(Manager.getInstance().getFavorites());
        recycler.setAdapter(deviceAdapter1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        //FIXME
        //recycler.setBackgroundColor(colorManager.DARK_BACKGROUND);


        dialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Manager.getInstance().detachBluetooth(this);
        Manager.getInstance().detachLocation();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        Manager.getInstance().setMap(mMap, this);

        LatLng mylocation = Manager.getInstance().getCurrentPosition();

        if ((mylocation.latitude == 0.0) && (mylocation.longitude == 0.0)) {
            Toast.makeText(this, "Location Not found", Toast.LENGTH_LONG).show();
        }

        // Add a marker in current location
        mMap.addMarker(new MarkerOptions().position(mylocation).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        float zoomLevel = (float) 17.0; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, zoomLevel));

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            if (mMap != null) {
                // Access to the location has been granted to the app.
                mMap.setMyLocationEnabled(true);
            }

            Manager.getInstance().handleLocation(this);

            if (Manager.getInstance().handleBluetooth(this)) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).

        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onRefresh() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Manager.getInstance().performScan()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Manager.getInstance().performScan()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Manager.getInstance().performScan()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        this.deviceAdapter.setDeviceList(Manager.getInstance().getDevices());
    }
}
