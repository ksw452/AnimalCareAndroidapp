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

public class trailCreate extends AppCompatActivity {
    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;
    //DB
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_create);
        fAuth = FirebaseAuth.getInstance();
        String username = fAuth.getCurrentUser().getEmail().replace('.', '!');
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("trails").child(username).addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<trailListViewItem> items = new ArrayList<trailListViewItem>() ;

                        try {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                trailListViewItem item ;
                                // 아이템 생성.


                                String delete1 = snapshot.getKey().toString();






                                item = new trailListViewItem() ;

                                item.setText(username) ;
                                item.setText1(snapshot.child("trailTitle").getValue().toString()) ;
                                int pointnum = 0;
                                for(DataSnapshot mysnapshot :snapshot.child("point").getChildren())
                                {

                                    pointnum++;
                                }
                                String mypoint = Integer.toString(pointnum);
                                item.setPointtext(mypoint);
                                item.setTextdate(snapshot.child("mydate").getValue().toString());
                                items.add(item) ;
                                System.out.println(snapshot.child("trailTitle").getValue().toString());

                                ListView listview ;
                                trailListViewBtnAdapter adapter;
                                // Adapter 생성
                                adapter = new trailListViewBtnAdapter(trailCreate.this, R.layout.traillist_item1, items,delete1) ;

                                // 리스트뷰 참조 및 Adapter달기
                                listview = (ListView) findViewById(R.id.myTrailList);
                                listview.setAdapter(adapter);
                                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                                    }
                                }) ;

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


        Button trail_Create = (Button) findViewById(R.id.trail_Create);
        trail_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(trailCreate.this, com.example.walkmap.trailCreate_new.class);
                startActivity(intent);

            }
        });

        Button recommend = (Button) findViewById(R.id.recommend);
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(trailCreate.this, com.example.walkmap.trail_recommend.class);
                startActivity(intent);

            }
        });


        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(trailCreate.this, com.example.walkmap.MainActivity2.class);
                startActivity(intent);
                finish();

            }
        });



    }


    private ArrayList<String> trail_number1 = new ArrayList<String>();

    public class trailListViewBtnAdapter extends ArrayAdapter {
        // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.

        String delete1;
        // 생성자로부터 전달된 resource id 값을 저장.
        int resourceId ;
        // 생성자로부터 전달된 ListBtnClickListener  저장.


        // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
        trailListViewBtnAdapter(Context context, int resource, ArrayList<trailListViewItem> list,String delete1) {
            super(context, resource, list) ;

            // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
            this.resourceId = resource ;
            this.delete1 = delete1;
            trail_number1.add(delete1);
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


            // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
            final ImageView iconImageView = (ImageView) convertView.findViewById(R.id.trailImage1);
            final TextView textTextView = (TextView) convertView.findViewById(R.id.trailuser_create1);
            final TextView textTextView1 = (TextView) convertView.findViewById(R.id.trailTitle_create1);
            final Button deletebtn = (Button) convertView.findViewById(R.id.delete_trail);
            final Button modifybtn = (Button) convertView.findViewById(R.id.trail_modify);
            final TextView textdate = (TextView) convertView.findViewById(R.id.textdate1);
            final TextView textpoint = (TextView) convertView.findViewById(R.id.trail_point);
            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener confirm = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("trails").child(username).child(trail_number1.get(pos)).setValue(null);

                            Intent dintent = new Intent(trailCreate.this, trailCreate.class);
                            startActivity(dintent);
                            finish();

                        }
                    };

                    DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    };
                    new AlertDialog.Builder(trailCreate.this)
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


                            Intent dintent = new Intent(trailCreate.this, trailCreate_new.class);
                            dintent.putExtra("trail_number", trail_number1.get(pos));
                            dintent.putExtra("modifymode", true);
                            startActivity(dintent);


                        }
                    };
                    new AlertDialog.Builder(trailCreate.this)
                            .setTitle("수정하시겠습니까?")
                            .setNegativeButton("수정", confirm)
                            .setPositiveButton("취소", cancel)
                            .show();


                }
            });


            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            final trailListViewItem listViewItem = (trailListViewItem) getItem(position);



            // 아이템 내 각 위젯에 데이터 반영

            textTextView.setText(listViewItem.getText().replace("!","."));
            textTextView1.setText(listViewItem.getText1());
            textdate.setText(listViewItem.getTextdate());
            textpoint.setText(listViewItem.getPointtext());

            LinearLayout btnarea = (LinearLayout) convertView.findViewById(R.id.btnarea1);
            btnarea.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(trailCreate.this, com.example.walkmap.trail_view.class);
                    for (int i = 0; i < trail_number1.size(); i++) {
                        if ((pos) == (i)) {
                            intent.putExtra("trail_viewuser", listViewItem.getText());
                            intent.putExtra("trail_number", trail_number1.get(i));

                        }
                    }
                    startActivity(intent);
                }

            });




                    return convertView;
                }

            }

        }
