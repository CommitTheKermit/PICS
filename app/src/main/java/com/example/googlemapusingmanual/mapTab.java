package com.example.googlemapusingmanual;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.google.android.gms.maps.MapsInitializer.Renderer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;

import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mapTab} factory method to
 * create an instance of this fragment.
 */
public class mapTab extends Fragment implements
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback,
    OnMapsSdkInitializedCallback{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int NOT_RUNNING_RECORD_STATE = 2;
    private static final int RUNNING_RECORD_STATE = 3;

    private static final int RECORD_BUTTON_PRESSED = 4;
    private static final int PAUSE_BUTTON_PRESSED = 5;

    private boolean permissionDenied = false;

    private GoogleMap map;
    private UiSettings mUiSettings;

    Button btnPause, btnStart, btnStop;
    private TextView txtTotalDistance, txtMeasuredSpeed, txtAvgSpeed;
    private static Handler recordHandler ;
    private static Handler weatherHandler ;
    Chronometer chronoElapsedTime;
    boolean flagTime;
    long[] timeArray = {0, 0, 0 ,0};

    Thread statisticsThread;
    Thread weatherDisplayThread;
    boolean pressed = false;
    boolean running = false;
    boolean firstWeatherApi = true;
    long currentTime;
    static float avgSpeed;
    Spinner spinnerExercise;

    double outerCurrentLat = 0;
    double outerCurrentLon = 0;


    ArrayList<LatLng> latLngList = new ArrayList<LatLng>();
    ArrayList<Float> speedList = new ArrayList();

    ArrayList<LatLng> pauseLatLngList = new ArrayList<LatLng>();
    ArrayList<Float> pauseSpeedList = new ArrayList();

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public mapTab() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment mapTab.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static mapTab newInstance(String param1, String param2) {
//        mapTab fragment = new mapTab();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
    MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_tab, container, false);

        /*Fragment내에서는 mapView로 지도를 실행*/
        mapView = (MapView)rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        chronoElapsedTime = (Chronometer)rootView.findViewById(R.id.chronoElapsedTime);

        txtTotalDistance = (TextView)rootView.findViewById(R.id.txtTotalDistance);
        txtMeasuredSpeed = (TextView)rootView.findViewById(R.id.txtMeasuredSpeed);
        txtAvgSpeed = (TextView)rootView.findViewById(R.id.txtAvgSpeed);

        btnPause = (Button) rootView.findViewById(R.id.btnPause);
        btnStart = (Button) rootView.findViewById(R.id.btnStart);
        btnStop = (Button) rootView.findViewById(R.id.btnStop);

        recordHandler = new Handler();
        weatherHandler = new Handler();

        // 핸들러로 전달할 runnable 객체. 수신 스레드 실행.


//        currentLocButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            double currentLat = location.getLatitude();
//            double currentLon = location.getLongitude();
//            Toast.makeText(getActivity().getApplicationContext(), "위도:" + currentLat +"\n경도" + currentLon, Toast.LENGTH_LONG).show();
//        }
//    });
        // Inflate the layout for this fragment
        return rootView;
}

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.83066595547154 ,128.75450499202682), 15));
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

        class Weathering implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {

                        if(firstWeatherApi != true){
                            Thread.sleep(300000);// 날씨 딜레이 변수로 설정할것
                        }
                        else{
                            firstWeatherApi = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace() ;
                    }
                    NetworkTask networkTask = new NetworkTask();
                    networkTask.execute();
                }
            }
        }

        Weathering weathering = new Weathering();
        weatherDisplayThread = new Thread(weathering);
        weatherDisplayThread.start();

        final Runnable onRecord = new Runnable() {
            @Override
            public void run() {
                if(running == false){
                    if(pauseLatLngList.size() < 2){
                        return;
                    }

                    Location beforeLastLoc = new Location("");
                    Location lastLoc = new Location("");

                    beforeLastLoc.setLatitude(pauseLatLngList.get(pauseLatLngList.size() - 2).latitude);
                    beforeLastLoc.setLongitude(pauseLatLngList.get(pauseLatLngList.size() - 2).longitude);

                    lastLoc.setLatitude(pauseLatLngList.get(pauseLatLngList.size() - 1).latitude);
                    lastLoc.setLongitude(pauseLatLngList.get(pauseLatLngList.size() - 1).longitude);

                    float secToHour = 0;
                    secToHour = (float) ((float)(timeArray[3] - timeArray[2]) / 3600.0);
                    float speed = lastLoc.distanceTo(beforeLastLoc) / 1000 / secToHour;
                    speed = Math.round(speed);
                    if(speed > 100 || speed < 0 )
                        speed = 0;

                    Log.d("DEBUG","secToHour   " + secToHour);
                    Log.d("DEBUG","speed   " + speed + "km/h");

                    if(pauseSpeedList.size() > 3){
                        pauseSpeedList.remove(0);
                    }
                    pauseSpeedList.add(speed);
                    float sum = 0;
                    for(float eachSpeed : pauseSpeedList){
                        sum += eachSpeed;
                    }
                    avgSpeed = sum / pauseSpeedList.size();
                    //멈춤 감지에서 다시 움직인다 판정
                    //사람의 평균 보행시속은 4.8km/h
                    if(avgSpeed > 2){
                        chronoElapsedTime.setBase(SystemClock.elapsedRealtime() - currentTime);
                        chronoElapsedTime.start();
                        running = true;
                    }
                }
                else{
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
                    avgSpeed = sum / speedList.size();
                    txtAvgSpeed.setText(avgSpeed + "km/h");

                    float speedThreshold = 0;
                    if(speedList.size() < 4){
                        return;
                    }
                    else{
                        float tempSum = 0;
                        for(int j = speedList.size() - 1; j > speedList.size() - 4; j--){
                            tempSum += speedList.get(j);
                        }
                        speedThreshold = tempSum / 3;
                    }

                    //멈춤 감지
                    if(speedThreshold < 2){
                        running = false;
//                    statisticsThread.interrupt();
                        chronoElapsedTime.stop();
                        currentTime = SystemClock.elapsedRealtime() - chronoElapsedTime.getBase();
                    }

                }



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

        class Running implements Runnable {
            @Override
            public void run() {
                while (true && running == true) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace() ;
                    }
                    recordHandler.post(onRecord) ;
                }
            }
        }


