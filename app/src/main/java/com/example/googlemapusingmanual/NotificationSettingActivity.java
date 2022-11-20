package com.example.googlemapusingmanual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NotificationSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
    }

    public void onClickBackButton(View v){
        Intent intent = new Intent(NotificationSettingActivity.this, EtcTabActivity.class);
        startActivity(intent);
        finish();
    }
}