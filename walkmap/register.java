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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    private DatabaseReference mDatabase;
    EditText mFullname,mEmail,mPassword,mPhone;
    Button mRegisterbtn;
    TextView mLloginbtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullname = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phonenb);
        mRegisterbtn = findViewById(R.id.registerbtn);
        mLloginbtn = findViewById(R.id.creatbtn);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (fAuth.getCurrentUser() != null){
            Toast.makeText(register.this, "logout Please", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(),Real_MainActivity.class));
            finish();
        }

        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String username = email.replace('.', '!');
                if (email.indexOf("!") > -1) {
                    mEmail.setError("이메일에 느낌표 사용불가");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }
                if (password.length()<6){
                    mPassword.setError("password is 6 or more characters");
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            mDatabase.child("users").child(username).child("usertype").setValue("0");





                            Toast.makeText(register.this, "User is successfuly created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(register.this, Real_MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        mLloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
    }
}
