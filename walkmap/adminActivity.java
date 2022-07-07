package com.example.walkmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class adminActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;

    Button btn_admin;
    TextView mEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);






        btn_admin = findViewById(R.id.btn_admin);
        btn_admin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                String username = fAuth.getCurrentUser().getEmail().replace('.', '!');


            }
        });
    }
}