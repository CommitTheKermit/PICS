package com.example.googlemapusingmanual;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class AchieveActivity extends AppCompatActivity {
    int intWater=0;
    int intWalk=0;
    int intRun=0;
    int intBicycle=0;
    int intTime=0;
    String water[] = {
            "물 10L 마시기",
            "물 20L 마시기",
            "물 30L 마시기",
            "물 40L 마시기",
            "물 50L 마시기"
    };
    String walk[] = {
            "1km 걷기",
            "10km 걷기",
            "20km 걷기",
            "30km 걷기",
            "40km 걷기"
    };
    String run[] = {
            "1km 달리기",
            "10km 달리기",
            "20km 달리기",
            "30km 달리기",
            "40km 달리기"
    };
    String bicycle[] = {
            "1km 자전거 타기",
            "10km 자전거 타기",
            "20km 자전거 타기",
            "30km 자전거 타기",
            "40km 자전거 타기"
    };
    String time[] = {
            "1시간 운동하기",
            "10시간 운동하기",
            "20시간 운동하기",
            "30시간 운동하기",
            "40시간 운동하기"
    };

    private int increase(int n){
        n++;
        if(n == 5){
            n = 0;
        }
        return n;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve);

        // 업적 체크박스
        CheckBox[] checkBoxs = {
                (CheckBox)findViewById(R.id.checkBox1),
                (CheckBox)findViewById(R.id.checkBox2),
                (CheckBox)findViewById(R.id.checkBox3),
                (CheckBox)findViewById(R.id.checkBox4),
                (CheckBox)findViewById(R.id.checkBox5)
        };
        checkBoxs[0].setText(water[0]);
        checkBoxs[1].setText(walk[0]);
        checkBoxs[2].setText(run[0]);
        checkBoxs[3].setText(bicycle[0]);
        checkBoxs[4].setText(time[0]);

        checkBoxs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxs[0].isChecked()) {
                    Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                    intWater = increase(intWater);
                    checkBoxs[0].setText(water[intWater]);

                    // 딜레이
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkBoxs[0].setChecked(false);
                        }
                    }, 1000);
                }
            }
        });

        checkBoxs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxs[1].isChecked()) {
                    Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                    intWalk = increase(intWalk);
                    checkBoxs[1].setText(walk[intWalk]);
                    // 딜레이
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkBoxs[1].setChecked(false);
                        }
                    }, 1000);
                }
            }
        });

        checkBoxs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxs[2].isChecked()) {
                    Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                    intRun = increase(intRun);
                    checkBoxs[2].setText(run[intRun]);
                    // 딜레이
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkBoxs[2].setChecked(false);
                        }
                    }, 1000);
                }
            }
        });

        checkBoxs[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxs[3].isChecked()) {
                    Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                    intBicycle = increase(intBicycle);
                    checkBoxs[3].setText(bicycle[intBicycle]);
                    // 딜레이
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkBoxs[3].setChecked(false);
                        }
                    }, 1000);
                }
            }
        });

        checkBoxs[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxs[4].isChecked()) {
                    Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                    intTime = increase(intTime);
                    checkBoxs[4].setText(time[intTime]);
                    // 딜레이
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkBoxs[4].setChecked(false);
                        }
                    }, 1000);
                }
            }
        });
    }
}