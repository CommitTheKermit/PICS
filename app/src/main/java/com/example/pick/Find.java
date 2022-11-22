package com.example.pick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.app.TabActivity;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@SuppressWarnings("deprecation")
public class Find extends TabActivity {

    private Button btn3,btn4,btn5,btn6;
    private EditText id_phn, pwd_email,pwd_phn;
    private TextView id_textView, pwd_textView;
    private String email, number;
    private String[] id_arr;
    private DatabaseReference mDataRef,mIdFindDataRef,mPwdFindDataRef;
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
        mDataRef = FirebaseDatabase.getInstance().getReference("Pick");
        mIdFindDataRef = mDataRef.child("IdFind");
        mPwdFindDataRef = mDataRef.child("PwdFind");

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
                mIdFindDataRef.child(number).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);
                        id_textView.setText("Id : " + value);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = pwd_phn.getText().toString();
                email = pwd_email.getText().toString();
                id_arr = email.split("@");
                mPwdFindDataRef.child(number).child(id_arr[0]).child("pwd").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);
                        pwd_textView.setText("PW : " + value);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


}