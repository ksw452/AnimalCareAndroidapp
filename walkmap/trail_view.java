package com.example.walkmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class trail_view extends AppCompatActivity {

    private DatabaseReference mDatabase;

    //DB
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_view);
        Intent intent = getIntent();
        String username = intent.getStringExtra("trail_viewuser");
        String number = (String) intent.getStringExtra("trail_number");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("trails").child(username).child(number).addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {




                                TextView titletext = (TextView)findViewById(R.id.title_text1);
                                TextView contenttext = (TextView)findViewById(R.id.content_text1);
                                titletext.setText(dataSnapshot.child("trailTitle").getValue().toString());
                                contenttext.setText(dataSnapshot.child("content").getValue().toString());
                                System.out.println(dataSnapshot.child("trailTitle").getValue().toString());





                            }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                    }
                });


        Button back = (Button) findViewById(R.id.back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        Button trailopen1 = (Button) findViewById(R.id.trail_view1);
        trailopen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(trail_view.this, com.example.walkmap.trailshare.class);
                intent.putExtra("trail_viewuser", username);
                intent.putExtra("trail_number", number);
                startActivity(intent);

            }
        });

    }
}
