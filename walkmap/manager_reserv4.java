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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class manager_reserv4 extends AppCompatActivity {
    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mreserv4);
        fAuth = FirebaseAuth.getInstance();

        final String[] arealist = {"울산광역시","부산광역시","서울특별시"};
        Spinner spinner1 = (Spinner) findViewById(R.id.reserv_spinner);

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,arealist);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        String username = fAuth.getCurrentUser().getEmail().replace('.', '!');
        mDatabase = FirebaseDatabase.getInstance().getReference();


        Button next = (Button) findViewById(R.id.reserv_next4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manager_reserv4.this, com.example.walkmap.manager_reserv5.class);
                startActivity(intent);

            }
        });

        mDatabase.child("users").addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<managerListViewItem> items = new ArrayList<managerListViewItem>() ;



                        try {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {



                                    if(snapshot.child("usertype").getValue().toString().equals("1")) {
                                        managerListViewItem item;
                                        // 아이템 생성.


                                        String uid = snapshot.getKey();
                                        String uname = snapshot.child("name").getValue().toString();
                                        String uage = snapshot.child("age").getValue().toString();
                                        String ucareer = snapshot.child("career").getValue().toString();
                                        String usex = snapshot.child("sex").getValue().toString();

                                        item = new managerListViewItem();

                                        item.setTextid(uid);
                                        item.setTextname(uname);
                                        item.setTextage(uage);
                                        item.setTextcareer(ucareer);
                                        item.setTextsex(usex);


                                        items.add(item);

                                        ListView listview;
                                        manager_reserv4.trailListViewBtnAdapter adapter;
                                        // Adapter 생성
                                        adapter = new manager_reserv4.trailListViewBtnAdapter(manager_reserv4.this, R.layout.manager_item, items);

                                        // 리스트뷰 참조 및 Adapter달기
                                        listview = (ListView) findViewById(R.id.managerlist);
                                        listview.setAdapter(adapter);
                                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView parent, View v, int position, long id) {

                                            }
                                        });


                                    }
                            }}catch(Exception e){

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                    }
                });


    }


    private ArrayList<String> trail_number1 = new ArrayList<String>();
    private ArrayList<String> manager_check = new ArrayList<String>();

    public class trailListViewBtnAdapter extends ArrayAdapter {
        // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.


        // 생성자로부터 전달된 resource id 값을 저장.
        int resourceId ;



        // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
        trailListViewBtnAdapter(Context context, int resource, ArrayList<managerListViewItem> list) {
            super(context, resource, list) ;

            // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
            this.resourceId = resource ;


        }

        // 새롭게 만든 Layout을 위한 View를 생성하는 코드
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();
            fAuth = FirebaseAuth.getInstance();
            String username = fAuth.getCurrentUser().getEmail().replace('.', '!');
            // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
            }

            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            layoutParams.height = 300;
            convertView.setLayoutParams(layoutParams);

// Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            final managerListViewItem listViewItem = (managerListViewItem) getItem(position);
            // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
            final ImageView iconImageView = (ImageView) convertView.findViewById(R.id.manager_image);
            final TextView textTextView = (TextView) convertView.findViewById(R.id.manager_name);
            final TextView textTextView1 = (TextView) convertView.findViewById(R.id.manager_age);
            final TextView textTextView2 = (TextView) convertView.findViewById(R.id.manager_career);
            final TextView textTextView3 = (TextView) convertView.findViewById(R.id.manager_sex);



            final CheckBox managerchecked = (CheckBox) convertView.findViewById(R.id.manager_check);



            // 아이템 내 각 위젯에 데이터 반영
            iconImageView.setImageResource(R.drawable.ic_baseline_emoji_emotions_24);
            textTextView.setText(listViewItem.getTextname());
            textTextView1.setText(listViewItem.getTextage());
            textTextView2.setText(listViewItem.getTextcareer());
            textTextView3.setText(listViewItem.getTextsex());


            LinearLayout btnarea = (LinearLayout) convertView.findViewById(R.id.mangerlist_area);
            btnarea.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (managerchecked.isChecked()) {
                        btnarea.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
                        manager_check.remove(listViewItem.getTextid());
                        managerchecked.setChecked(false);
                    } else {
                        btnarea.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_default));
                        manager_check.add(listViewItem.getTextid());
                        managerchecked.setChecked(true);
                    }

                }

            });




            return convertView;
        }

    }


}
