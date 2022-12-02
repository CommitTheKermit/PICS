package com.example.googlemapusingmanual;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class weightTab extends Fragment {
    float height_cm = 0;
    float height_m = 0;
    float weight = 0;
    float ideal_weight = 0;
    float bmi = 0;
    float user_bmi_position = 0;
    String user_bmi_state = null;

    float difference_with_latestRecord = 0;
    float difference_with_firstRecord = 0;

    ArrayList<String> personal_info_arr = new ArrayList<String>();
    ArrayList<String> weight_record_arr = new ArrayList<String>();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            String nickname = LoginActivity.info.getID() + "_info.txt";
            InputStream in = null;
            in = getActivity().openFileInput(nickname);

            byte[] b = new byte[in.available()];

            try {
                in.read(b);
            } catch (IOException e) {
                Log.d("kermit",e.getMessage());
            }
            in.close();

            Scanner scanStop = new Scanner(new String(b));
            while(scanStop.hasNext()){
                personal_info_arr.add(scanStop.next());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        View rootView = inflater.inflate(R.layout.fragment_weight_tab, container, false);

        weight = Float.parseFloat(personal_info_arr.get(2));
        height_cm = Float.parseFloat(personal_info_arr.get(3));
        height_m = height_cm / 100;
        ideal_weight = (height_cm - 100) * 0.9f;
        bmi = weight / (height_m * height_m);
        user_bmi_position = (bmi-15)*29.2f;

        check_bmi_state();

        LineChart chart = rootView.findViewById(R.id.lineChart);

        chart.setExtraBottomOffset(15f);
        chart.getDescription().setEnabled(false);

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

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.rgb(70, 50, 70));
        xAxis.setSpaceMin(0.1f);
        xAxis.setSpaceMax(0.1f);

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setTextSize(10f);
        yAxisLeft.setTextColor(Color.rgb(70, 50, 70));
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisLineWidth(2);
        yAxisLeft.setAxisMinimum(ideal_weight-30);
        yAxisLeft.setAxisMaximum(ideal_weight+30);
        yAxisLeft.setGranularity(5f);

        YAxis yAxis = chart.getAxisRight();
        yAxis.setDrawLabels(false);
        yAxis.setTextColor(Color.rgb(70, 50, 70));
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisLineWidth(2);
        yAxis.setAxisMinimum(ideal_weight-30);
        yAxis.setAxisMaximum(ideal_weight+30);
        yAxis.setGranularity(5f);

        ArrayList<Entry> weight_value_in_graph = new ArrayList<>();
        ArrayList<Entry> ideal_value_in_graph = new ArrayList<>();

        try {
            String nickname = LoginActivity.info.getID() + "_weight" + ".txt";
            InputStream in = null;
            in = getActivity().openFileInput(nickname);

            byte[] d = new byte[in.available()];

            try {
                in.read(d);
            } catch (IOException e) {
                Log.d("kermit",e.getMessage());
            }
            in.close();

            Scanner scanStop = new Scanner(new String(d));
            while(scanStop.hasNext()){
                weight_record_arr.add(scanStop.next());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i=0; i < weight_record_arr.size(); i++) {
            weight_value_in_graph.add(new Entry(i, Float.parseFloat(weight_record_arr.get(i))));
            ideal_value_in_graph.add(new Entry(i, ideal_weight));
        }

        LineDataSet weight_record_line, ideal_weight_line;
        weight_record_line = new LineDataSet(weight_value_in_graph, "실제");
        ideal_weight_line = new LineDataSet(ideal_value_in_graph, "이상");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(weight_record_line);
        dataSets.add(ideal_weight_line);

        LineData linedata = new LineData(dataSets);

        weight_record_line.setLineWidth(3);
        weight_record_line.setCircleRadius(6);
        weight_record_line.setDrawValues(false);
        weight_record_line.setDrawCircleHole(true);
        weight_record_line.setDrawCircles(true);
        weight_record_line.setDrawHorizontalHighlightIndicator(false);
        weight_record_line.setDrawHighlightIndicators(false);
        weight_record_line.setColor(Color.rgb(155, 155, 255));
        weight_record_line.setCircleColor(Color.rgb(155, 155, 255));

        ideal_weight_line.setLineWidth(3);
        ideal_weight_line.setCircleRadius(1);
        ideal_weight_line.setDrawValues(false);
        ideal_weight_line.setDrawCircleHole(true);
        ideal_weight_line.setDrawCircles(true);
        ideal_weight_line.setDrawHorizontalHighlightIndicator(false);
        ideal_weight_line.setDrawHighlightIndicators(false);
        ideal_weight_line.setColor(Color.rgb(178, 223, 138));
        ideal_weight_line.setCircleColor(Color.rgb(178, 223, 138));

        chart.setData(linedata);

        EditText editText = (EditText)rootView.findViewById(R.id.editTextNumber);
        Button btn_input = rootView.findViewById(R.id.button);

        ImageView imageView1 = (ImageView)rootView.findViewById(R.id.imageView3);
        imageView1.setX(user_bmi_position);

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
        txtView4.setText(String.format("%+.1f kg",difference_with_latestRecord));

        TextView txtView5 = (TextView)rootView.findViewById(R.id.textView5);
        txtView5.setTextSize(30);
        txtView5.setText(String.format("%+.1f kg",difference_with_firstRecord));

        TextView txtView6 = (TextView)rootView.findViewById(R.id.textView6);
        txtView6.setTextSize(18);
        txtView6.setText(String.format("현재 당신은 '%s'에 해당합니다.",user_bmi_state));

        TextView txtView7 = (TextView)rootView.findViewById(R.id.textView7);
        txtView7.setTextSize(15);
        txtView7.setText(String.format("변화된 체중 입력   :"));

        weight = Float.parseFloat(weight_record_arr.get(weight_record_arr.size()-1));
        if (weight_record_arr.size()>1) {
            difference_with_latestRecord = Float.parseFloat(weight_record_arr.get(weight_record_arr.size()-1)) - Float.parseFloat(weight_record_arr.get(weight_record_arr.size()-2));
        }
        else {
            difference_with_latestRecord =0;
        }

        difference_with_firstRecord = Float.parseFloat(weight_record_arr.get(weight_record_arr.size()-1)) - Float.parseFloat(weight_record_arr.get(0));

        txtView1.setText(String.format("당신의 이상적 체중은 %.1f kg 입니다.\n당신의 현재 체중은 %.1f kg 입니다.",ideal_weight,weight));
        txtView4.setText(String.format("%+.1f kg",difference_with_latestRecord));
        txtView5.setText(String.format("%+.1f kg",difference_with_firstRecord));


        btn_input.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    String nickname = LoginActivity.info.getID() + "_weight" + ".txt";
                    InputStream in = null;
                    OutputStreamWriter outputStream = new OutputStreamWriter(getActivity().openFileOutput(
                            nickname, Context.MODE_APPEND));

                    try {
                        outputStream.write(editText.getText().toString()+"\n");
                        outputStream.close();
                        in = getActivity().openFileInput(nickname);
                    }
                    catch (FileNotFoundException e){

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

                    Scanner scanStop = new Scanner(new String(c));
                    while(scanStop.hasNext()){
                        weight_record_arr.add(scanStop.next());
                    }

                    weight = Float.parseFloat(weight_record_arr.get(weight_record_arr.size()-1));


                    height_m = height_cm / 100;
                    ideal_weight = (height_cm - 100) * 0.9f;
                    bmi = weight / (height_m * height_m);
                    user_bmi_position = (bmi-15)*29.2f;

                    check_bmi_state();

                    imageView1.setX(user_bmi_position);




                    try {
                        String nickname_1 = LoginActivity.info.getID() + "_info" + ".txt";
                        //InputStream in_1 = null;
                        OutputStreamWriter outputStream_1 = new OutputStreamWriter(getActivity().openFileOutput(
                                nickname_1,
                                Context.MODE_PRIVATE
                        ));

                        outputStream_1.write(
                                personal_info_arr.get(0)+"\n"+
                                        personal_info_arr.get(1)+"\n"+
                                        weight_record_arr.get(weight_record_arr.size()-1)+"\n"+
                                        personal_info_arr.get(3)+"\n"+
                                        personal_info_arr.get(4)
                        );
                        outputStream_1.close();
                        //in_1= getActivity().openFileInput(nickname_1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameMain, new weightTab())
                        .commit();
            }

        });

        return rootView;
    }

    public void check_bmi_state() {
        if (bmi < 20) {
            user_bmi_state = "저체중";
        }
        else if (bmi < 25) {
            user_bmi_state = "정상";
        }
        else if (bmi < 30) {
            user_bmi_state = "과체중";
        }
        else if (bmi < 30) {
            user_bmi_state = "비만";
        }
        else {
            user_bmi_state = "초고도비만";
        }
    }

}