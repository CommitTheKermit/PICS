package com.example.googlemapusingmanual.Tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.example.googlemapusingmanual.Utils.PreferenceManager;
import com.example.googlemapusingmanual.R;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class achievementTab extends Fragment {

    private static final String TAG = "MyActivity";
    private LinearLayout linear1;
    private ArrayList<CheckBox> checkBoxArrayList;
    private ArrayList<ImageView> imageViewArrayList;
    private ArrayList<TextView> textViewArrayList;
    private ArrayList<String> achieve;

    private int[] textureArrayWin = {
            R.drawable.water,
            R.drawable.walk,
            R.drawable.run,
            R.drawable.bicycle,
            R.drawable.push_up,
            R.drawable.sit_up,
            R.drawable.game
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        fileSys file = new fileSys();

        View rootView = inflater.inflate(R.layout.fragment_achievement_tab, container, false);
        linear1 = (LinearLayout) rootView.findViewById(R.id.linearAchievementMain);

        checkBoxArrayList = new ArrayList<CheckBox>(20);
        imageViewArrayList = new ArrayList<ImageView>(20);
        textViewArrayList = new ArrayList<TextView>(20);
        achieve = new ArrayList<String>(20);

        //수동으로 업적 넣음
        achieve.add("물 한 잔 마시기"); achieve.add("물 세 잔 마시기"); achieve.add("물 네 잔 마시기");
        achieve.add("1km 걷기"); achieve.add("10km 걷기"); achieve.add("100km 걷기");
        achieve.add("1km 달리기"); achieve.add("10km 달리기"); achieve.add("100km 달리기");

        achieve.add("1km 자전거타기"); achieve.add("10km 자전거타기"); achieve.add("100km 자전거타기");
        achieve.add("10번 팔굽혀펴기"); achieve.add("100번 팔굽혀펴기"); achieve.add("200번 팔굽혀펴기");
        achieve.add("10번 윗몸일으키기"); achieve.add("100번 윗몸일으키기"); achieve.add("200번 윗몸일으키기");

        achieve.add("1시간 게임하기"); achieve.add("1만 시간 게임하기");
        //끝

        for(int count=0; count<20; count++) {
            boolean checkPreference = PreferenceManager.getBoolean(getActivity(), "chk"+(count));
            //CheckBox 추가 부분
            CheckBox checkView = new CheckBox(getActivity().getApplicationContext());

            checkView.setId(count);
            checkView.setText("업적"+(count+1));
            checkView.setX(200);
            checkView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20); // textSize
            checkView.setGravity(Gravity.LEFT);
            checkView.setChecked(checkPreference);
            linear1.addView(checkView);
            checkBoxArrayList.add(checkView);

            //ImageView 추가 부분
            ImageView imgView = new ImageView(getActivity().getApplicationContext());

            imgView.setId(count);
            imgView.setImageResource(textureArrayWin[count/3]);
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
            txtView.setText(achieve.get(count));

            linear1.addView(txtView);
            textViewArrayList.add(txtView);

            //클릭 이벤트 추가 부분
            checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = checkView.getId();

                    if(checkView.isChecked()) {
                        PreferenceManager.setBoolean(getActivity(), "chk"+(id), true);

                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 a HH시 mm분 ss초");
                        String getTime = sdf.format(date);

                        fileWrite(getTime+"\n"+achieve.get(id)+" 업적을 완료했다.\n", id);
                    }
                }
            });
            // 파일

        }
        return rootView;
    }
    public void fileWrite(String text, int id){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(
                    getActivity().getFilesDir()+"sw"+id+".txt", true));
            bw.write(text);
            bw.newLine();
            bw.close();
            Toast.makeText(getActivity(), (id+1)+"번 업적 저장완료", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}