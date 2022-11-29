package com.example.googlemapusingmanual;

import android.app.NotificationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class achievementTab extends Fragment {

    private static final String TAG = "MyActivity";
    private LinearLayout linear1;
    private ArrayList<CheckBox> checkBoxArrayList;
    private ArrayList<ImageView> imageViewArrayList;
    private ArrayList<TextView> textViewArrayList;
    public ArrayList<String> textList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_achievement_tab, container, false);
        linear1 = (LinearLayout) rootView.findViewById(R.id.linearAchievementMain);

        checkBoxArrayList = new ArrayList<CheckBox>(20);
        imageViewArrayList = new ArrayList<ImageView>(20);
        textViewArrayList = new ArrayList<TextView>(20);
        textList = new ArrayList<String>(20);

        for(int count=0; count<20; count++) {
            //CheckBox 추가 부분
            CheckBox checkView = new CheckBox(getActivity().getApplicationContext());

            checkView.setId(count);
            checkView.setText("업적"+(count+1));
            checkView.setX(200);
            checkView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20); // textSize
            checkView.setGravity(Gravity.LEFT);
            linear1.addView(checkView);
            checkBoxArrayList.add(checkView);

            //ImageView 추가 부분
            ImageView imgView = new ImageView(getActivity().getApplicationContext());

            imgView.setId(count);
            imgView.setImageResource(R.drawable.img1);
            imgView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            imgView.setY(-100);
            linear1.addView(imgView);
            imageViewArrayList.add(imgView);

            //TextView 추가 부분
            TextView txtView = new TextView(getActivity().getApplicationContext());
            txtView.setId(count);
            txtView.setX(210);
            txtView.setY(-200);
            txtView.setTextSize(20);
            txtView.setText("업적을 아직 정하지 않았습니다." + (count+1));

            linear1.addView(txtView);
            textViewArrayList.add(txtView);

            //클릭 이벤트 추가 부분
            checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkView.isChecked()) {
                        int id = checkView.getId();
                        textList.add("업적 "+(id+1)+"번이 완료되었습니다.");

                        Toast.makeText(getActivity().getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                        txtView.setText("업적이 완료되었음. 1초 후 돌아감");

                        // 딜레이
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkView.setChecked(false);
                                txtView.setText("업적 완료후 초기화된 업적 " + (id+1));
                            }
                        }, 1000);
                    }
                }
            });

        }
        return rootView;
    }
}