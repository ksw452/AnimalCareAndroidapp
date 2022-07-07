package com.example.walkmap;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class manager_reserv5 extends AppCompatActivity {
    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mreserv5);
        fAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ArrayList<String> time_list = new ArrayList<>();

        time_list.add("시간 선택");
        time_list.add("06:00");
        time_list.add("07:00");
        time_list.add("08:00");
        time_list.add("09:00");
        time_list.add("10:00");
        time_list.add("11:00");
        time_list.add("12:00");
        time_list.add("13:00");
        time_list.add("14:00");
        time_list.add("15:00");
        time_list.add("16:00");
        time_list.add("17:00");
        time_list.add("18:00");
        time_list.add("19:00");
        time_list.add("20:00");
        time_list.add("21:00");
        time_list.add("22:00");
        time_list.add("23:00");
        time_list.add("24:00");






        mDatabase.child("users").child("khj123@naver!com").child("petmanager").child("reserv_time").child("2021-04-01").child("06:00").setValue("ksw@naver!com");

        mDatabase.child("users").child("khj123@naver!com").child("petmanager").child("reserv_time").addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {




                        try {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    time_list.remove(snapshot1.getKey());
                                    System.out.println(snapshot1.getKey());
                                }
                            }
                        }catch(Exception e){

                            e.printStackTrace();
                        }
                    }@Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                    }

                });


        Spinner spinner1 = (Spinner) findViewById(R.id.manager_time);



        TextView reserv_date = findViewById(R.id.mreserv_date);
        CalendarView reservCalendarView = findViewById(R.id.mreserv_calendar);

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-M-dd");
        String getTime = simpleDate.format(mDate);
        reserv_date.setText(getTime);


        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,time_list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        reservCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                month += 1;
                reserv_date.setText(String.format("%d-%d-%d",year,month,dayOfMonth));
            }
        });

    }




}