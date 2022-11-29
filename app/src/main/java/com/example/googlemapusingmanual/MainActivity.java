package com.example.googlemapusingmanual;

import static java.text.DateFormat.DEFAULT;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private final String DEFAULT = "DEFAULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Fragment 관련 파트
        setFragment(new mapTab());

        int mainScreenId = PreferenceManager.getInt(getApplicationContext(), "mainScreen");
        bottomNavigation.getMenu().getItem(mainScreenId).setChecked(true);
        changeFragment(mainScreenId);

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                List<Integer> arr = List.of(R.id.iconAchievement, R.id.iconRanking, R.id.iconMap, R.id.iconWeight, R.id.iconEtc);
                changeFragment(arr.indexOf(item.getItemId()));
                return true;
            }
        });

        //Notification 관련 파트
        createNotificationChannel(DEFAULT, "default channel", NotificationManager.IMPORTANCE_HIGH);
        createNotification(DEFAULT, -1, "Hello", "Welcome to PICS");
    }

    private void changeFragment(int screenId) {
        switch (screenId) {
            case 0:
                setFragment(new achievementTab());
                break;
            case 1: // 랭킹
                break;
            case 2: // 지도
                setFragment(new mapTab());
                break;
            case 3: // 체중 기록
                break;
            case 4: // 기타
                setFragment(new etcTab());
                break;
            default:
                break;
        }
    }

    //프래그먼트 화면 설정 함수.
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameMain, fragment)
                .commit();
    }

    //Notification
    public void createNotificationChannel(String channelId, String channelName, int importance){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    public void createNotification(String channelId, int id, String title, String text){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH).setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title).setContentText(text)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }
}
