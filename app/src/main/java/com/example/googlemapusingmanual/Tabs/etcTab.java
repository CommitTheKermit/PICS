package com.example.googlemapusingmanual.Tabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.googlemapusingmanual.Activities.LoginActivity;
import com.example.googlemapusingmanual.Activities.MainActivity;
import com.example.googlemapusingmanual.R;


public class etcTab extends Fragment {

    //    private FirebaseAuth mAuth;
    Button btnWithdrawal, btnWeatherDelay, btnLogout, btnMainScreenSet;
    Switch pushSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_etc_tab, container, false);

        btnWithdrawal = (Button) rootView.findViewById(R.id.btnWithdrawal);
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        });

//        boolean pushSetting = PreferenceManager.getBoolean(getActivity(), "pushSwitch");
        pushSwitch = (Switch) rootView.findViewById(R.id.pushSwitch);
        pushSwitch.setChecked(MainActivity.pushState);
        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                if(pushSwitch.isChecked()){
                    MainActivity.pushState = true;
                    builder.setMessage("푸쉬알림이 활성화 되었습니다.");
                }
                else{
                    MainActivity.pushState = false;
                    builder.setMessage("푸쉬알림이 비활성화 되었습니다.");
                }
                builder.create().show();
            }
        });

        btnMainScreenSet = (Button) rootView.findViewById(R.id.btnMainScreenSet);
        btnMainScreenSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] mainScreenStrings = {"업적", "랭킹", "지도", "체중 기록", "기타"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("메인 화면 설정");
                builder.setItems(mainScreenStrings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.mainScreenID = i;
                    }
                });
                builder.create().show();
            }
        });

        btnWeatherDelay = (Button) rootView.findViewById(R.id.btnWeatherDelay);
        btnWeatherDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDelaySetting();
            }
        });

        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("로그 아웃");
                builder.setMessage("로그 아웃하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                });
                builder.setNegativeButton("아니오", null);

                builder.create().show();
            }
        });
        return rootView;
    }

    public void withdrawal() {
        // TODO: 회원 탈퇴 기능 추가

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("회원 탈퇴");
        builder.setMessage("회원 탈퇴가 되었습니다.");
        builder.setPositiveButton("확인", null);
        builder.create().show();
        getActivity().finish();
    }

    public void weatherDelaySetting() {
        String[] weatherDelayStrings = {"15분", "30분", "45분"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("날씨 정보 딜레이 설정");

        builder.setItems(weatherDelayStrings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setDelay(i + 1); //15분, 30분, 45분 간결하게.
            }
        });
        builder.create().show();
    }

    public void setDelay(int t){
        // TODO: 날씨 정보 딜레이 설정 로직이 추가되면 여기에 추가
        mapTab.weatherDelay = t;

    }

    public void logout() {
        // TODO: 로그 아웃 기능 추가 현재는 알림뿐


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("로그 아웃");
        builder.setMessage("로그 아웃 되었습니다.");
        builder.setPositiveButton("확인", null);
        builder.create().show();
        getActivity().finish();
    }
}