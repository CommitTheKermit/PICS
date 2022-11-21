package com.example.googlemapusingmanual;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EtcTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_tab);
    }

    public void onClickWithdrawalButton(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EtcTabActivity.this);
        builder.setTitle("회원 탈퇴");
        builder.setMessage("회원 탈퇴하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                withdrawal();
            }
        });
        builder.setNegativeButton("아니오", null);

        builder.create().show();
    }

    public void onClickSetMainScreenButton(View v) {
        setMainScreen();
    }

//    public void onClickSetAlarmButton(View v) {
//        Intent intent = new Intent(EtcTabActivity.this, NotificationSettingActivity.class);
//        startActivity(intent);
//        finish();
//    }

    // etcLogic
    public void setMainScreen() {
        String[] mainScreenStrings = {"업적", "랭킹", "지도", "체중 기록", "기타"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EtcTabActivity.this);
        builder.setTitle("메인 화면 설정");

        builder.setItems(mainScreenStrings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: 메인 화면 설정 로직이 추가되면 여기에 추가
                switch (i) {
                    case 0:
                        // 업적
                        break;
                    default:
                        // ...
                }
            }
        });

        builder.create().show();
    }

    public void withdrawal() {
        // TODO: 회원 탈퇴 기능 추가
        /* if (회원탈퇴 됐으면) {

        } */
        AlertDialog.Builder builder = new AlertDialog.Builder(EtcTabActivity.this);
        builder.setTitle("회원 탈퇴");
        builder.setMessage("회원 탈퇴가 되었습니다.");
        builder.setPositiveButton("확인", null);

        builder.create().show();
    }

    public void onClickWeatherDelayButton(View v){
        weatherDelaySetting();
    }

    public void weatherDelaySetting() {
        String[] mainScreenStrings = {"1분", "5분", "10분"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EtcTabActivity.this);
        builder.setTitle("날씨 정보 딜레이 설정");

        builder.setItems(mainScreenStrings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: 날씨 정보 딜레이 설정 로직이 추가되면 여기에 추가
                switch (i) {
                    case 0:
                        // 업적
                        break;
                    default:
                        // ...
                }
            }
        });

        builder.create().show();
    }
}