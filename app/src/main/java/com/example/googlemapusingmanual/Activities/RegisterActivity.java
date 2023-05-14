package com.example.googlemapusingmanual.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.googlemapusingmanual.R;
import com.example.googlemapusingmanual.Classes.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class RegisterActivity extends AppCompatActivity {

    private EditText input_email, input_password, input_password2, input_number;
    private Button btn_register, btn_cancel, id_check;
    private String email, pwd, pwd2, number;
    private String[] id_arr;
    private String check = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_password2 = findViewById(R.id.input_password2);
        input_number = findViewById(R.id.input_number);
        btn_register = findViewById(R.id.btn_register);
        btn_cancel = findViewById(R.id.btn_cancel);
        id_check = findViewById(R.id.id_check);



        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

//        id_check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                email = input_email.getText().toString();
//                id_arr = email.split("@");
//                mIdCheckDataRef.child(id_arr[0]).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String value = snapshot.getValue(String.class);
//
//                        if (value != null) {
//                            Toast.makeText(getApplicationContext(), "사용불가능한 이메일입니다.", Toast.LENGTH_SHORT).show();//토스메세지 출력
//                        } else {
//                            Toast.makeText(getApplicationContext(), "사용가능한 이메일입니다", Toast.LENGTH_SHORT).show();//토스메세지 출력
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = input_email.getText().toString();
                String pw = input_password.getText().toString();
                String pwCheck = input_password2.getText().toString();
                String phoneNumber = input_number.getText().toString();

                if (pw.equals(pwCheck) == true) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                // API 요청을 보내기 위한 URL 생성
                                URL url = new URL("http://52.79.247.229:8000/pics_users/signup");
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            int status = conn.getResponseCode();
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Type", "application/json");


                                // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                                JSONObject data = new JSONObject();
                                data.put("user_id", id);
                                data.put("user_pw", pw);
                                data.put("user_phone", phoneNumber);

                                conn.setDoOutput(true);
                                conn.getOutputStream().write(data.toString().getBytes());

                                String output = null;
                                int status = conn.getResponseCode();
                                if (status == HttpURLConnection.HTTP_OK) {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                // 연결 종료
                                conn.disconnect();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (ProtocolException e) {
                                throw new RuntimeException(e);
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }.start();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "비밀번호 다름", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}