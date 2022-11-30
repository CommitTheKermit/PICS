package com.example.googlemapusingmanual;

import static androidx.fragment.app.DialogFragment.STYLE_NORMAL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;

import com.google.android.gms.maps.MapsInitializer.Renderer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;

import org.json.JSONException;

public class mapTab extends Fragment implements
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback,
    OnMapsSdkInitializedCallback{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean permissionDenied = false;

    public mapTab tab;
    public int rainState = -1;

    private GoogleMap map;
    private UiSettings mUiSettings;

    private Button btnPause, btnStart, btnStop;
    private ImageView imgWeatherIcon;
    private TextView txtTotalDistance, txtMeasuredSpeed, txtAvgSpeed;
    private static Handler recordHandler ;
    private Chronometer chronoElapsedTime;
    private long[] timeArray = {0, 0, 0 ,0};

    private Thread statisticsThread;
    private Thread weatherDisplayThread;
    private boolean pressed = false;
    private boolean running = false;
    private boolean firstWeatherApi = true;
    private int gpsCalledCount = 0;
    private long currentTime;
    private static float avgSpeed;
    private float outputDist;

    public static String currentExerciseType = "WALKING";

    private double outerCurrentLat = 0;
    private double outerCurrentLon = 0;

    private Dialog dialogView;


    private ArrayList<LatLng> latLngList = new ArrayList<>();
    private ArrayList<Float> speedList = new ArrayList();
    private ArrayList<LatLng> pauseLatLngList = new ArrayList<>();

    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_tab, container, false);
        tab = this;
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

        imgWeatherIcon = (ImageView) rootView.findViewById(R.id.imgWeatherIcon);

        recordHandler = new Handler();

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

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
                    NetworkTask networkTask = new NetworkTask(tab);
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
                    Toast.makeText(getActivity().getApplicationContext(),"stopped thread " + gpsCalledCount,Toast.LENGTH_SHORT).show();

                    //멈춤 감지에서 다시 움직인다 판정
                    //사람의 평균 보행시속은 4.8km/h
                    if(gpsCalledCount > 2){
                        chronoElapsedTime.setBase(SystemClock.elapsedRealtime() - currentTime);
                        chronoElapsedTime.start();
                        running = true;
                        Toast.makeText(getActivity().getApplicationContext(), "자동 시작 : " + avgSpeed, Toast.LENGTH_SHORT).show();
                        gpsCalledCount = 0;
                    }
                }
                else{
                    gpsCalledCount -= 1;
                    Toast.makeText(getActivity().getApplicationContext(), "count subed " + gpsCalledCount,Toast.LENGTH_SHORT).show();

                    if(latLngList.size() < 2){
                        gpsCalledCount += 1;
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
                    outputDist = distanceKilometer;
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



                    float speedThreshold = 10;
                    if(!(speedList.size() < 4)) {
                        float tempSum = 0;
                        for(int j = speedList.size() - 1; j > speedList.size() - 4; j--){
                            tempSum += speedList.get(j);
                        }
                        speedThreshold = tempSum / 3;
                    }

                    //멈춤 감지
                    if(speedThreshold < 2 || gpsCalledCount < -2){
                        running = false;
                        chronoElapsedTime.stop();
                        currentTime = SystemClock.elapsedRealtime() - chronoElapsedTime.getBase();
                        Toast.makeText(getActivity().getApplicationContext(), "자동 멈춤 : " + avgSpeed, Toast.LENGTH_SHORT).show();
                        gpsCalledCount = 0;
                    }
                }
            }
        };

        class Running implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(4000);
                    } catch (Exception e) {
                        e.printStackTrace() ;
                    }
                    recordHandler.post(onRecord) ;
                }
            }
        }
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pressed == true){
                    running = false;
//                    statisticsThread.interrupt();
                    chronoElapsedTime.stop();
                    currentTime = SystemClock.elapsedRealtime() - chronoElapsedTime.getBase();
                    locationManager.removeUpdates(mapTab.this.gpsLocationListener);
                }
                else return;
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gpsCalledCount = 0;

                if(pressed == true && running == false){
                    chronoElapsedTime.setBase(SystemClock.elapsedRealtime() - currentTime);
                }
                else{
                    chronoElapsedTime.setBase(SystemClock.elapsedRealtime());

                    if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    //gps기반 + 네트워크 기반 gps 업데이트 시작
                    //mintime 업데이트 최소 시간
                    //mindistance 업데이트 최소 거리 둘 다 초과하여야 gpsLocationListener 작동함.
                    locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER,
                            3000,
                            5,
                            gpsLocationListener);
                    chronoElapsedTime.start();
                    pressed = true;
                    running = true;
                    Running nr = new Running();
                    statisticsThread = new Thread(nr);
                    statisticsThread.start();
                }
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
                gpsCalledCount = 0;
                statisticsThread.interrupt();
//                something = outputDist;// 운동 끝내고 거리 전달

                locationManager.removeUpdates(mapTab.this.gpsLocationListener);

                try {

                    // TODO: 2022-11-28  //유저 닉네임 설정
                    String nickname = "someone" + ".txt";
                    InputStream in = null;
                    try {
                        in = getActivity().openFileInput(nickname);
                    }
                    catch (FileNotFoundException e){
                        OutputStreamWriter outputStream = new OutputStreamWriter(getActivity().openFileOutput(
                                nickname,
                                Context.MODE_PRIVATE
                        ));

                        outputStream.write(
                                "<CYCLE>\n" +
                                "</CYCLE>\n"+
                                "<RUNNING>\n" +
                                "</RUNNING>\n" +
                                "<WALKING>\n" +
                                "</WALKING>\n"
                        );

                        outputStream.close();
                        in = getActivity().openFileInput(nickname);
                    }
                    

                    byte[] b = new byte[in.available()];

                    try {
                        in.read(b);
                    } catch (IOException e) {
                        Log.d("kermit",e.getMessage());
                    }
                    in.close();
                    ArrayList<String> lines = new ArrayList<String>();

                    Scanner scanStop = new Scanner(new String(b));
                    while(scanStop.hasNext()){
                        lines.add(scanStop.next()); // 실제읽
                    }

                    FileOutputStream outputStream = getActivity().openFileOutput(
                            nickname,
                            Context.MODE_PRIVATE);

                    String output = "";

                    for(int i = 0; i < lines.size(); i ++){
                        int targetLine = lines.get(i).indexOf("</" + currentExerciseType + ">");
                        if(targetLine != -1){
                            lines.add(i, "<dist>"+ outputDist + "</dist>");
                            i += 1;
                        }
                    }
                    for(int i = 0; i < lines.size(); i ++){
                        output += lines.get(i)+ "\n";
                    }
                    outputStream.write(output.getBytes());
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.

            if(rainState == 0){
                imgWeatherIcon.setImageResource(R.drawable.sunny);
            }
            else if(rainState == 1 || rainState == 2){
                imgWeatherIcon.setImageResource(R.drawable.rainy);
            }
            else if(rainState == 3){
                imgWeatherIcon.setImageResource(R.drawable.snow);
            }


            double currentLat = location.getLatitude();
            double currentLon = location.getLongitude();

            outerCurrentLat = currentLat;
            outerCurrentLon = currentLon;

            gpsCalledCount += 1;
            Toast.makeText(getActivity().getApplicationContext(),"count " + gpsCalledCount, Toast.LENGTH_SHORT).show();

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
}