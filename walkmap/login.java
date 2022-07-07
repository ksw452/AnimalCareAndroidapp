package com.example.walkmap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mloginbtn;
    TextView mregister;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    private long pressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseAuth.getInstance().signOut();

        mEmail = findViewById(R.id.Email1);
        mPassword = findViewById(R.id.password1);
        mloginbtn = findViewById(R.id.loginbtn);
        mregister = findViewById(R.id.creatbtn);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }
                if (password.length()<6){
                    mPassword.setError("6자리 이상의 패스워드를 입력하세요.");
                }


                //authenticating the user

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(login.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = fAuth.getCurrentUser();


                            startActivity(new Intent(getApplicationContext(),Real_MainActivity.class));
                        }
                        else {
                            Toast.makeText(login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });





    }

    @Override
    public void onBackPressed(){
            if(pressedTime== 0)
            {
                Toast.makeText(login.this,"한번 더 누르면 종료됩니다",Toast.LENGTH_LONG).show();
                pressedTime = System.currentTimeMillis();
            }
            else{

                int seconds = (int) (System.currentTimeMillis() - pressedTime);

                if(seconds> 2000){
                    pressedTime = 0;
                }else{
                    FirebaseAuth.getInstance().signOut();
                    finish();
                }
            }

    }

}
