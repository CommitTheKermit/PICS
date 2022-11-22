package com.example.pick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import java.util.Iterator;
import java.util.Objects;

public class Register extends AppCompatActivity {
    private EditText input_email, input_password,input_password2, input_number;
    private Button btn_register, btn_cancel, id_check;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataRef, mIdFindDataRef,mPwdFindDataRef, mIdCheckDataRef;
    private String email,pwd,pwd2,number;
    private String[] id_arr;

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
                onBackPressed();
            }
        });

        id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = input_email.getText().toString();
                id_arr = email.split("@");
                mIdCheckDataRef.child(id_arr[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);

                        if(value!=null){
                            Toast.makeText(getApplicationContext(),"사용불가능한 이메일입니다.",Toast.LENGTH_SHORT).show();//토스메세지 출력
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"사용가능한 이메일입니다",Toast.LENGTH_SHORT).show();//토스메세지 출력
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
                    try{
                        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    UserAccount account = new UserAccount();
//                                account.setIdToken(user.getUid());
                                    account.setEmail(email);
                                    account.setPwd(pwd);
//                                account.setNumber(number);

                                    id_arr = email.split("@");

                                    mIdCheckDataRef.child(id_arr[0]).setValue(email);
                                    mIdFindDataRef.child(number).setValue(email);
                                    mPwdFindDataRef.child(number).child(id_arr[0]).setValue(account);
//                                mDataRef.child("UserAccount").child(number).setValue(id_arr[0]);
//                                mDataRef.child("UserAccount").child(id_arr[0]).setValue(id_arr[0]);
                                    Toast.makeText(Register.this, "등록 성공", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(Register.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    }

                    catch (Exception e){

                        Log.d("kermit",e.getMessage());
                        System.out.println(e.getMessage());
                        e.printStackTrace();

                    }



                }
                else{
                    Toast.makeText(Register.this,"비밀번호 다름",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}