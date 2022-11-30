package com.example.googlemapusingmanual;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class weightTab extends Fragment {
    private static final String[] APPS = null;
    private LinearLayout linear1;
    private ArrayList<CheckBox> checkBoxArrayList;
    private ArrayList<ImageView> imageViewArrayList;
    private ArrayList<TextView> textViewArrayList;
    private ImageView imageView1;

    private float outputDist;
    public static String currentWeight = "WALKING";

    float height_cm = 0;
    float height_m = 0;
    float weight = 0;
    float ideal_weight = 0;
    float bmi = 0;
    float calculate = 0;
    String state = null;

    float recent_difference = 0;
    float first_difference = 0;

    ArrayList<String> lines = new ArrayList<String>();
    ArrayList<String> history_weight = new ArrayList<String>();
    //ArrayList<String> graph_weight = new ArrayList<String>();

    private LineChart chart;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            String nickname = "Personal_Info" + ".txt";
            InputStream in_2 = null;
            in_2 = getActivity().openFileInput(nickname);

            byte[] b = new byte[in_2.available()];

            try {
                in_2.read(b);
            } catch (IOException e) {
                Log.d("kermit",e.getMessage());
            }
            in_2.close();

            //ArrayList<String> lines = new ArrayList<String>();

            Scanner scanStop = new Scanner(new String(b));
            while(scanStop.hasNext()){
                lines.add(scanStop.next()); // 실제읽
            }
            //weight = Float.parseFloat(lines.get(2));
            //height_cm = Float.parseFloat(lines.get(3));
        } catch (IOException e) {
            e.printStackTrace();
        }

        View rootView = inflater.inflate(R.layout.fragment_weight_tab, container, false);

        //weight = Float.parseFloat(value);
        weight = Float.parseFloat(lines.get(2));
        height_cm = Float.parseFloat(lines.get(3));
        height_m = height_cm / 100;
        ideal_weight = (height_cm - 100) * 0.9f;
        bmi = weight / (height_m * height_m);
        calculate = (bmi-15)*29.2f;

        if (bmi < 20) {
             state = "저체중";
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

        // 그래프 관련

        LineChart chart = rootView.findViewById(R.id.lineChart);

        chart.setExtraBottomOffset(15f); // 간격
        chart.getDescription().setEnabled(false); // chart 밑에 description 표시 유무

        // Legend는 차트의 범례
        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(10);
        legend.setTextSize(13);
        legend.setTextColor(Color.parseColor("#A3A3A3"));
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setYEntrySpace(5);
        legend.setWordWrapEnabled(true);
        legend.setXOffset(80f);
        legend.setYOffset(20f);
        legend.getCalculatedLineSizes();

        // XAxis (아래쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 데이터 표시 위치
        xAxis.setGranularity(1f); // 값만큼 라인설정
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.rgb(70, 50, 70));
        xAxis.setSpaceMin(0.1f); // Chart 맨 왼쪽 간격 띄우기
        xAxis.setSpaceMax(0.1f); // Chart 맨 오른쪽 간격 띄우기

        // YAxis(Right) (왼쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setTextSize(10f);
        yAxisLeft.setTextColor(Color.rgb(70, 50, 70));
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisLineWidth(2);
        yAxisLeft.setAxisMinimum(ideal_weight-15); // 최솟값
        yAxisLeft.setAxisMaximum(ideal_weight+15); // 최댓값
        yAxisLeft.setGranularity(5f); // 값만큼 라인설정

        // YAxis(Left) (오른쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        YAxis yAxis = chart.getAxisRight();
        yAxis.setDrawLabels(false); // label 삭제
        yAxis.setTextColor(Color.rgb(70, 50, 70));
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisLineWidth(2);
        yAxis.setAxisMinimum(ideal_weight-15); // 최솟값
        yAxis.setAxisMaximum(ideal_weight+15); // 최댓값
        yAxis.setGranularity(5f);// 값만큼 라인설정

        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> values_2 = new ArrayList<>();

        try {
            String nickname = "someone_weight" + ".txt";
            InputStream in_3 = null;
            in_3 = getActivity().openFileInput(nickname);

            byte[] d = new byte[in_3.available()];

            try {
                in_3.read(d);
            } catch (IOException e) {
                Log.d("kermit",e.getMessage());
            }
            in_3.close();

            Scanner scanStop = new Scanner(new String(d));
            while(scanStop.hasNext()){
                history_weight.add(scanStop.next()); // 실제읽
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i=0; i < history_weight.size(); i++) {
            //float val =i;
            values.add(new Entry(i, Float.parseFloat(history_weight.get(i))));
            values_2.add(new Entry(i, ideal_weight));
        }

        LineDataSet set1, set2;
        set1 = new LineDataSet(values, "실제");
        set2 = new LineDataSet(values_2, "이상");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setLineWidth(3);
        set1.setCircleRadius(6);
        set1.setDrawValues(false);
        set1.setDrawCircleHole(true);
        set1.setDrawCircles(true);
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setDrawHighlightIndicators(false);
        set1.setColor(Color.rgb(155, 155, 255));
        set1.setCircleColor(Color.rgb(155, 155, 255));

        set2.setLineWidth(3);
        set2.setCircleRadius(1);
        set2.setDrawValues(false);
        set2.setDrawCircleHole(true);
        set2.setDrawCircles(true);
        set2.setDrawHorizontalHighlightIndicator(false);
        set2.setDrawHighlightIndicators(false);
        set2.setColor(Color.rgb(178, 223, 138));
        set2.setCircleColor(Color.rgb(178, 223, 138));

        // set data
        chart.setData(data);




        // 그래프 관련 끝

        EditText editText = (EditText)rootView.findViewById(R.id.editTextNumber);
        Button btn = rootView.findViewById(R.id.button);

        ImageView imageView1 = (ImageView)rootView.findViewById(R.id.imageView3);
        imageView1.setX(calculate);

        TextView txtView1 = (TextView)rootView.findViewById(R.id.textView1);
        txtView1.setTextSize(18);
        txtView1.setText(String.format("당신의 이상적 체중은 %.1f kg 입니다.\n당신의 현재 체중은 %.1f kg 입니다.",ideal_weight,weight));

        TextView txtView2 = (TextView)rootView.findViewById(R.id.textView2);
        txtView2.setTextSize(15);
        txtView2.setText(String.format("최근 기록치와 비교"));

        TextView txtView3 = (TextView)rootView.findViewById(R.id.textView3);
        txtView3.setTextSize(15);
        txtView3.setText(String.format("처음 기록치와 비교"));

        TextView txtView4 = (TextView)rootView.findViewById(R.id.textView4);
        txtView4.setTextSize(30);
        txtView4.setText(String.format("%+.1f kg",recent_difference));

        TextView txtView5 = (TextView)rootView.findViewById(R.id.textView5);
        txtView5.setTextSize(30);
        txtView5.setText(String.format("%+.1f kg",first_difference));

        TextView txtView6 = (TextView)rootView.findViewById(R.id.textView6);
        txtView6.setTextSize(18);
        txtView6.setText(String.format("현재 당신은 '%s'에 해당합니다.",state));

        TextView txtView7 = (TextView)rootView.findViewById(R.id.textView7);
        txtView7.setTextSize(15);
        txtView7.setText(String.format("변화된 체중 입력   :"));

        TextView txtView8 = (TextView)rootView.findViewById(R.id.textView8);
        txtView8.setTextSize(15);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //    txtView8.setText(editText.getText().toString());
                try {
                    String nickname = "someone_weight" + ".txt";
                    InputStream in = null;
                    OutputStreamWriter outputStream = new OutputStreamWriter(getActivity().openFileOutput(
                            nickname, Context.MODE_APPEND));


                    try {
                        outputStream.write(editText.getText().toString()+"\n");
                        outputStream.close();
                        in = getActivity().openFileInput(nickname);
                    }
                    catch (FileNotFoundException e){
                        //outputStream.write(Float.toString(weight));
                        outputStream.write(editText.getText().toString()+"\n");
                        outputStream.close();
                        in = getActivity().openFileInput(nickname);
                    }

                    byte[] c = new byte[in.available()];

                    try {
                        in.read(c);
                    } catch (IOException e) {
                        Log.d("kermit",e.getMessage());
                    }
                    in.close();

                    //ArrayList<String> history_weight = new ArrayList<String>();

                    Scanner scanStop = new Scanner(new String(c));
                    while(scanStop.hasNext()){
                        history_weight.add(scanStop.next()); // 실제읽
                    }
                    //for(int i=1 ; i<history_weight.size()+1 ; i++){
                    weight = Float.parseFloat(history_weight.get(history_weight.size()-1));
                    //}

                    if (history_weight.size()>1) {
                        recent_difference = Float.parseFloat(history_weight.get(history_weight.size()-1)) - Float.parseFloat(history_weight.get(history_weight.size()-2));

                    }
                    else {
                        recent_difference =0;
                    }

                    first_difference = Float.parseFloat(history_weight.get(history_weight.size()-1)) - Float.parseFloat(history_weight.get(0));
                    height_m = height_cm / 100;
                    ideal_weight = (height_cm - 100) * 0.9f;
                    bmi = weight / (height_m * height_m);
                    calculate = (bmi-15)*29.2f;

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

                    imageView1.setX(calculate);
                    txtView1.setText(String.format("당신의 이상적 체중은 %.1f kg 입니다.\n당신의 현재 체중은 %.1f kg 입니다.",ideal_weight,weight));
                    txtView4.setText(String.format("%+.1f kg",recent_difference));
                    txtView5.setText(String.format("%+.1f kg",first_difference));
                    txtView6.setText(String.format("현재 당신은 '%s'에 해당합니다.",state));

                    try {
                        String nickname_1 = "Personal_Info" + ".txt";
                        InputStream in_1 = null;
                        OutputStreamWriter outputStream_1 = new OutputStreamWriter(getActivity().openFileOutput(
                                nickname_1,
                                Context.MODE_PRIVATE
                        ));

                        outputStream_1.write(
                                lines.get(0)+"\n"+
                                        lines.get(1)+"\n"+
                                        history_weight.get(history_weight.size()-1)+"\n"+
                                        lines.get(3)+"\n"+
                                        lines.get(4)
                        );
                        outputStream_1.close();
                        in_1= getActivity().openFileInput(nickname_1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });
        return rootView;
    }
}