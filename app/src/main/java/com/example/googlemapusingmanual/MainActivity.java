package com.example.googlemapusingmanual;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import android.Manifest;

import java.util.List;
//import com.example.googlemapusingmanual.PushNotification.*;

public class MainActivity extends AppCompatActivity {

    private PushNotification push = new PushNotification(this);
    private BottomNavigationView bottomNavigation;
    private ImageButton btnLogo, btnNotification, btnMenu ;
    private final String DEFAULT = "DEFAULT";
    static int mainScreenID = 2;
    static boolean pushState;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater mInflater = getMenuInflater();
        if (v == btnMenu){
            mInflater.inflate(R.menu.menu2, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemCycle:
                mapTab.currentExerciseType = "CYCLE";
                break;
            case R.id.itemRunning:
                mapTab.currentExerciseType = "RUNNING";
                break;
            case R.id.itemWalking:
                mapTab.currentExerciseType = "WALKING";
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//// storage permission
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
//                }
//
//                requestPermissions(new String[]
//                        {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
//            }
//        }



        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide(); //상단 바 없애기

        btnLogo = (ImageButton) findViewById(R.id.btnLogo);
        btnNotification = (ImageButton) findViewById(R.id.btnNotification);
        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        registerForContextMenu(btnMenu);

        setFragment(new mapTab());
        bottomNavigation.getMenu().getItem(mainScreenID).setChecked(true);
//        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new mapTab()).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.iconAchievement:
//                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new achievementTab()).commit();
//                        btnMenu.setVisibility(View.INVISIBLE);
//                        break;
//                    case R.id.iconRanking:
//                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new rankingTab()).commit();
//                        btnMenu.setVisibility(View.INVISIBLE);
//                        break;
//                    case R.id.iconMap:
//                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new mapTab()).commit();
//                        btnMenu.setVisibility(View.VISIBLE);
//                        break;
//                    case R.id.iconWeight:
//                        btnMenu.setVisibility(View.INVISIBLE);
//                        break;
//                    case R.id.iconEtc:
//                        btnMenu.setVisibility(View.INVISIBLE);
//                        break;
//                }
                List<Integer> arr = List.of(R.id.iconAchievement, R.id.iconRanking, R.id.iconMap, R.id.iconWeight, R.id.iconEtc);
                changeFragment(arr.indexOf(item.getItemId()));
                return true;

            }
        });
        btnLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(mainScreenID);
            }
        });

        btnNotification = (ImageButton) findViewById(R.id.btnNotification);
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationHistoryActivity.class);
                startActivity(intent);
            }
        });
//        if(pushState == true){
//            createNotificationChannel(DEFAULT, "default channel", NotificationManager.IMPORTANCE_HIGH);
//            createNotification(DEFAULT, -1, "Notification is on", "set on last run");
//        }

    }
    private void changeFragment(int screenId) {
        switch (screenId) {
            case 0:
                setFragment(new achievementTab());
                btnMenu.setVisibility(View.INVISIBLE);
                break;
            case 1: // 랭킹
                setFragment(new rankingTab());
                btnMenu.setVisibility(View.INVISIBLE);
                break;
            case 2: // 지도
                setFragment(new mapTab());
                btnMenu.setVisibility(View.VISIBLE);
                break;
            case 3: // 체중 기록
                setFragment(new weightTab());
                btnMenu.setVisibility(View.INVISIBLE);
                break;
            case 4: // 기타
                setFragment(new etcTab());
                btnMenu.setVisibility(View.INVISIBLE);
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

//    //    Notification
//    public void createNotificationChannel(String channelId, String channelName, int importance){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
//        }
//    }
//
//    public void createNotification(String channelId, int id, String title, String text){
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                .setPriority(NotificationCompat.PRIORITY_HIGH).setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle(title).setContentText(text)
//                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(id, builder.build());
//    }
}
