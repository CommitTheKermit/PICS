package com.example.googlemapusingmanual.Activities;

import com.example.googlemapusingmanual.R;
import com.example.googlemapusingmanual.Tabs.achievementTab;
import com.example.googlemapusingmanual.Tabs.etcTab;
import com.example.googlemapusingmanual.Tabs.mapTab;
import com.example.googlemapusingmanual.Tabs.traceTab;
import com.example.googlemapusingmanual.Tabs.weightTab;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;
//import com.example.googlemapusingmanual.Utils.PushNotification.*;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNavigation;
    private ImageButton btnLogo, btnNotification, btnMenu ;
    public static int mainScreenID = 2;
    public static boolean pushState;

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

                List<Integer> arr = List.of(R.id.iconAchievement, R.id.iconTrace, R.id.iconMap, R.id.iconWeight, R.id.iconEtc);
                changeFragment(arr.indexOf(item.getItemId()));
                return true;

            }
        });
        btnLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(mainScreenID);
                bottomNavigation.getMenu().getItem(mainScreenID).setChecked(true);

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
    }
    private void changeFragment(int screenId) {
        switch (screenId) {
            case 0:
                setFragment(new achievementTab());
                btnMenu.setVisibility(View.INVISIBLE);
                break;
            case 1: // 랭킹
                setFragment(new traceTab());
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
}