//        markMarkerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//
//                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                double currentLat = location.getLatitude();
//                double currentLon = location.getLongitude();
//
//                LatLng currentLocMarker = new LatLng(currentLat,currentLon);
//                map.addMarker(new MarkerOptions().position(currentLocMarker));
//
//                latLngList.add(currentLocMarker);
//
//            }
//        });

//        drawLineBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                for(int i = 0; i < locsList.size(); i++)
////                Log.println((String)locsList.get(i));
////                for(int i = 0; i < locsList.size(); i++){
////                    polylineOptions = new PolylineOptions().add((LatLng) locsList.get(i));
////                }
//
//                if(latLngList.size() > 1){
//                    Polyline polyline = map.addPolyline(new PolylineOptions().addAll(latLngList)
//                            .width(7)
//                            .color(Color.RED));
//                }
//                else{
//                    Toast.makeText(getActivity().getApplicationContext(),"두개 이상의 마커 필요",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//            }
//        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pressed == true){
                    running = false;
//                    statisticsThread.interrupt();
                    chronoElapsedTime.stop();
                    currentTime = SystemClock.elapsedRealtime() - chronoElapsedTime.getBase();
                }
                else return;
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pressed == true && running == false){
                    chronoElapsedTime.setBase(SystemClock.elapsedRealtime() - currentTime);
                    chronoElapsedTime.start();
                    running = true;
                }
                else{
                    chronoElapsedTime.setBase(SystemClock.elapsedRealtime());
                    chronoElapsedTime.start();
                }

                pressed = true;
                running = true;
                Running nr = new Running();
                statisticsThread = new Thread(nr);
                statisticsThread.start();

                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                //gps기반 + 네트워크 기반 gps 업데이트 시작
                //mintime 업데이트 최소 시간
                //mindistance 업데이트 최소 거리 둘 다 초과하여야 gpsLocationListener 작동함.
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        3000,
                        5,
                        gpsLocationListener);

//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                        5000,
//                        5,
//                        gpsLocationListener);

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronoElapsedTime.stop();
                chronoElapsedTime.setBase(SystemClock.elapsedRealtime());
                txtTotalDistance.setText("0.00km");
                txtMeasuredSpeed.setText("0km/s");
                txtAvgSpeed.setText("0km/s");
                latLngList.clear();
                map.clear();
                statisticsThread.interrupt();
                pressed = false;
                running = false;

            }
        });

    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
//            String provider = location.getProvider();  // 위치정보
//            double longitude = location.getLongitude(); // 위도
//            double latitude = location.getLatitude(); // 경도
//            double altitude = location.getAltitude(); // 고도

            double currentLat = location.getLatitude();
            double currentLon = location.getLongitude();

            outerCurrentLat = currentLat;
            outerCurrentLon = currentLon;

            if( running == false){
                LatLng currentLocMarker = new LatLng(currentLat,currentLon);
                map.addMarker(new MarkerOptions().position(currentLocMarker));

                pauseLatLngList.add(currentLocMarker);

                timeArray[2] = timeArray[3];
                timeArray[3] = (SystemClock.elapsedRealtime()
                        - chronoElapsedTime.getBase()) / 1000;

                return;
            }

            LatLng currentLocMarker = new LatLng(currentLat,currentLon);
            map.addMarker(new MarkerOptions().position(currentLocMarker));

            latLngList.add(currentLocMarker);

            if(latLngList.size() > 1){
                Polyline polyline = map.addPolyline(new PolylineOptions().addAll(latLngList)
                        .width(10)
                        .color(Color.RED));
            }

            timeArray[0] = timeArray[1];
            timeArray[1] = (SystemClock.elapsedRealtime()
                    - chronoElapsedTime.getBase()) / 1000;
            Log.d("DEBUG","timeArray   " + timeArray[1]);
//
//            txtResult.setText("위치정보 : " + provider + "\n" + "위도 : " + latitude +
//                    "\n" + "경도 : " + longitude +
//                    "\n" + "고도 : " + altitude +
//                    "\n마커 개수" + locsList.size());

            Log.d("DEBUG","마커 개수   " + latLngList.size());

        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity().getApplicationContext(), "현재 내 위치로 이동", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity().getApplicationContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
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
    public void onMapsSdkInitialized(@NonNull Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDemo", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDemo", "The legacy version of the renderer is used.");
                break;
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

//    @Override
//    protected void onResumeFragments() {
//        super.onResumeFragments();
//        if (permissionDenied) {
//            // Permission was not granted, display error dialog.
//            showMissingPermissionError();
//            permissionDenied = false;
//        }
//    }
//
//    /**
//     * Displays a dialog with error message explaining that the location permission is missing.
//     */
//    private void showMissingPermissionError() {
//        PermissionUtils.PermissionDeniedDialog
//                .newInstance(true).show(getSupportFragmentManager(), "dialog");
//    }
}