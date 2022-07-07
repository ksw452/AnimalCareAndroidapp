package com.example.walkmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class manager_reserv1 extends AppCompatActivity {

    FirebaseAuth fAuth;
    public static Activity reserv1_acitivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mreserv1);
        fAuth = FirebaseAuth.getInstance();

        reserv1_acitivity = manager_reserv1.this;


        Button baseserv_btn = (Button) findViewById(R.id.baseserv_btn);
        baseserv_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(manager_reserv1.this, manager_reserv2.class);
                intent.putExtra("reserv_serv",0);
                startActivity(intent);

            }
        });

        Button walkserv_btn = (Button) findViewById(R.id.walkserv_btn);
        walkserv_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(manager_reserv1.this, manager_reserv2.class);
                intent.putExtra("reserv_serv",1);
                startActivity(intent);

            }
        });

        Button careserv_btn = (Button) findViewById(R.id.careserv_btn);
        careserv_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(manager_reserv1.this, manager_reserv2.class);
                intent.putExtra("reserv_serv",2);
                startActivity(intent);

            }
        });





    }

}
