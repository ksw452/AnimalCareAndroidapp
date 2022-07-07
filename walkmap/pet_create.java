package com.example.walkmap;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class pet_create extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ArrayList<LatLng> list;
    private boolean modifymode = false;

    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_create);
        fAuth = FirebaseAuth.getInstance();



        final String[] sex = {"성별","남","여"};
        final String[] neut = {"중성화여부","예","아니오"};

        Spinner spinner1 = (Spinner) findViewById(R.id.pet_sex);
        Spinner spinner2 = (Spinner) findViewById(R.id.pet_neut);

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,sex);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,neut);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);



    }
}
