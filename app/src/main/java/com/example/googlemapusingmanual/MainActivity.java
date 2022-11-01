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
import androidx.loader.app.LoaderManager;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean permissionDenied = false;

    private GoogleMap map;

//    Button gpsProviderButton;
//    Button netProviderButton;
    Button currentLocButton, markMarkerBtn, drawLineBtn;
    Button btnGpsStart, btnInitiate;
    private TextView txtResult, txtTotalDistance, txtMeasuredSpeed, txtAvgSpeed;
    private static Handler mHandler ;
    Chronometer chronoElapsedTime;
    boolean flagTime;
    long[] timeArray = {0, 0};


    ArrayList<LatLng> latLngList = new ArrayList<LatLng>();
    ArrayList<Float> speedList = new ArrayList();

//    List

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnGpsStart = (Button)findViewById(R.id.btnGpsStart);
        currentLocButton = (Button) findViewById(R.id.currentLoc);
        chronoElapsedTime = (Chronometer)findViewById(R.id.chronoElapsedTime);
        btnInitiate = (Button)findViewById(R.id.btnInitiate);

        txtTotalDistance = (TextView)findViewById(R.id.txtTotalDistance);
        txtMeasuredSpeed = (TextView)findViewById(R.id.txtMeasuredSpeed);
        txtAvgSpeed = (TextView)findViewById(R.id.txtAvgSpeed);

        txtResult = (TextView) findViewById(R.id.txtResult);
        //        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide(); //상단 바 없애기
//
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
        mHandler = new Handler() ;



        // 핸들러로 전달할 runnable 객체. 수신 스레드 실행.
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if(latLngList.size() < 2){
                    return;
                }

                Location previousLoc = new Location("");

                previousLoc.setLatitude(latLngList.get(0).latitude);
                previousLoc.setLongitude(latLngList.get(0).longitude);

                long distanceMeter = 0;

                for(int i = 1; i <latLngList.size();i++){
                    Location tempLoc = new Location("");
                    tempLoc.setLatitude(latLngList.get(i).latitude);
                    tempLoc.setLongitude(latLngList.get(i).longitude);
                    distanceMeter += Math.round(tempLoc.distanceTo(previousLoc));

                    previousLoc = tempLoc;
                }
                float distanceKilometer =  (float) (distanceMeter / 1000.0);
                txtTotalDistance.setText(distanceKilometer + "km");
                Log.d("DEBUG","distanceM   " + distanceMeter);
                Log.d("DEBUG","distanceKM   " + distanceKilometer);

                Location beforeLastLoc = new Location("");
                Location lastLoc = new Location("");

                beforeLastLoc.setLatitude(latLngList.get(latLngList.size() - 2).latitude);
                beforeLastLoc.setLongitude(latLngList.get(latLngList.size() - 2).longitude);

                lastLoc.setLatitude(latLngList.get(latLngList.size() - 1).latitude);
                lastLoc.setLongitude(latLngList.get(latLngList.size() - 1).longitude);

                float secToHour = 0;
                secToHour = (float) ((float)(timeArray[1] - timeArray[0]) / 3600.0);
                float speed = lastLoc.distanceTo(beforeLastLoc) / 1000 / secToHour;
                speed = Math.round(speed);
                if(speed > 100 || speed < 0 )
                    speed = 0;
                txtMeasuredSpeed.setText( speed+ "km/h");
                Log.d("DEBUG","secToHour   " + secToHour);
                Log.d("DEBUG","speed   " + speed + "km/h");

                speedList.add(speed);
                float sum = 0;
                for(float item : speedList){
                    sum += item;
                }
                sum = sum / speedList.size();
                txtAvgSpeed.setText(sum + "km/h");

//                float elapsedMillis =  (SystemClock.elapsedRealtime()
//                        - chronoElapsedTime.getBase());
//                speed = Math.round(distanceKilometer / (elapsedMillis / 1000.0)  * (1/3600));
//                if(speed > 100 || speed < 0 )
//                    speed = 0;
//                txtAvgSpeed.setText( speed+ "km/h");
//                Log.d("DEBUG","elapsedMillis   " + elapsedMillis + "milsec");
//                Log.d("DEBUG","avgSpeed   " + speed + "km/h");

            }
        } ;


        class NewRunnable implements Runnable {
            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace() ;
                    }

                    mHandler.post(runnable) ;
                }
            }
        }

        btnGpsStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronoElapsedTime.setBase(SystemClock.elapsedRealtime());
                NewRunnable nr = new NewRunnable();
                Thread statisticsThread = new Thread(nr);
                statisticsThread.start();

                chronoElapsedTime.start();
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                //gps기반 + 네트워크 기반 gps 업데이트 시작
                //mintime 업데이트 최소 시간
                //mindistance 업데이트 최소 거리 둘 다 초과하여야 gpsLocationListener 작동함.
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        5000,
                        10,
                        gpsLocationListener);

//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                        5000,
//                        5,
//                        gpsLocationListener);


            }
        });
        btnInitiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronoElapsedTime.setBase(SystemClock.elapsedRealtime());
                txtTotalDistance.setText("0.00km");
                txtMeasuredSpeed.setText("0km/s");
                txtAvgSpeed.setText("0km/s");
                latLngList.clear();
            }
        });
        currentLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(MainActivity.this, permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double currentLat = location.getLatitude();
                double currentLon = location.getLongitude();
                Toast.makeText(MainActivity.this, "위도:" + currentLat +"\n경도" + currentLon, Toast.LENGTH_LONG).show();
            }
        });

    }


    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
            double altitude = location.getAltitude(); // 고도


            double currentLat = location.getLatitude();
            double currentLon = location.getLongitude();

            LatLng currentLocMarker = new LatLng(currentLat,currentLon);
            map.addMarker(new MarkerOptions().position(currentLocMarker));

            latLngList.add(currentLocMarker);
            timeArray[0] = timeArray[1];
            timeArray[1] = (SystemClock.elapsedRealtime()
                    - chronoElapsedTime.getBase()) / 1000;
            Log.d("DEBUG","timeArray   " + timeArray[1]);
//
//            txtResult.setText("위치정보 : " + provider + "\n" + "위도 : " + latitude +
//                    "\n" + "경도 : " + longitude +
//                    "\n" + "고도 : " + altitude +
//                    "\n마커 개수" + locsList.size());

            txtResult.setText("마커 개수 :" + latLngList.size());
            Log.d("DEBUG","마커 개수   " + latLngList.size());

        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();

        markMarkerBtn = (Button) findViewById(R.id.markMarker);
        markMarkerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(MainActivity.this, permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double currentLat = location.getLatitude();
                double currentLon = location.getLongitude();

                LatLng currentLocMarker = new LatLng(currentLat,currentLon);
                map.addMarker(new MarkerOptions().position(currentLocMarker));

                latLngList.add(currentLocMarker);

            }
        });
        drawLineBtn = (Button) findViewById(R.id.drawLine);
        drawLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for(int i = 0; i < locsList.size(); i++)
//                Log.println((String)locsList.get(i));
//                for(int i = 0; i < locsList.size(); i++){
//                    polylineOptions = new PolylineOptions().add((LatLng) locsList.get(i));
//                }

                if(latLngList.size() > 1){
                    Polyline polyline = map.addPolyline(new PolylineOptions().addAll(latLngList)
                            .width(5)
                            .color(Color.RED));
                }
                else{
                    Toast.makeText(MainActivity.this,"두개 이상의 마커 필요",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
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
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}