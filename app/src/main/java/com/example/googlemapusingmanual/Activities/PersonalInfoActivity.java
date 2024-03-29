package com.example.googlemapusingmanual.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.example.googlemapusingmanual.R;

import java.io.FileOutputStream;
import java.io.IOException;

public class PersonalInfoActivity extends AppCompatActivity {

    private EditText nickname_text, age_text, weight_text, height_text;
    private Button btn2, btn3;
    private String nickname, age, weight, height, gender;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        btn2 = findViewById(R.id.sava_btn);
        btn3 = findViewById(R.id.cancle_btn);
        nickname_text = findViewById(R.id.NicknameText);
        age_text = findViewById(R.id.AgeText);
        weight_text = findViewById(R.id.WeightText);
        height_text = findViewById(R.id.HeighText);
        spinner = findViewById(R.id.Gen_btn);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    gender = spinner.getSelectedItem().toString();
                } else if (i == 1) {
                    gender = spinner.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    nickname = nickname_text.getText().toString() + "\n";
                    age = age_text.getText().toString() + "\n";
                    weight = weight_text.getText().toString() + "\n";
                    height = height_text.getText().toString() + "\n";
                    LoginActivity.info.setNickname(nickname);
                    LoginActivity.info.setAge(age);
                    LoginActivity.info.setWeight(weight);
                    LoginActivity.info.setHeight(height);
                    LoginActivity.info.setGender(gender);


                    String temp = LoginActivity.info.getID();
                    FileOutputStream outFs = openFileOutput(temp + "_info.txt", Context.MODE_PRIVATE);

                    outFs.write(LoginActivity.info.getNickname().getBytes());
                    outFs.write(LoginActivity.info.getAge().getBytes());
                    outFs.write(LoginActivity.info.getWeight().getBytes());
                    outFs.write(LoginActivity.info.getHeight().getBytes());
                    outFs.write(LoginActivity.info.getGender().getBytes());
                    outFs.close();
                    Intent intent = new Intent(PersonalInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (IOException E) {
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}