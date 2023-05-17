package com.example.googlemapusingmanual.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.googlemapusingmanual.Utils.PermissionUtils;
import com.example.googlemapusingmanual.R;
import com.example.googlemapusingmanual.Classes.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Button LoginBtn, RegisterBtn, FindBtn;
    private EditText loginId, loginPasswd;
    private String email,pwd,check_num;
    private String[] id_arr;
    public static UserInfo info = new UserInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginBtn = findViewById(R.id.LoginBtn);
        RegisterBtn = findViewById(R.id.RegisterBtn);
        FindBtn = findViewById(R.id.Find_Btn);
        loginId = findViewById(R.id.loginId);
        loginPasswd = findViewById(R.id.loginPasswd);


        Map<String, Object> checkMap = new HashMap<String, Object>();
        checkMap.put("check","1");

        //// storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "저장소 사용 권한", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestLocationPermissions((AppCompatActivity) LoginActivity.this, 1, true);

            return;
        }

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String returnStr, id, pw;
                id = loginId.getText().toString();
                pw = loginPasswd.getText().toString();

                new Thread(){
                    @Override
                    public void run() {
                        try{
                            // API 요청을 보내기 위한 URL 생성
                            URL url = new URL("http://52.79.247.229:8000/pics_users/login");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            int status = conn.getResponseCode();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json");


                            // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                            JSONObject data = new JSONObject();
                            data.put("user_id", id);
                            data.put("user_pw", pw);

                            conn.setDoOutput(true);
                            conn.getOutputStream().write(data.toString().getBytes());

                            String output = null;
                            int status = conn.getResponseCode();
                            if(status == HttpURLConnection.HTTP_OK){
                                Intent intent = new Intent(LoginActivity.this, PersonalInfoActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            // 연결 종료
                            conn.disconnect();
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

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

