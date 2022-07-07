package com.example.walkmap;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class manager_reserv3 extends AppCompatActivity {
    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mreserv3);
        fAuth = FirebaseAuth.getInstance();
        String username = fAuth.getCurrentUser().getEmail().replace('.', '!');
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("users").child(username).child("pets").child("멍멍").child("종류").setValue("강아지");


        mDatabase.child("users").child(username).addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<trailListViewItem> items = new ArrayList<trailListViewItem>() ;



                        try {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                if(snapshot.getKey().equals("pets")) {

                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        trailListViewItem item;
                                        // 아이템 생성.


                                        String petname = snapshot1.getKey();


                                        item = new trailListViewItem();

                                        item.setText(petname);




                                        items.add(item);

                                        ListView listview;
                                        manager_reserv3.trailListViewBtnAdapter adapter;
                                        // Adapter 생성
                                        adapter = new manager_reserv3.trailListViewBtnAdapter(manager_reserv3.this, R.layout.pet_item, items);

                                        // 리스트뷰 참조 및 Adapter달기
                                        listview = (ListView) findViewById(R.id.petlist);
                                        listview.setAdapter(adapter);
                                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView parent, View v, int position, long id) {

                                            }
                                        });

                                    }

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


        Button pet_Create = (Button) findViewById(R.id.pet_create_btn);
        pet_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manager_reserv3.this, com.example.walkmap.pet_create.class);
                startActivity(intent);

            }
        });

        Button next = (Button) findViewById(R.id.reserv_next2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manager_reserv3.this, com.example.walkmap.manager_reserv4.class);
                intent.putExtra("pet_list",pet_check);
                startActivity(intent);


            }
        });



    }


    private ArrayList<String> trail_number1 = new ArrayList<String>();
    private ArrayList<String> pet_check = new ArrayList<String>();

    public class trailListViewBtnAdapter extends ArrayAdapter {
        // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.


        // 생성자로부터 전달된 resource id 값을 저장.
        int resourceId ;



        // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
        trailListViewBtnAdapter(Context context, int resource, ArrayList<trailListViewItem> list) {
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
            final trailListViewItem listViewItem = (trailListViewItem) getItem(position);
            // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
            final ImageView iconImageView = (ImageView) convertView.findViewById(R.id.petimage1);
            final TextView textTextView = (TextView) convertView.findViewById(R.id.pet_name);
            final Button deletebtn = (Button) convertView.findViewById(R.id.delete_pet);
            final Button modifybtn = (Button) convertView.findViewById(R.id.pet_modify);

            iconImageView.setImageResource(R.drawable.dog);

            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener confirm = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("users").child(username).child(listViewItem.getText()).setValue(null);

                            Intent dintent = new Intent(manager_reserv3.this, manager_reserv3.class);
                            startActivity(dintent);

                        }
                    };

                    DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    };
                    new AlertDialog.Builder(manager_reserv3.this)
                            .setTitle("삭제하시겠습니까?")
                            .setPositiveButton("취소", cancel)
                            .setNegativeButton("삭제", confirm)
                            .show();
                }
            });

            modifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    };
                    DialogInterface.OnClickListener confirm = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            Intent dintent = new Intent(manager_reserv3.this, trailCreate_new.class);
                            dintent.putExtra("pet_name", listViewItem.getText());
                            dintent.putExtra("modifymode", true);
                            startActivityForResult(dintent,0);


                        }
                    };
                    new AlertDialog.Builder(manager_reserv3.this)
                            .setTitle("수정하시겠습니까?")
                            .setNegativeButton("수정", confirm)
                            .setPositiveButton("취소", cancel)
                            .show();


                }
            });

            final CheckBox petchecked = (CheckBox) convertView.findViewById(R.id.pet_check);







            // 아이템 내 각 위젯에 데이터 반영

            textTextView.setText(listViewItem.getText());


            LinearLayout btnarea = (LinearLayout) convertView.findViewById(R.id.btnarea2);
            btnarea.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
            System.out.println(pet_check);
                    if (petchecked.isChecked()) {
                        btnarea.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
                        pet_check.remove(listViewItem.getText());
                       petchecked.setChecked(false);
                    } else {
                        btnarea.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_default));
                        pet_check.add(listViewItem.getText());
                        petchecked.setChecked(true);
                    }


                }

            });




            return convertView;
        }

    }




}
