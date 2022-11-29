package com.example.googlemapusingmanual;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private ImageButton btnLogo, btnNotification, btnMenu ;

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

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        registerForContextMenu(btnMenu);

        bottomNavigation.getMenu().getItem(2).setChecked(true);
        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new mapTab()).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.iconAchievement:
                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new achievementTab()).commit();
                        btnMenu.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.iconRanking:
                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new rankingTab()).commit();
                        btnMenu.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.iconMap:
                        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, new mapTab()).commit();
                        btnMenu.setVisibility(View.VISIBLE);
                        break;
                    case R.id.iconWeight:
                        btnMenu.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.iconEtc:
                        btnMenu.setVisibility(View.INVISIBLE);
                        break;
                }

                Log.d("kermit", "itemId :" + item.getItemId());
                return true;
            }
        });
    }
}
