package com.example.walkmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    FirebaseAuth fAuth;
    private DatabaseReference mDatabase;
    private String trail_number;
    private ArrayList<trailListViewItem> items;
    private ArrayList<trailListViewItem> items2;
    private ArrayList<String> mytrail_number;
    private ArrayList<trailListViewItem> items3;
    private ArrayList<trailListViewItem> items4;
    private ArrayList<String> mytrail_number2;
    private ArrayList<String> mytrail_number3;

    private  ListView listview ;
    private boolean searchtitle =false;
    private boolean initialize =false;
    private boolean initialize_spinner = false;
    final String[] area1 = {"울산광역시"};
    final String[] area2 = {"남구","중구","북구","동구","울주군"};
    final String[] area3 = {"무거동","삼산동"};
    final String[] area4 = {"유곡동","성남동"};
    final String[] area5 = {"진장동"};
    final String[] area6 = {"전하동"};
    final String[] area7 = {"범서읍"};


    //DB
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        fAuth = FirebaseAuth.getInstance();
        Intent intentx = getIntent();
        searchtitle = intentx.getBooleanExtra("searchtitle",false);
        initialize =  intentx.getBooleanExtra("initialize",false);
        initialize_spinner = intentx.getBooleanExtra("initialize_spinner",false);
        Button Trailsearch = (Button) findViewById(R.id.trailsearch);
        EditText title_text = (EditText) findViewById(R.id.title_text);

        Spinner areaspinner1 = (Spinner) findViewById(R.id.area_mainspinner);
        Spinner areaspinner2 = (Spinner) findViewById(R.id.area_mainspinner2);
        Spinner areaspinner3 = (Spinner) findViewById(R.id.area_mainspinner3);

        ArrayAdapter areaadapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,area1);
        areaadapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaspinner1.setAdapter(areaadapter1);

        ArrayAdapter areaadapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,area2);
        areaadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaspinner2.setAdapter(areaadapter2);

        ArrayAdapter areaadapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,area3);
        areaadapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaspinner3.setAdapter(areaadapter3);

        areaspinner1.setSelection(intentx.getIntExtra("current_area1",0));
        areaspinner2.setSelection(intentx.getIntExtra("current_area2",0));


        areaspinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int iCurrentSelection = areaspinner2.getSelectedItemPosition();

                if(iCurrentSelection == 0){
                    ArrayAdapter areaadapter3 = new ArrayAdapter(MainActivity2.this, android.R.layout.simple_spinner_item,area3);
                    areaadapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaspinner3.setAdapter(areaadapter3);


                }
                else if(iCurrentSelection == 1)
                {
                    ArrayAdapter areaadapter3 = new ArrayAdapter(MainActivity2.this, android.R.layout.simple_spinner_item,area4);
                    areaadapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaspinner3.setAdapter(areaadapter3);

                }
                else if(iCurrentSelection == 2)
                {
                    ArrayAdapter areaadapter3 = new ArrayAdapter(MainActivity2.this, android.R.layout.simple_spinner_item,area5);
                    areaadapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaspinner3.setAdapter(areaadapter3);

                }
                else if(iCurrentSelection == 3)
                {
                    ArrayAdapter areaadapter3 = new ArrayAdapter(MainActivity2.this, android.R.layout.simple_spinner_item,area6);
                    areaadapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaspinner3.setAdapter(areaadapter3);

                }
                else if(iCurrentSelection == 4)
                {
                    ArrayAdapter areaadapter3 = new ArrayAdapter(MainActivity2.this, android.R.layout.simple_spinner_item,area7);
                    areaadapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaspinner3.setAdapter(areaadapter3);

                }

                if(initialize==true) {
                    areaspinner3.setSelection(intentx.getIntExtra("current_area3", 0));
                    initialize = false;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });



        System.out.println(intentx.getIntExtra("current_area3",0));

        final String[] search_spinner = {"최신순","추천순"};
        Spinner spinner1 = (Spinner) findViewById(R.id.trail_search1);
        Intent intent2 = getIntent();
        boolean search_bool = intent2.getBooleanExtra("search_bool",false);

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, search_spinner);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        items = new ArrayList<trailListViewItem>() ;
        items2 = new ArrayList<trailListViewItem>() ;
        items3= new ArrayList<trailListViewItem>() ;
        items4= new ArrayList<trailListViewItem>() ;

        if(search_bool==false)
    {
        spinner1.setSelection(0);
    }
        else{

            spinner1.setSelection(1);

        }
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int iCurrentSelection = spinner1.getSelectedItemPosition();

                if(iCurrentSelection == 0){

                    if(search_bool==true) {
                        Intent intent4 = new Intent(MainActivity2.this, com.example.walkmap.MainActivity2.class);
                        intent4.putExtra("current_area1",areaspinner1.getSelectedItemPosition());
                        intent4.putExtra("current_area2",areaspinner2.getSelectedItemPosition());
                        intent4.putExtra("current_area3",areaspinner3.getSelectedItemPosition());
                        intent4.putExtra("initialize",true);
                        intent4.putExtra("searchtitle", false);
                        intent4.putExtra("search_bool", false);
                        intent4.putExtra("initialize_spinner",true);
                        initialize_spinner = true;

                        Trailsearch.callOnClick();
                    }
                }
                else if(iCurrentSelection == 1)
                {
                    mytrail_number = new ArrayList<String>();
                    if(search_bool==false) {
                        listview.smoothScrollToPosition( 0 );
                        for(int p=0;p<items.size();p++) {
                            trailListViewItem item_copy = items.get(p);

                            int item1 = Integer.parseInt(items.get(p).getPointtext());

                            int rule = 0;
                            int limit =0;
                            for (int j = 0; j < items2.size(); j++) {
                                int item2 = Integer.parseInt(items2.get(j).getPointtext());

                                if (item1 > item2) {
                                    limit = j;
                                    rule = 1;
                                    break;
                                }
                            }
                            if(rule == 0) {
                                items2.add(item_copy);

                            }
                            else if (rule == 1){
                                for(int u=limit;u<items2.size();u++) {

                                    items3.add(items2.get(u));
                                }

                                int u1 = items2.size();
                                int u2 = limit;
                                for(int u=limit;u<u1;u++) {

                                   items2.remove(u2);
                                }
                                items2.add(item_copy);
                                for(int u=0;u<items3.size();u++) {

                                    items2.add(items3.get(u));

                                }
                                items3= new ArrayList<trailListViewItem>();
                            }
                        }



                        for(int w=0;w<items2.size();w++) {
                            mytrail_number.add(items2.get(w).getMydbnum());

                            System.out.println(mytrail_number);
                        }
                        listview.smoothScrollToPosition( 0 );
                        Intent intent3 = new Intent(MainActivity2.this, com.example.walkmap.MainActivity2.class);
                        intent3.putExtra("current_area1",areaspinner1.getSelectedItemPosition());
                        intent3.putExtra("current_area2",areaspinner2.getSelectedItemPosition());
                        intent3.putExtra("current_area3",areaspinner3.getSelectedItemPosition());
                        intent3.putExtra("initialize",true);

                        intent3.putExtra("searchtitle", false);

                        intent3.putExtra("mytrail_number",mytrail_number);
                        intent3.putExtra("search_bool", true);
                        intent3.putExtra("myitems", items2);
                        initialize_spinner = true;

                        Trailsearch.callOnClick();

                    }


                }


            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        // 리스트뷰 참조 및 Adapter달기




        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("trails").addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {


                        try {
                            int i = 0;
                            for (DataSnapshot dataSnapshot : dataSnapshot1.getChildren()) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if ((snapshot.child("area1").getValue().toString().equals(areaspinner1.getSelectedItem().toString())) && (snapshot.child("area2").getValue().toString().equals(areaspinner2.getSelectedItem().toString())) && (snapshot.child("area3").getValue().toString().equals(areaspinner3.getSelectedItem().toString()))) {

                                    trailListViewItem item;
                                    // 아이템 생성.


                                    trail_number = snapshot.getKey().toString();


                                    item = new trailListViewItem();

                                    item.setText(dataSnapshot.getKey());

                                    item.setText1(snapshot.child("trailTitle").getValue().toString());
                                    item.setPointtext("0");
                                    item.setTextdate(snapshot.child("mydate").getValue().toString());
                                    item.setMydbnum(trail_number);
                                    items.add(item);


                                    listview = (ListView) findViewById(R.id.trailList);
                                    trailListViewBtnAdapter adapter;
                                    // Adapter 생성
                                    if (search_bool == false) {

                                        if (searchtitle == false) {

                                            adapter = new trailListViewBtnAdapter(MainActivity2.this, R.layout.traillist_item, items, trail_number, search_bool);
                                            listview.setAdapter(adapter);
                                        } else {
                                            mytrail_number3 = (ArrayList<String>) intentx.getSerializableExtra("mytrail_number2");

                                            for (int x = 0; x < mytrail_number3.size(); x++) {

                                                if (trail_number.equals(mytrail_number3.get(x).toString())) {

                                                    items4.add(item);
                                                    System.out.println(items4);
                                                    adapter = new trailListViewBtnAdapter(MainActivity2.this, R.layout.traillist_item, items4, trail_number, search_bool);
                                                    listview.setAdapter(adapter);
                                                    break;
                                                }
                                            }


                                        }

                                    } else {
                                        Intent intent = getIntent();
                                        ArrayList<trailListViewItem> myitems = (ArrayList<trailListViewItem>) intent.getSerializableExtra("myitems");
                                        ArrayList<String> mytrail_numbers = (ArrayList<String>) intent.getSerializableExtra("mytrail_number");

                                        items2 = myitems;
                                        String mytrailnum = mytrail_numbers.get(i);
                                        String myitemsid = items2.get(i).getText();

                                        if (searchtitle == false) {
                                            adapter = new trailListViewBtnAdapter(MainActivity2.this, R.layout.traillist_item, myitems, mytrailnum, search_bool);
                                            listview.setAdapter(adapter);
                                        } else {
                                            mytrail_number3 = (ArrayList<String>) intentx.getSerializableExtra("mytrail_number2");

                                            for (int x = 0; x < mytrail_number3.size(); x++) {

                                                if (mytrailnum.equals(mytrail_number3.get(x).toString())) {

                                                    System.out.println(myitemsid);
                                                    System.out.println(dataSnapshot.getKey());
                                                    item.setText(myitemsid);
                                                    item.setText1(dataSnapshot1.child(myitemsid).child(mytrailnum).child("trailTitle").getValue().toString());
                                                    item.setTextdate(dataSnapshot1.child(myitemsid).child(mytrailnum).child("mydate").getValue().toString());
                                                    item.setMydbnum(mytrailnum);
                                                    item.setPointtext("0");
                                                    items4.add(item);

                                                    adapter = new trailListViewBtnAdapter(MainActivity2.this, R.layout.traillist_item, items4, mytrailnum, search_bool);
                                                    listview.setAdapter(adapter);
                                                    break;
                                                }
                                            }
                                        }
                                        i++;
                                    }


                                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView parent, View v, int position, long id) {

                                        }
                                    });

                                }
                            }
                            }
                        }catch(Exception e){

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                    }
                });
        mytrail_number2 = new ArrayList<String>();



        Trailsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("trails").addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                for (DataSnapshot dataSnapshot : dataSnapshot1.getChildren()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if ((snapshot.child("area1").getValue().toString().equals(areaspinner1.getSelectedItem().toString())) && (snapshot.child("area2").getValue().toString().equals(areaspinner2.getSelectedItem().toString())) && (snapshot.child("area3").getValue().toString().equals(areaspinner3.getSelectedItem().toString()))) {
                                            if (!(title_text.getText().toString().equals("")) && (title_text.getText().toString() != null)) {
                                                if (snapshot.child("trailTitle").getValue().toString().indexOf(title_text.getText().toString()) != -1) {


                                                    mytrail_number2.add(snapshot.getKey().toString());

                                                }

                                            } else {

                                                mytrail_number2.add(snapshot.getKey().toString());
                                            }

                                        }


                                    }


                                    Intent intent = new Intent(MainActivity2.this, com.example.walkmap.MainActivity2.class);

                                    Intent intent5 = getIntent();
                                    ArrayList<trailListViewItem> myitems = (ArrayList<trailListViewItem>) intent5.getSerializableExtra("myitems");
                                    ArrayList<String> mytrail_numbers = (ArrayList<String>) intent5.getSerializableExtra("mytrail_number");
                                    intent.putExtra("initialize", true);
                                    intent.putExtra("initialize_spinner", false);
                                    intent.putExtra("current_area1", areaspinner1.getSelectedItemPosition());
                                    intent.putExtra("current_area2", areaspinner2.getSelectedItemPosition());
                                    intent.putExtra("current_area3", areaspinner3.getSelectedItemPosition());
                                    intent.putExtra("mytrail_number", mytrail_numbers);

                                    intent.putExtra("mytrail_number2", mytrail_number2);
                                    intent.putExtra("search_bool", false);
                                    intent.putExtra("searchtitle", true);
                                    intent.putExtra("myitems", myitems);

                                    if (search_bool == false && initialize_spinner == true) {
                                        intent.putExtra("searchtitle", true);
                                        intent.putExtra("mytrail_number", mytrail_number);
                                        intent.putExtra("search_bool", true);
                                        intent.putExtra("myitems", items2);

                                    }


                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message
                            }
                        }
                );







            }
        });




        Button myTrail = (Button) findViewById(R.id.myTrail);
        myTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, com.example.walkmap.trailCreate.class);
                startActivity(intent);

            }
        });
    }



    private ArrayList<String> trail_number1 = new ArrayList<String>();

    public class trailListViewBtnAdapter extends ArrayAdapter{
        // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.


        // 생성자로부터 전달된 resource id 값을 저장.
        int resourceId ;
        // 생성자로부터 전달된 ListBtnClickListener  저장.

        private boolean search_bool = false;
       private String trail_number;

        // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
        trailListViewBtnAdapter(Context context, int resource, ArrayList<trailListViewItem> list,String trail_number,boolean search_bool) {
            super(context, resource, list) ;

            // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
            this.resourceId = resource ;
            this.trail_number = trail_number;
            trail_number1.add(trail_number);
            this.search_bool = search_bool;

        }

        // 새롭게 만든 Layout을 위한 View를 생성하는 코드
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position ;
            final Context context = parent.getContext();

            // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
            }

            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            layoutParams.height = 300;
            convertView.setLayoutParams(layoutParams);
            fAuth = FirebaseAuth.getInstance();

            String username = fAuth.getCurrentUser().getEmail().replace('.', '!');

            // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
            final ImageView iconImageView = (ImageView) convertView.findViewById(R.id.trailImage);
            final TextView textTextView = (TextView) convertView.findViewById(R.id.trailuser_create);
            final TextView textTextView1 = (TextView) convertView.findViewById(R.id.trailTitle_create);
            final TextView pointtext = (TextView) convertView.findViewById(R.id.trailpoint_1);
            final TextView textdate = (TextView) convertView.findViewById(R.id.traildate);
            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            final trailListViewItem listViewItem = (trailListViewItem) getItem(position);

            // 아이템 내 각 위젯에 데이터 반영




            textTextView.setText(listViewItem.getText().replace("!","."));
            textTextView1.setText(listViewItem.getText1());
            pointtext.setText(listViewItem.getPointtext());
            textdate.setText(listViewItem.getTextdate());

            LinearLayout btnarea = (LinearLayout)convertView.findViewById(R.id.btnarea);
            btnarea.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                    Intent intent = new Intent(MainActivity2.this, com.example.walkmap.trail_view.class);
                    for(int i=0;i<trail_number1.size();i++) {
                        if ((pos) == (i))
                        {
                            intent.putExtra("trail_viewuser", listViewItem.getText());
                            intent.putExtra("trail_number", trail_number1.get(i));

                        }
                    }



                    startActivity(intent);
                }

            });
            ToggleButton pointtoggle = (ToggleButton) convertView.findViewById(R.id.togglepoint_btn);


            pointtoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {


                    Intent intent = new Intent(MainActivity2.this, com.example.walkmap.trail_view.class);

                    if (isChecked) {
                        if(search_bool==false) {
                            for (int i = 0; i < trail_number1.size(); i++) {

                                int b = i;
                                if ((pos) == (i)) {
                                    mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(i)).child("point").addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                int pointnum2 = 0;
                                                int checkpoint = 0;

                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        if (snapshot.getValue().equals(username)) {
                                                            checkpoint = 1;
                                                        }
                                                        pointnum2++;
                                                    }
                                                    if (checkpoint == 0) {
                                                        mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(pos)).child("point").child(username).setValue(username);

                                                        mDatabase.child("users").child(username).child("trail_recommend").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                          @Override
                                                                                                                                                          public void onDataChange(@NonNull DataSnapshot snapshotmy) {
                                                                                                                                                              int reconum =0;
                                                                                                                                                              for (DataSnapshot snapshotmy1 : snapshotmy.getChildren()){

                                                                                                                                                                  reconum ++;

                                                                                                                                                              }

                                                                                                                                                              writeNewUser_recommend(username,trail_number1.get(pos),Integer.toString(reconum),listViewItem.getText());
                                                                                                                                                          }

                                                                                                                                                          @Override
                                                                                                                                                          public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                          }
                                                                                                                                                      }


                                                        );
                                                        pointnum2++;
                                                        items.get(pos).setPointtext(Integer.toString(pointnum2));
                                                    }
                                                    pointtext.setText(Integer.toString(pointnum2));
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    // Getting Post failed, log a message
                                                }
                                            });

                                }
                            }
                        }else{
                            for (int i = 0; i < trail_number1.size(); i++) {

                                int b = i;
                                if ((pos) == (i)) {
                                    mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(i)).child("point").addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                int pointnum2 = 0;
                                                int checkpoint = 0;

                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        if (snapshot.getValue().equals(username)) {
                                                            checkpoint = 1;
                                                        }
                                                        pointnum2++;
                                                    }
                                                    if (checkpoint == 0) {
                                                        mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(pos)).child("point").child(username).setValue(username);

                                                        mDatabase.child("users").child(username).child("trail_recommend").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                 @Override
                                                                                                                                 public void onDataChange(@NonNull DataSnapshot snapshotmy) {
                                                                                                                                     int reconum =0;
                                                                                                                                     for (DataSnapshot snapshotmy1 : snapshotmy.getChildren()){

                                                                                                                                         reconum ++;

                                                                                                                                     }

                                                                                                                                     writeNewUser_recommend(username,trail_number1.get(pos),Integer.toString(reconum),listViewItem.getText());
                                                                                                                                 }

                                                                                                                                 @Override
                                                                                                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                 }
                                                                                                                             }


                                                        );




                                                        pointnum2++;
                                                        items2.get(pos).setPointtext(Integer.toString(pointnum2));
                                                    }
                                                    pointtext.setText(Integer.toString(pointnum2));
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    // Getting Post failed, log a message
                                                }
                                            });

                                }
                            }





                        }
                    }
                    else {
                        if (search_bool == false) {
                            for (int i = 0; i < trail_number1.size(); i++) {

                                int b = i;
                                if ((pos) == (i)) {

                                    mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(i)).child("point").addListenerForSingleValueEvent(
                                            new ValueEventListener() {

                                                int pointnum1 = 0;

                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {


                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                        pointnum1++;

                                                        try {
                                                            if (snapshot.getValue().toString().equals(username)) {
                                                                mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(pos)).child("point").child(username).setValue(null);
                                                                mDatabase.child("users").child(username).child("trail_recommend").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot recosnapshot) {

                                                                        for (DataSnapshot resnapshot : recosnapshot.getChildren()) {

                                                                           if((resnapshot.child("id").getValue().toString().equals(listViewItem.getText()))&&(resnapshot.child("trail_number").getValue().toString().equals(trail_number1.get(pos)))){

                                                                             
                                                                               mDatabase.child("users").child(username).child("trail_recommend").child(resnapshot.getKey()).child("id").setValue(null);
                                                                               mDatabase.child("users").child(username).child("trail_recommend").child(resnapshot.getKey()).child("trail_number").setValue(null);


                                                                           }
                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });

                                                                pointnum1--;
                                                                items.get(pos).setPointtext(Integer.toString(pointnum1));

                                                            }


                                                        } catch (Exception e) {

                                                            e.printStackTrace();
                                                        }
                                                    }


                                                    pointtext.setText(Integer.toString(pointnum1));

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    // Getting Post failed, log a message
                                                }
                                            });

                                }
                            }


                        }else{

                            for (int i = 0; i < trail_number1.size(); i++) {

                                int b = i;
                                if ((pos) == (i)) {

                                    mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(i)).child("point").addListenerForSingleValueEvent(
                                            new ValueEventListener() {

                                                int pointnum1 = 0;

                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {


                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                        pointnum1++;

                                                        try {
                                                            if (snapshot.getValue().toString().equals(username)) {
                                                                mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(pos)).child("point").child(username).setValue(null);
                                                                mDatabase.child("users").child(username).child("trail_recommend").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot recosnapshot) {

                                                                        for (DataSnapshot resnapshot : recosnapshot.getChildren()) {

                                                                            if( resnapshot.child("id").equals(listViewItem.getText())&&resnapshot.child("trail_number").equals(trail_number1.get(pos))){

                                                                                mDatabase.child("users").child(username).child(resnapshot.getKey()).child("id").setValue(null);
                                                                                mDatabase.child("users").child(username).child(resnapshot.getKey()).child("trail_number").setValue(null);

                                                                            }
                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });

                                                                pointnum1--;
                                                                items2.get(pos).setPointtext(Integer.toString(pointnum1));

                                                            }


                                                        } catch (Exception e) {

                                                            e.printStackTrace();
                                                        }
                                                    }


                                                    pointtext.setText(Integer.toString(pointnum1));

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    // Getting Post failed, log a message
                                                }
                                            });

                                }
                            }

                        }

                    }

                }


            });


                if(search_bool==false) {
                    for (int i = 0; i < trail_number1.size(); i++) {

                        if ((pos) == (i)) {
                            mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(i)).child("point").addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        int pointnum = 0;

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                pointnum++;
                                                try {
                                                    if (snapshot.getValue().toString().equals(username)) {
                                                        pointtoggle.setChecked(true);
                                                    }

                                                } catch (Exception e) {

                                                    e.printStackTrace();
                                                }
                                            }

                                            items.get(pos).setPointtext(Integer.toString(pointnum));
                                            pointtext.setText(Integer.toString(pointnum));

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Getting Post failed, log a message
                                        }
                                    });
                        }
                    }
                }else{

                    for (int i = 0; i < trail_number1.size(); i++) {

                        if ((pos) == (i)) {
                            mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(i)).child("point").addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        int pointnum = 0;
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                pointnum++;
                                                try {
                                                    if (snapshot.getValue().toString().equals(username)) {
                                                        pointtoggle.setChecked(true);
                                                    }

                                                } catch (Exception e) {

                                                    e.printStackTrace();
                                                }
                                            }

                                            items2.get(pos).setPointtext(Integer.toString(pointnum));
                                            pointtext.setText(Integer.toString(pointnum));

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Getting Post failed, log a message
                                        }
                                    });
                        }
                    }




                }





            return convertView;
        }

    }

    public class Users_trail_recommend {

        public String id;
        public String trail_number;

        public  Users_trail_recommend() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Users_trail_recommend(String id,String trail_number) {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)

            this.id = id;
            this.trail_number = trail_number;

        }
    }
    private void writeNewUser_recommend(String id, String trail_number,String trail_recommend1,String trailid) {
        MainActivity2.Users_trail_recommend user = new MainActivity2.Users_trail_recommend(trailid,trail_number);

        mDatabase.child("users").child(id).child("trail_recommend").child(trail_recommend1).setValue(user);
    }

}

