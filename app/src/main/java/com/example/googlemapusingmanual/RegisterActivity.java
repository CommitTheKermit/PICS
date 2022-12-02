package com.example.googlemapusingmanual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {

    private EditText input_email, input_password, input_password2, input_number;
    private Button btn_register, btn_cancel, id_check;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataRef, mIdFindDataRef, mPwdFindDataRef, mIdCheckDataRef;
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

        mAuth = FirebaseAuth.getInstance();
        mDataRef = FirebaseDatabase.getInstance().getReference("Pick");
        mIdFindDataRef = mDataRef.child("IdFind");
        mPwdFindDataRef = mDataRef.child("PwdFind");
        mIdCheckDataRef = mDataRef.child("IdCheck");


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = input_email.getText().toString();
                id_arr = email.split("@");
                mIdCheckDataRef.child(id_arr[0]).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);

                        if (value != null) {
                            Toast.makeText(getApplicationContext(), "사용불가능한 이메일입니다.", Toast.LENGTH_SHORT).show();//토스메세지 출력
                        } else {
                            Toast.makeText(getApplicationContext(), "사용가능한 이메일입니다", Toast.LENGTH_SHORT).show();//토스메세지 출력
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = input_email.getText().toString();
                pwd = input_password.getText().toString();
                pwd2 = input_password2.getText().toString();
                number = input_number.getText().toString();

                if (pwd.equals(pwd2) == true) {
                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                UserAccount account = new UserAccount();

                                account.setEmail(email);
                                account.setPwd(pwd);
                                account.setCheck(check);
                                account.setNumber(number);

                                id_arr = email.split("@");

                                mIdCheckDataRef.child(id_arr[0]).setValue(account);
                                mIdFindDataRef.child(number).setValue(email);
                                mPwdFindDataRef.child(id_arr[0]).child(number).setValue(pwd);

                                Toast.makeText(RegisterActivity.this, "등록 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(RegisterActivity.this, "비밀번호 다름", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}