package com.example.googlemapusingmanual;

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

public class rankingTab extends Fragment {
    private LinearLayout linear1;
    private ArrayList<CheckBox> checkBoxArrayList;
    private ArrayList<ImageView> imageViewArrayList;
    private ArrayList<TextView> textViewArrayList;

    int record = 0;  // DB에서 가져와서 저장
    int rank = 0;  // DB에서 가져와서 저장
    String[] exerciseList = {"운동1", "운동2", "운동3", "운동4"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_ranking_tab, container, false);

        TextView txtView1 = (TextView)rootView.findViewById(R.id.textView1);
        txtView1.setTextSize(20);
        txtView1.setText(String.format("이전 기록 : %d 위 입니다.",record));

        TextView txtView2 = (TextView)rootView.findViewById(R.id.textView2);
        txtView2.setTextSize(20);
        txtView2.setText(String.format("랭킹 기록 : %d 위 입니다.",rank));

        TextView txtView3 = (TextView)rootView.findViewById(R.id.textView3);
        txtView3.setTextSize(20);

        //  Spinner spinner = new Spinner(getActivity().getApplicationContext());
        String[] exerciseList = {"운동 1", "운동 2", "운동 3", "운동 4"};
        Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, exerciseList);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                                              @Override
                                              public void onItemSelected(AdapterView<?>parent, View view, int position, long id) {
                                                  txtView3.setText(exerciseList[position] + "이 선택되었습니다.");

                                                  // 그래프 변경 수정중//
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> adapterView) {
                                              }
                                          }
        );

        return rootView;

        // return inflater.inflate(R.layout.fragment_ranking_tab, container, false);
    }
}