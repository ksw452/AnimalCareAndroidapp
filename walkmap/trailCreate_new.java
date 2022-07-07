package com.example.walkmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class trailCreate_new extends AppCompatActivity  {
    private DatabaseReference mDatabase;
    private ArrayList<LatLng> list;
    private boolean modifymode = false;
    private boolean modifytrail = false;
    FirebaseAuth fAuth;
    //DB
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailcreate_new);
        final String[] area1 = {"울산광역시"};
        final String[] area2 = {"남구","중구","북구","동구","울주군"};
        final String[] area3 = {"무거동","삼산동"};
        final String[] area4 = {"유곡동","성남동"};
        final String[] area5 = {"진장동"};
        final String[] area6 = {"전하동"};
        final String[] area7 = {"범서읍"};

        ArrayList<String> marea1 = new ArrayList<String>();
        fAuth = FirebaseAuth.getInstance();
        String username = fAuth.getCurrentUser().getEmail().replace('.', '!');

        Intent intent = getIntent();
        String trail_modinumber = intent.getStringExtra("trail_number");
        Button trail_check = (Button) findViewById(R.id.check_trail);
        trail_check.setVisibility(View.GONE);
        modifymode = intent.getBooleanExtra("modifymode",false);
        if(modifymode== true){


            trail_check.setVisibility(View.VISIBLE);

            trail_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(trailCreate_new.this, com.example.walkmap.trailshare.class);
                    intent.putExtra("trail_viewuser",username);
                    intent.putExtra("trail_number", trail_modinumber);
                    startActivityForResult(intent,0);

                }
            });


        }



        Spinner spinner1 = (Spinner) findViewById(R.id.area1_spinner);
        Spinner spinner2 = (Spinner) findViewById(R.id.area2_spinner);
        Spinner spinner3 = (Spinner) findViewById(R.id.area3_spinner);
        EditText create_content = (EditText)findViewById(R.id.create_content);
        EditText create_title = (EditText)findViewById(R.id.create_title);


        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,area1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,area2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,area3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int iCurrentSelection = spinner2.getSelectedItemPosition();

                if(iCurrentSelection == 0){
                    ArrayAdapter adapter3 = new ArrayAdapter(trailCreate_new.this, android.R.layout.simple_spinner_item,area3);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter3);

                }
                else if(iCurrentSelection == 1)
                {
                    ArrayAdapter adapter3 = new ArrayAdapter(trailCreate_new.this, android.R.layout.simple_spinner_item,area4);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter3);
                }
                else if(iCurrentSelection == 2)
                {
                    ArrayAdapter adapter3 = new ArrayAdapter(trailCreate_new.this, android.R.layout.simple_spinner_item,area5);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter3);
                }
                else if(iCurrentSelection == 3)
                {
                    ArrayAdapter adapter3 = new ArrayAdapter(trailCreate_new.this, android.R.layout.simple_spinner_item,area6);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter3);
                }
                else if(iCurrentSelection == 4)
                {
                    ArrayAdapter adapter3 = new ArrayAdapter(trailCreate_new.this, android.R.layout.simple_spinner_item,area7);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter3);
                }


            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        if(modifymode== true) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("trails").child(username).child(trail_modinumber).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (int i = 0; i < area1.length; i++) {
                               if( spinner1.getItemAtPosition(i).toString().equals(snapshot.child("area1").getValue().toString())){

                                   spinner1.setSelection(i);
                               }
                            }
                            for (int i = 0; i < area2.length; i++) {
                                if( spinner2.getItemAtPosition(i).toString().equals(snapshot.child("area2").getValue().toString())){

                                    spinner2.setSelection(i);
                                }
                            }
                            for (int i = 0; i < area3.length; i++) {
                                if( spinner3.getItemAtPosition(i).toString().equals(snapshot.child("area3").getValue().toString())){

                                    spinner3.setSelection(i);
                                }
                            }

                            create_content.setText(snapshot.child("content").getValue().toString());
                            create_title.setText(snapshot.child("trailTitle").getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
            );
        }


            Button trail_Createtrailbtn = (Button) findViewById(R.id.createtrailbtn);
        trail_Createtrailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifytrail = true;
                Intent intent = new Intent(trailCreate_new.this, com.example.walkmap.MainActivity.class);
                startActivityForResult(intent,0);

            }
        });

        if(modifymode==true){

            trail_Createtrailbtn.setText("산책로재작성");


        }

        Button trail_Createcancelbtn = (Button) findViewById(R.id.create_cancel);
        trail_Createcancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        Button trail_Createbtn = (Button) findViewById(R.id.createbtn);
        trail_Createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                mDatabase = FirebaseDatabase.getInstance().getReference();


                mDatabase.child("trails").child(username).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            int j=1;
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String trailId1;

                                String trailId2;
                                int trailId3= 0;


                                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()) {

                                   trailId2 = postSnapshot.getKey().toString();

                                    trailId3 = Integer.parseInt(trailId2) + 1;
                                    /*
                                    데이터 베이스 빈공간 채우는 알고리즘
                                    이것 사용 시 생성순서대로 DB에 1,2,3인데 지운 빈공간을 채우므로
                                    생선순서가 어긋나므로
                                    날짜나 생성순서를 DB에 추가하여 최신순 정렬을 사용해야됨


                                    if (Integer.parseInt(trailId2) != j) {
                                        trailId3 = j;
                                        break;
                                    }

                                    j++;
                                    */
                                }

                                    trailId1 = Integer.toString(trailId3);
                                if(modifymode==true)
                                {
                                    trailId1 = trail_modinumber;

                                }


                                long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                String getTime = sdf.format(date);

                                if(modifymode==false) {
                                    writeNewUser(username, trailId1, create_content.getText().toString(), create_title.getText().toString(), spinner1.getSelectedItem().toString(), spinner2.getSelectedItem().toString(), spinner3.getSelectedItem().toString(), list, null, getTime);
                                }else{
                                    if(modifytrail==false) {
                                        mDatabase.child("trails").child(username).child(trailId1).child("area1").setValue(spinner1.getSelectedItem().toString());
                                        mDatabase.child("trails").child(username).child(trailId1).child("area2").setValue(spinner2.getSelectedItem().toString());
                                        mDatabase.child("trails").child(username).child(trailId1).child("area3").setValue(spinner3.getSelectedItem().toString());
                                        mDatabase.child("trails").child(username).child(trailId1).child("content").setValue(create_content.getText().toString());
                                        mDatabase.child("trails").child(username).child(trailId1).child("trailTitle").setValue(create_title.getText().toString());
                                        mDatabase.child("trails").child(username).child(trailId1).child("mydate").setValue(getTime);
                                    }else{
                                        mDatabase.child("trails").child(username).child(trailId1).child("area1").setValue(spinner1.getSelectedItem().toString());
                                        mDatabase.child("trails").child(username).child(trailId1).child("area2").setValue(spinner2.getSelectedItem().toString());
                                        mDatabase.child("trails").child(username).child(trailId1).child("area3").setValue(spinner3.getSelectedItem().toString());
                                        mDatabase.child("trails").child(username).child(trailId1).child("content").setValue(create_content.getText().toString());
                                        mDatabase.child("trails").child(username).child(trailId1).child("trailTitle").setValue(create_title.getText().toString());
                                        mDatabase.child("trails").child(username).child(trailId1).child("mydate").setValue(getTime);
                                        mDatabase.child("trails").child(username).child(trailId1).child("dbpaths").setValue(list);

                                    }

                                }



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message
                            }
                        });

                try
                {
                   Thread.sleep(500);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                Intent intent = new Intent(trailCreate_new.this, com.example.walkmap.trailCreate.class);
                startActivity(intent);
                finish();
            }
        });






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode,intent);
        if(intent!=null) {
            list = (ArrayList<LatLng>) intent.getSerializableExtra("list");
        }
        else
        {
            list = null;
        }


    }

    @IgnoreExtraProperties
    public class User {
        public String content;
        public String area1;
        public String area2;
        public String area3;
        public String point;
        public String trailTitle;

        public List<LatLng> dbpaths;
        public String mydate;
        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String trailTitle,String content,String area1,String area2,String area3,List<LatLng> dbpaths,String point,String mydate) {
            this.trailTitle = trailTitle;
            this.dbpaths = dbpaths;
            this.content = content;
            this.area1 = area1;
            this.area2 = area2;
            this.area3 = area3;
            this.point = point;
            this.mydate = mydate;
        }


    }
    private void writeNewUser(String userId, String trailId,String content, String trailTitle,String area1,String area2,String area3,List<LatLng> firepaths,String point,String mydate) {
        trailCreate_new.User user = new trailCreate_new.User(trailTitle,content,area1,area2,area3,firepaths,point,mydate);


        mDatabase.child("trails").child(userId).child(trailId).setValue(user);
    }
}

