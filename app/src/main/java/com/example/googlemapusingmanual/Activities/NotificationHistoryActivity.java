package com.example.googlemapusingmanual.Activities;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.googlemapusingmanual.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NotificationHistoryActivity extends AppCompatActivity {

    private LinearLayout linear;
    private ArrayList<TextView> textViewArrayList;
    private ArrayList<ImageView> imageViewArrayList;

    private int[] textureArrayWin = {
            R.drawable.water,
            R.drawable.walk,
            R.drawable.run,
            R.drawable.bicycle,
            R.drawable.push_up,
            R.drawable.sit_up,
            R.drawable.game,
            R.drawable.rainy,
            R.drawable.snow
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);

        linear = findViewById(R.id.linear1);

        textViewArrayList = new ArrayList<TextView>(20);
        imageViewArrayList = new ArrayList<ImageView>(20);

        String setText = "";

        for(int id=21; id<25; id+=3){
            setText = fileReadWeather(id);
            if(setText != ""){
                createImgView(id, 200, 200, 100);
                createTextView(id, 310, -100, setText);
                setText = "";
            }
        }

        for(int id=0; id<20; id++) {
            setText = fileRead(id);
            if(setText != ""){
                createImgView(id, 200, 200, 100);
                createTextView(id, 310, -100, setText);
                setText = "";
            }
        }





    }

    private void createTextView(int id, int setX, int setY, String text){
        TextView textView = new TextView(getApplicationContext());
        textView.setId(id);

        textView.setText(text);

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
        imgView.setImageResource(textureArrayWin[id/3]);
        imgView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        imgView.setY(setY);
        linear.addView(imgView);
        imageViewArrayList.add(imgView);
    }

    public String fileRead(int id) {
        String readStr = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir() + "sw"+id+".txt"));
            String str = null;
            while ((str = br.readLine()) != null) {
                readStr += str + "\n";
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readStr;
    }

    public String fileReadWeather(int id) {
        String readStr = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir() + "weather"+id+".txt"));
            String str = null;
            while ((str = br.readLine()) != null) {
                readStr += str + "\n";
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readStr;
    }


    public void onClickBackButton(View v){
        finish();
    }
}