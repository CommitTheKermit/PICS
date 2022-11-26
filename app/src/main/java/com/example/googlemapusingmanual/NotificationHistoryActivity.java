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

    private LinearLayout linear1;
    private ArrayList<TextView> textViewArrayList1;
    private ArrayList<ImageView> imageViewArrayList;
    private ArrayList<TextView> textViewArrayList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);

        linear1 = findViewById(R.id.linear1);

        textViewArrayList1 = new ArrayList<TextView>(20);
        imageViewArrayList = new ArrayList<ImageView>(20);
        textViewArrayList2 = new ArrayList<TextView>(20);


        for(int count=0; count<20; count++) {
            //날짜
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String getTime = sdf.format(date);

            //TextView 추가 부분
            TextView textView1 = new TextView(getApplicationContext());

            textView1.setId(count);
            textView1.setText(getTime+(count+1));
            textView1.setX(200);
            textView1.setY(300);
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20); // textSize
            textView1.setGravity(Gravity.LEFT);
            linear1.addView(textView1);
            textViewArrayList1.add(textView1);

            //ImageView 추가 부분
            ImageView imgView = new ImageView(getApplicationContext());

            imgView.setId(count);
            imgView.setImageResource(R.drawable.img1);
            imgView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            imgView.setY(200);
            linear1.addView(imgView);
            imageViewArrayList.add(imgView);

            //TextView 추가 부분
            TextView textView2 = new TextView(getApplicationContext());
            textView2.setId(count);
            textView2.setX(210);
            textView2.setY(100);
            textView2.setTextSize(20);
            textView2.setText("알림기록은 나중에 받아오겠음" + (count+1));

            linear1.addView(textView2);
            textViewArrayList2.add(textView2);
        }
    }

    public void onClickBackButton(View v){
        finish();
    }
}