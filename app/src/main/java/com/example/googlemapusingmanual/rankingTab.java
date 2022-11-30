package com.example.googlemapusingmanual;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.github.mikephil.charting.charts.BarChart;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class rankingTab extends Fragment {
    private LinearLayout linear1;
    private ArrayList<CheckBox> checkBoxArrayList;
    private ArrayList<ImageView> imageViewArrayList;
    private ArrayList<TextView> textViewArrayList;

    String total_list = null;
    String total_list_delete = null;
    ArrayList<String> time_walking = new ArrayList<String>();
    ArrayList<String> time_running = new ArrayList<String>();
    ArrayList<String> time_cycle = new ArrayList<String>();

    float record = 0;  // DB에서 가져와서 저장
    float distance_recent = 0;
    float distance_all = 0;
    double sum = 0 ;
    float c = 0;
    float d = 0;
    float e = 0;
    float f = 0;

    String[] exerciseList = {"걷기", "달리기", "자전거"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            String nickname = "someone" + ".txt";
            InputStream in = null;
            in = getActivity().openFileInput(nickname);
            byte[] b = new byte[in.available()];

            try {
                in.read(b);
                total_list=new String(b);

            } catch (IOException e) {
                Log.d("kermit",e.getMessage());
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        total_list = total_list.replace("<dist>","");
        total_list = total_list.replace("</dist>","");
        total_list = total_list.replace("<CYCLE>\n","");
        total_list = total_list.replace("<RUNNING>\n","");
        total_list = total_list.replace("<WALKING>\n","");
        total_list = total_list.replace("</WALKING>\n","");


        String [] cut_list_1 = total_list.split("</CYCLE>\n");
        String cut_list_2 = cut_list_1[0];

        String [] cut_list_3 = cut_list_2.split("\n");
        for (int i=0 ; i<cut_list_3.length; i++) {
            time_cycle.add(cut_list_3[i]);
        }

        String cut_list_4 = cut_list_1[1];
        String [] cut_list_5 = cut_list_4.split("</RUNNING>\n");

        String cut_list_6 = cut_list_5[0]; //running
        String cut_list_7 = cut_list_5[1]; //walking

        String [] cut_list_8 = cut_list_6.split("\n");
        for (int i=0 ; i<cut_list_8.length; i++) {
            time_running.add(cut_list_8[i]);
        }

        String [] cut_list_9 = cut_list_7.split("\n");
        for (int i=0 ; i<cut_list_9.length; i++) {
            time_walking.add(cut_list_9[i]);
        }




        View rootView = inflater.inflate(R.layout.fragment_ranking_tab, container, false);

        TextView txtView1 = (TextView)rootView.findViewById(R.id.textView1);
        txtView1.setTextSize(20);
        txtView1.setText(String.format("걷기 총 이동거리 : %.1f km",record));

        TextView txtView2 = (TextView)rootView.findViewById(R.id.textView2);
        txtView2.setTextSize(20);
        txtView2.setText(String.format("달리기 총 이동거리 : %.1f km",record));

        TextView txtView3 = (TextView)rootView.findViewById(R.id.textView3);
        txtView3.setTextSize(20);

        LineChart chart = rootView.findViewById(R.id.lineChart);


        String[] exerciseList = {"걷기", "달리기", "자전거"};

        Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, exerciseList);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?>parent, View view, int position, long id) {
                if (position==0) {
                    distance_recent = Float.parseFloat(time_walking.get(time_walking.size() - 1));
                    for (int i = 0; i < time_walking.size(); i++) {
                         sum += Float.parseFloat(time_walking.get(i));
                    }
                    distance_recent = Math.round(distance_recent*100)/100;
                    sum = Math.round(sum*100)/100.0;


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
                    yAxisLeft.setAxisMinimum(15); // 최솟값
                    yAxisLeft.setAxisMaximum(15); // 최댓값
                    yAxisLeft.setGranularity(5f); // 값만큼 라인설정

                    // YAxis(Left) (오른쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
                    YAxis yAxis = chart.getAxisRight();
                    yAxis.setDrawLabels(false); // label 삭제
                    yAxis.setTextColor(Color.rgb(70, 50, 70));
                    yAxis.setDrawAxisLine(false);
                    yAxis.setAxisLineWidth(2);
                    yAxis.setAxisMinimum(15); // 최솟값
                    yAxis.setAxisMaximum(15); // 최댓값
                    yAxis.setGranularity(5f);// 값만큼 라인설정

                    ArrayList<Entry> values = new ArrayList<>();

                    LineDataSet set1;
                    set1 = new LineDataSet(values, "실제");

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1); // add the data sets

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

                    chart.setData(data);

                }
                if (position==1) {
                    distance_recent = Float.parseFloat(time_running.get(time_running.size() - 1));
                    for (int i = 0; i < time_running.size(); i++) {
                        sum += Float.parseFloat(time_running.get(i));
                    }
                    distance_recent = Math.round(distance_recent*100)/100;
                    sum = Math.round(sum*100)/100.0;
                }
                if (position==2) {
                    distance_recent = Float.parseFloat(time_cycle.get(time_cycle.size() - 1));
                    for (int i = 0; i < time_cycle.size(); i++) {
                        sum += Float.parseFloat(time_cycle.get(i));
                    }
                    distance_recent = Math.round(distance_recent*100)/100;
                    sum = Math.round(sum*100)/100.0;
                }

                txtView1.setText(exerciseList[position] + " 최근 이동거리 : " + distance_recent + " km");
                txtView2.setText(exerciseList[position] + " 총 이동거리 : " + sum + " km");
                //txtView3.setText(time_walking.toString());
                txtView3.setText(exerciseList[position] + " 가 선택되었습니다.");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        }
        );

        return rootView;
    }
}