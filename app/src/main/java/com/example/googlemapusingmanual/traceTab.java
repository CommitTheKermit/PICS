package com.example.googlemapusingmanual;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class traceTab extends Fragment {

    String personal_exerciseTime_list = null;
    ArrayList<String> time_walking = new ArrayList<String>();
    ArrayList<String> time_running = new ArrayList<String>();
    ArrayList<String> time_cycle = new ArrayList<String>();

    float latest_distance = 0;
    double sum_of_distance = 0 ;

    String[] exerciseList = {"걷기", "달리기", "자전거"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            String nickname = LoginActivity.info.getID() + ".txt";
            InputStream in = null;
            in = getActivity().openFileInput(nickname);
            byte[] b = new byte[in.available()];

            try {
                in.read(b);
                personal_exerciseTime_list=new String(b);

            } catch (IOException e) {
                MainActivity.bottomNavigation.getMenu().getItem(2).setChecked(true);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameMain, new mapTab())
                        .commit();
                Toast.makeText(getActivity().getApplicationContext(), "기록 없음!",Toast.LENGTH_SHORT).show();
                return null;
            }
            in.close();

        } catch (IOException e) {
            MainActivity.bottomNavigation.getMenu().getItem(2).setChecked(true);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameMain, new mapTab())
                    .commit();
            Toast.makeText(getActivity().getApplicationContext(), "기록 없음!",Toast.LENGTH_SHORT).show();
            return null;
        }

        extract_data();

        View rootView = inflater.inflate(R.layout.fragment_trace_tab, container, false);

        TextView txtView1 = (TextView)rootView.findViewById(R.id.textView1);
        txtView1.setTextSize(20);

        TextView txtView2 = (TextView)rootView.findViewById(R.id.textView2);
        txtView2.setTextSize(20);

        TextView txtView3 = (TextView)rootView.findViewById(R.id.textView3);
        txtView3.setTextSize(20);

        LineChart walking_chart = rootView.findViewById(R.id.lineChart);
        walking_chart.setExtraBottomOffset(15f); // 간격
        walking_chart.getDescription().setEnabled(false); // chart 밑에 description 표시 유무

        Legend legend = walking_chart.getLegend();
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
        XAxis xAxis = walking_chart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 데이터 표시 위치
        xAxis.setGranularity(1f); // 값만큼 라인설정
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.rgb(70, 50, 70));
        xAxis.setSpaceMin(0.1f); // Chart 맨 왼쪽 간격 띄우기
        xAxis.setSpaceMax(0.1f); // Chart 맨 오른쪽 간격 띄우기

        YAxis yAxisLeft = walking_chart.getAxisLeft();
        yAxisLeft.setTextSize(10f);
        yAxisLeft.setTextColor(Color.rgb(70, 50, 70));
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisLineWidth(2);
        yAxisLeft.setAxisMinimum(0); // 최솟값
        yAxisLeft.setAxisMaximum(5); // 최댓값
        yAxisLeft.setGranularity(1f); // 값만큼 라인설정

        YAxis yAxis = walking_chart.getAxisRight();
        yAxis.setDrawLabels(false); // label 삭제
        yAxis.setTextColor(Color.rgb(70, 50, 70));
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisLineWidth(2);
        yAxis.setAxisMinimum(0); // 최솟값
        yAxis.setAxisMaximum(5); // 최댓값
        yAxis.setGranularity(1f);// 값만큼 라인설정

        ArrayList<Entry> weight_value_in_walkingGraph = new ArrayList<>();

        for (int i=0; i < time_walking.size(); i++) {
            weight_value_in_walkingGraph.add(new Entry(i, Float.parseFloat(time_walking.get(i))));
        }

        LineDataSet walking_distance_line;
        walking_distance_line = new LineDataSet(weight_value_in_walkingGraph, "km");

        ArrayList<ILineDataSet> walking_dataSets = new ArrayList<>();
        walking_dataSets.add(walking_distance_line); // add the data sets

        LineData walkingGraph_line_data = new LineData(walking_dataSets);

        walking_distance_line.setLineWidth(3);
        walking_distance_line.setCircleRadius(6);
        walking_distance_line.setDrawValues(false);
        walking_distance_line.setDrawCircleHole(true);
        walking_distance_line.setDrawCircles(true);
        walking_distance_line.setDrawHorizontalHighlightIndicator(false);
        walking_distance_line.setDrawHighlightIndicators(false);
        walking_distance_line.setColor(Color.rgb(155, 155, 255));
        walking_distance_line.setCircleColor(Color.rgb(155, 155, 255));

        walking_chart.setData(walkingGraph_line_data);

        LineChart running_chart = rootView.findViewById(R.id.lineChart_2);
        running_chart.setExtraBottomOffset(15f); // 간격
        running_chart.getDescription().setEnabled(false); // chart 밑에 description 표시 유무

        // 차트의 범례
        Legend legend_2 = running_chart.getLegend();
        legend_2.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend_2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend_2.setForm(Legend.LegendForm.CIRCLE);
        legend_2.setFormSize(10);
        legend_2.setTextSize(13);
        legend_2.setTextColor(Color.parseColor("#A3A3A3"));
        legend_2.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend_2.setDrawInside(false);
        legend_2.setYEntrySpace(5);
        legend_2.setWordWrapEnabled(true);
        legend_2.setXOffset(80f);
        legend_2.setYOffset(20f);
        legend_2.getCalculatedLineSizes();

        // XAxis (아래쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis_2 = running_chart.getXAxis();
        xAxis_2.setDrawAxisLine(false);
        xAxis_2.setDrawGridLines(false);
        xAxis_2.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 데이터 표시 위치
        xAxis_2.setGranularity(1f); // 값만큼 라인설정
        xAxis_2.setTextSize(10f);
        xAxis_2.setTextColor(Color.rgb(70, 50, 70));
        xAxis_2.setSpaceMin(0.1f); // Chart 맨 왼쪽 간격 띄우기
        xAxis_2.setSpaceMax(0.1f); // Chart 맨 오른쪽 간격 띄우기

        // YAxis(Right) (왼쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        YAxis yAxisLeft_2 = running_chart.getAxisLeft();
        yAxisLeft_2.setTextSize(10f);
        yAxisLeft_2.setTextColor(Color.rgb(70, 50, 70));
        yAxisLeft_2.setDrawAxisLine(false);
        yAxisLeft_2.setAxisLineWidth(2);
        yAxisLeft_2.setAxisMinimum(0); // 최솟값
        yAxisLeft_2.setAxisMaximum(5); // 최댓값
        yAxisLeft_2.setGranularity(1f); // 값만큼 라인설정

        // YAxis(Left) (오른쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        YAxis yAxis_2 = running_chart.getAxisRight();
        yAxis_2.setDrawLabels(false); // label 삭제
        yAxis_2.setTextColor(Color.rgb(70, 50, 70));
        yAxis_2.setDrawAxisLine(false);
        yAxis_2.setAxisLineWidth(2);
        yAxis_2.setAxisMaximum(5); // 최댓값
        yAxis_2.setGranularity(1f);// 값만큼 라인설정

        ArrayList<Entry> weight_value_in_runningGraph= new ArrayList<>();

        for (int i=0; i < time_running.size(); i++) {
            weight_value_in_runningGraph.add(new Entry(i, Float.parseFloat(time_running.get(i))));
        }

        LineDataSet running_distance_line;
        running_distance_line = new LineDataSet(weight_value_in_runningGraph, "km");

        ArrayList<ILineDataSet> running_dataSets = new ArrayList<>();
        running_dataSets.add(running_distance_line); // add the data sets

        // create a data object with the data sets
        LineData runningGraph_line_data = new LineData(running_dataSets);

        // black lines and points
        running_distance_line.setLineWidth(3);
        running_distance_line.setCircleRadius(6);
        running_distance_line.setDrawValues(false);
        running_distance_line.setDrawCircleHole(true);
        running_distance_line.setDrawCircles(true);
        running_distance_line.setDrawHorizontalHighlightIndicator(false);
        running_distance_line.setDrawHighlightIndicators(false);
        running_distance_line.setColor(Color.rgb(155, 155, 255));
        running_distance_line.setCircleColor(Color.rgb(155, 155, 255));

        running_chart.setData(runningGraph_line_data);

        LineChart cycle_chart = rootView.findViewById(R.id.lineChart_3);
        cycle_chart.setExtraBottomOffset(15f); // 간격
        cycle_chart.getDescription().setEnabled(false); // chart 밑에 description 표시 유무

        // Legend는 차트의 범례
        Legend legend_3 = cycle_chart.getLegend();
        legend_3.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend_3.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend_3.setForm(Legend.LegendForm.CIRCLE);
        legend_3.setFormSize(10);
        legend_3.setTextSize(13);
        legend_3.setTextColor(Color.parseColor("#A3A3A3"));
        legend_3.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend_3.setDrawInside(false);
        legend_3.setYEntrySpace(5);
        legend_3.setWordWrapEnabled(true);
        legend_3.setXOffset(80f);
        legend_3.setYOffset(20f);
        legend_3.getCalculatedLineSizes();

        // XAxis (아래쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis_3 = cycle_chart.getXAxis();
        xAxis_3.setDrawAxisLine(false);
        xAxis_3.setDrawGridLines(false);
        xAxis_3.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 데이터 표시 위치
        xAxis_3.setGranularity(1f); // 값만큼 라인설정
        xAxis_3.setTextSize(10f);
        xAxis_3.setTextColor(Color.rgb(70, 50, 70));
        xAxis_3.setSpaceMin(0.1f); // Chart 맨 왼쪽 간격 띄우기
        xAxis_3.setSpaceMax(0.1f); // Chart 맨 오른쪽 간격 띄우기

        // YAxis(Right) (왼쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        YAxis yAxisLeft_3 = cycle_chart.getAxisLeft();
        yAxisLeft_3.setTextSize(10f);
        yAxisLeft_3.setTextColor(Color.rgb(70, 50, 70));
        yAxisLeft_3.setDrawAxisLine(false);
        yAxisLeft_3.setAxisLineWidth(2);
        yAxisLeft_3.setAxisMinimum(0); // 최솟값
        yAxisLeft_3.setAxisMaximum(5); // 최댓값
        yAxisLeft_3.setGranularity(1f); // 값만큼 라인설정

        // YAxis(Left) (오른쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        YAxis yAxis_3 = cycle_chart.getAxisRight();
        yAxis_3.setDrawLabels(false); // label 삭제
        yAxis_3.setTextColor(Color.rgb(70, 50, 70));
        yAxis_3.setDrawAxisLine(false);
        yAxis_3.setAxisLineWidth(2);
        yAxis_3.setAxisMinimum(0); // 최솟값
        yAxis_3.setAxisMaximum(5); // 최댓값
        yAxis_3.setGranularity(1f);// 값만큼 라인설정

        ArrayList<Entry> weight_value_in_cycleGraph = new ArrayList<>();

        for (int i=0; i < time_cycle.size(); i++) {
            weight_value_in_cycleGraph.add(new Entry(i, Float.parseFloat(time_cycle.get(i))));
        }

        LineDataSet cycle_distance_line;
        cycle_distance_line = new LineDataSet(weight_value_in_cycleGraph, "km");

        ArrayList<ILineDataSet> cycle_dataSets = new ArrayList<>();
        cycle_dataSets.add(cycle_distance_line); // add the data sets

        // create a data object with the data sets
        LineData cycleGraph_line_data = new LineData(cycle_dataSets);

        // black lines and points
        cycle_distance_line.setLineWidth(3);
        cycle_distance_line.setCircleRadius(6);
        cycle_distance_line.setDrawValues(false);
        cycle_distance_line.setDrawCircleHole(true);
        cycle_distance_line.setDrawCircles(true);
        cycle_distance_line.setDrawHorizontalHighlightIndicator(false);
        cycle_distance_line.setDrawHighlightIndicators(false);
        cycle_distance_line.setColor(Color.rgb(155, 155, 255));
        cycle_distance_line.setCircleColor(Color.rgb(155, 155, 255));

        cycle_chart.setData(cycleGraph_line_data);

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
                    try{
                        latest_distance = Float.parseFloat(time_walking.get(time_walking.size() - 1));
                        for (int i = 0; i < time_walking.size(); i++) {
                            sum_of_distance += Float.parseFloat(time_walking.get(i));
                        }
                        sum_of_distance = Math.round(sum_of_distance*100)/100.0;

                        walking_chart.setVisibility(View.VISIBLE);
                        running_chart.setVisibility(View.INVISIBLE);
                        cycle_chart.setVisibility(View.INVISIBLE);
                        walking_chart.setEnabled(true);
                        running_chart.setEnabled(false);
                        cycle_chart.setEnabled(false);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "기록 없음!",Toast.LENGTH_SHORT).show();
                        MainActivity.bottomNavigation.getMenu().getItem(2).setChecked(true);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameMain, new mapTab())
                                .commit();
                        return;
                    }
                }
                if (position==1) {

                    try{
                        latest_distance = Float.parseFloat(time_running.get(time_running.size() - 1));
                        for (int i = 0; i < time_running.size(); i++) {
                            sum_of_distance += Float.parseFloat(time_running.get(i));
                        }
                        sum_of_distance = Math.round(sum_of_distance*100)/100.0;

                        walking_chart.setVisibility(View.INVISIBLE);
                        running_chart.setVisibility(View.VISIBLE);
                        cycle_chart.setVisibility(View.INVISIBLE);
                        walking_chart.setEnabled(false);
                        running_chart.setEnabled(true);
                        cycle_chart.setEnabled(false);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "기록 없음!",Toast.LENGTH_SHORT).show();
                        MainActivity.bottomNavigation.getMenu().getItem(2).setChecked(true);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameMain, new mapTab())
                                .commit();
                        return;
                    }
                }
                if (position==2) {

                    try{
                        latest_distance = Float.parseFloat(time_cycle.get(time_running.size() - 1));
                        for (int i = 0; i < time_cycle.size(); i++) {
                            sum_of_distance += Float.parseFloat(time_cycle.get(i));
                        }
                        sum_of_distance = Math.round(sum_of_distance*100)/100.0;

                        walking_chart.setVisibility(View.INVISIBLE);
                        running_chart.setVisibility(View.INVISIBLE);
                        cycle_chart.setVisibility(View.VISIBLE);
                        walking_chart.setEnabled(false);
                        running_chart.setEnabled(false);
                        cycle_chart.setEnabled(true);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "기록 없음!",Toast.LENGTH_SHORT).show();
                        MainActivity.bottomNavigation.getMenu().getItem(2).setChecked(true);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameMain, new mapTab())
                                .commit();
                        return;
                    }
                }

                txtView1.setText(exerciseList[position] + " 최근 이동거리 : " + latest_distance + " km");
                txtView2.setText(exerciseList[position] + " 총 이동거리 : " + sum_of_distance + " km");
                txtView3.setText(exerciseList[position] + "가 선택되었습니다.");

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }

    public void extract_data() {
        personal_exerciseTime_list = personal_exerciseTime_list.replace("<dist>","");
        personal_exerciseTime_list = personal_exerciseTime_list.replace("</dist>","");
        personal_exerciseTime_list = personal_exerciseTime_list.replace("<CYCLE>\n","");
        personal_exerciseTime_list = personal_exerciseTime_list.replace("<RUNNING>\n","");
        personal_exerciseTime_list = personal_exerciseTime_list.replace("<WALKING>\n","");
        personal_exerciseTime_list = personal_exerciseTime_list.replace("</WALKING>\n","");


        String [] cut_list_1 = personal_exerciseTime_list.split("</CYCLE>\n");
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
    }
}