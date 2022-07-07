package com.example.walkmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Real_MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    private long pressdTime;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_activity);
        fAuth = FirebaseAuth.getInstance();



        Button trailbtn = (Button) findViewById(R.id.trailmainbtn);
        trailbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Real_MainActivity.this, MainActivity2.class);
                startActivity(intent);
                fAuth.getCurrentUser().getEmail();
            }
        });

        Button reservbtn = (Button) findViewById(R.id.manager_reserv);
        reservbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Real_MainActivity.this, manager_reserv1.class);
                startActivity(intent);
                fAuth.getCurrentUser().getEmail();
            }
        });

        Button btn_apply = (Button) findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Real_MainActivity.this, applyActivity.class);
                startActivity(intent);  // 지원액티비티로 이동
            }
        });

        Button btn_ad = (Button) findViewById(R.id.btn_ad);
        btn_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Real_MainActivity.this,adminActivity.class);
                startActivity(intent);
            }
        });



        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();

            }
        });





        bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            { switch (menuItem.getItemId()){
                    case R.id.navigation_1:{
                        Intent intent = new Intent(Real_MainActivity.this, com.example.walkmap.manager_reserv4.class);
                        startActivity(intent);


                        return true; }
                    case R.id.navigation_2:{
                        Intent intent = new Intent(Real_MainActivity.this, com.example.walkmap.Real_MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true; }
                    case R.id.navigation_3:{
                        Intent intent = new Intent(Real_MainActivity.this, com.example.walkmap.Real_MainActivity.class);
                        startActivity(intent);

                        return true; }
                    case R.id.navigation_4:{
                        Intent intent = new Intent(Real_MainActivity.this, com.example.walkmap.Real_MainActivity.class);
                        startActivity(intent);

                        return true; }
                    default:
                        return false;
            } } });





    }



}
