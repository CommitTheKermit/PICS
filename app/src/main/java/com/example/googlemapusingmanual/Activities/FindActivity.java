package com.example.googlemapusingmanual.Activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.app.TabActivity;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googlemapusingmanual.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@SuppressWarnings("deprecation")
public class FindActivity extends TabActivity {

    private Button btn3, btn4, btn5, btn6;
    private EditText id_phn, pwd_email, pwd_phn;
    private TextView id_textView, pwd_textView;
    private String email, number;
    private String[] id_arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        id_phn = findViewById(R.id.id_phnumEditText);
        pwd_email = findViewById(R.id.pwd_emailEditText);
        pwd_phn = findViewById(R.id.pwd_phnumEditText);
        id_textView = findViewById(R.id.Id_textView);
        pwd_textView = findViewById(R.id.Pwd_textView);


        TabHost tabHost = getTabHost();

        TabSpec tabSpecTab1 = tabHost.newTabSpec("TAB1").setIndicator("ID 찾기");
        tabSpecTab1.setContent(R.id.tab1);
        tabHost.addTab(tabSpecTab1);

        TabSpec tabSpecTab2 = tabHost.newTabSpec("TAB2").setIndicator("PW 찾기");
        tabSpecTab2.setContent(R.id.tab2);
        tabHost.addTab(tabSpecTab2);

        tabHost.setCurrentTab(0);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = id_phn.getText().toString();

                new Thread(){
                    @Override
                    public void run() {
                        try{
                            // API 요청을 보내기 위한 URL 생성
                            URL url = new URL("http://52.79.247.229:8000/pics_users/findid");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            int status = conn.getResponseCode();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json");


                            // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                            JSONObject data = new JSONObject();
                            data.put("user_phone", number);

                            conn.setDoOutput(true);
                            conn.getOutputStream().write(data.toString().getBytes());

                            String output = null;
                            int status = conn.getResponseCode();
                            if(status == HttpURLConnection.HTTP_OK){
                                InputStream temp = conn.getInputStream();
                                output = new BufferedReader(new InputStreamReader(temp)).lines()
                                        .reduce((a, b) -> a + b).get();
                                String returnStr = new JSONObject(output).getString("user_id");

                                id_textView.setText("ID : " + returnStr);
                                conn.disconnect();
                            }
                            else{
//                                Toast.makeText(FindActivity.this, "전화번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e) {
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
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number = pwd_phn.getText().toString();
                String id = pwd_email.getText().toString();

                new Thread(){
                    @Override
                    public void run() {
                        try{
                            // API 요청을 보내기 위한 URL 생성
                            URL url = new URL("http://52.79.247.229:8000/pics_users/findpw");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json");


                            // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                            JSONObject data = new JSONObject();
                            data.put("user_id", id);
                            data.put("user_phone", number);

                            conn.setDoOutput(true);
                            conn.getOutputStream().write(data.toString().getBytes());

                            String output = null;
                            int status = conn.getResponseCode();
                            if(status == HttpURLConnection.HTTP_OK){
                                InputStream temp = conn.getInputStream();
                                output = new BufferedReader(new InputStreamReader(temp)).lines()
                                        .reduce((a, b) -> a + b).get();
                                String returnStr = new JSONObject(output).getString("user_pw");


                                pwd_textView.setText("PW : " + returnStr);
                                conn.disconnect();
                            }
                            else{
//                                Toast.makeText(FindActivity.this, "아이디와 전화번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                            }
                            // 연결 종료

                        }
                        catch (JSONException e)
                        {
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
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}