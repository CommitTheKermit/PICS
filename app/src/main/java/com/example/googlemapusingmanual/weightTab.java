package com.example.googlemapusingmanual;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class weightTab extends Fragment {
    private LinearLayout linear1;
    private ArrayList<CheckBox> checkBoxArrayList;
    private ArrayList<ImageView> imageViewArrayList;
    private ArrayList<TextView> textViewArrayList;
    private ImageView imageView1;

    float height_cm = 173;
    float height_m = height_cm / 100;
    float weight = 80;
    float ideal_weight = (height_cm - 100) * 0.9f;
    float bmi = weight / (height_m * height_m);
    float calculate = (bmi-15)*29.2f;
    String state = "초기값";


    int rank = 0;



    float latest_weight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (bmi < 20) {
            String state = "저체중";
        }
        else if (bmi < 25) {
            state = "정상";
        }
        else if (bmi < 30) {
            state = "과체중";
        }
        else if (bmi < 30) {
            state = "비만";
        }
        else {
            state = "초고도비만";
        }


        View rootView = inflater.inflate(R.layout.fragment_weight_tab, container, false);


        ImageView imageView1 = (ImageView)rootView.findViewById(R.id.imageView3);
        imageView1.setX(calculate);

        TextView txtView1 = (TextView)rootView.findViewById(R.id.textView1);
        txtView1.setTextSize(18);
        txtView1.setText(String.format("당신의 이상적 체중은 %.1f kg 입니다.",ideal_weight));

        TextView txtView2 = (TextView)rootView.findViewById(R.id.textView2);
        txtView2.setTextSize(15);
        txtView2.setText(String.format("최근 기록치와 비교"));

        TextView txtView3 = (TextView)rootView.findViewById(R.id.textView3);
        txtView3.setTextSize(15);
        txtView3.setText(String.format("모든 기록치와 비교"));

        TextView txtView4 = (TextView)rootView.findViewById(R.id.textView4);
        txtView4.setTextSize(30);
        txtView4.setText(String.format("%d kg",rank));

        TextView txtView5 = (TextView)rootView.findViewById(R.id.textView5);
        txtView5.setTextSize(30);
        txtView5.setText(String.format("%d kg",rank));

        TextView txtView6 = (TextView)rootView.findViewById(R.id.textView6);
        txtView6.setTextSize(18);
        txtView6.setText(String.format("현재 당신은 %s에 해당합니다.",state));

        return rootView;

        // return inflater.inflate(R.layout.fragment_ranking_tab, container, false);
    }

}