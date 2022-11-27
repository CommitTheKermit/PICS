package com.example.googlemapusingmanual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationHistoryActivity extends AppCompatActivity {

    private LinearLayout linear;
    private ArrayList<TextView> textViewArrayList;
    private ArrayList<ImageView> imageViewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);

        linear = findViewById(R.id.linear1);

        textViewArrayList = new ArrayList<TextView>(20);
        imageViewArrayList = new ArrayList<ImageView>(20);

        for(int count=0; count<20; count++) {
            createTextViewWith(count,  210,  200, true);
            createImgView(count, 200, 200, 100);
            createTextViewWith(count, 210, 0, false);
        }
    }

    private void createTextViewWith(int id, int setX, int setY, boolean isDate){
        TextView textView = new TextView(getApplicationContext());
        textView.setId(id);

        //날짜
        if(isDate == true){
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 a HH시 mm분 ss초");
            String getTime = sdf.format(date);
            textView.setText(getTime);
        }
        else{
            textView.setText("알림기록은 나중에 받아오겠음");
        }
        textView.setX(setX);
        textView.setY(setY);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15); // textSize
        textView.setGravity(Gravity.LEFT);
        linear.addView(textView);
        textViewArrayList.add(textView);
    }

    private void createImgView(int id, int width, int height, int setY){
        ImageView imgView = new ImageView(getApplicationContext());

        imgView.setId(id);
        imgView.setImageResource(R.drawable.img1);
        imgView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        imgView.setY(setY);
        linear.addView(imgView);
        imageViewArrayList.add(imgView);
    }

    public void onClickBackButton(View v){
        finish();
    }
}