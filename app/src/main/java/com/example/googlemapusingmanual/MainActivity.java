package com.example.googlemapusingmanual;
//
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     *
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//}

import android.Manifest.permission;
import android.annotation.SuppressLint;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    Button btnLogo, btnNotification, btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);

//        btnLogo = (Button) findViewById(R.id.btnLogo);
//        btnNotification = (Button) findViewById(R.id.btnNotification);
//        btnMenu = (Button) findViewById(R.id.btnMenu);

//        btnLogo.bringToFront();
//        btnNotification.bringToFront();
//        btnMenu.bringToFront();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide(); //상단 바 없애기


//        gpsProviderButton = (Button) findViewById(R.id.gpsProvider);
//        gpsProviderButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//                if (ActivityCompat.checkSelfPermission(MainActivity.this, permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                        5000,
//                        5,
//                        gpsLocationListener);
//
//            }
//        });
//
//        netProviderButton = (Button) findViewById(R.id.netProvider);
//        netProviderButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (ActivityCompat.checkSelfPermission(MainActivity.this, permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//
//
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                        5000,
//                        5,
//                        gpsLocationListener);
//
//            }
//        });
        bottomNavigation.getMenu().getItem(2).setChecked(true);
        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new mapTab()).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.iconAchievement:
                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new achievementTab()).commit();
                        break;
                    case R.id.iconRanking:
                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new rankingTab()).commit();
                        break;
                    case R.id.iconMap:
                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new mapTab()).commit();
                        break;
                    case R.id.iconWeight:
                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new weightTab()).commit();
                        break;
                    case R.id.iconEtc:
                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new etcTab()).commit();
                        break;
                }

                Log.d("kermit", "itemId :" + item.getItemId());
                return true;
            }
        });
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle presses on the action bar items
//        switch (item.getItemId()) {
//            case R.id.action_btn1:
//                playBtn();
//                return true;
//            case R.id.action_btn2:
//                stopBtn();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
