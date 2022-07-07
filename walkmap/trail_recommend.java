package com.example.walkmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class trail_recommend extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ListView listview ;
    private String trail_number;
    private ArrayList<trailListViewItem> items;
    private String reid;
    FirebaseAuth fAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_recommend);
        listview = (ListView) findViewById(R.id.myrecommend_list);
        items = new ArrayList<trailListViewItem>() ;

        fAuth = FirebaseAuth.getInstance();
        String username = fAuth.getCurrentUser().getEmail().replace('.', '!');


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(username).child("trail_recommend").addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {



                                mDatabase.child("trails").child(snapshot.child("id").getValue().toString()).child(snapshot.child("trail_number").getValue().toString()).addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot4) {
                                                trailListViewItem item ;
                                                item = new trailListViewItem() ;
                                                reid = snapshot.child("id").getValue().toString();
                                      
                                                item.setText(reid) ;
                                                item.setPointtext("0");
                                                item.setText1( snapshot4.child("trailTitle").getValue().toString()) ;
                                                item.setTextdate(snapshot4.child("mydate").getValue().toString());
                                                trail_number = snapshot4.getKey();

                                                item.setMydbnum(trail_number);
                                                items.add(item);


                                                trail_recommend.trailListViewBtnAdapter adapter;
                                                adapter = new trail_recommend.trailListViewBtnAdapter(trail_recommend.this, R.layout.traillist_item, items, trail_number);
                                                listview.setAdapter(adapter);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        }
                                );







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


        Button back1 = (Button) findViewById(R.id.recommend_back);
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(trail_recommend.this, com.example.walkmap.trailCreate.class);
                startActivity(intent);
                finish();
            }
        });

    }




    private ArrayList<String> trail_number1 = new ArrayList<String>();

    public class trailListViewBtnAdapter extends ArrayAdapter {
        // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.


        // 생성자로부터 전달된 resource id 값을 저장.
        int resourceId ;
        // 생성자로부터 전달된 ListBtnClickListener  저장.


        private String trail_number;

        // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
        trailListViewBtnAdapter(Context context, int resource, ArrayList<trailListViewItem> list, String trail_number) {
            super(context, resource, list) ;

            // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
            this.resourceId = resource ;
            this.trail_number = trail_number;
            trail_number1.add(trail_number);


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



            // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
            final ImageView iconImageView = (ImageView) convertView.findViewById(R.id.trailImage);
            final TextView textTextView = (TextView) convertView.findViewById(R.id.trailuser_create);
            final TextView textTextView1 = (TextView) convertView.findViewById(R.id.trailTitle_create);
            final TextView pointtext = (TextView) convertView.findViewById(R.id.trailpoint_1);
            final TextView textdate = (TextView) convertView.findViewById(R.id.traildate);
            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            final trailListViewItem listViewItem = (trailListViewItem) getItem(position);

            // 아이템 내 각 위젯에 데이터 반영

            String username = fAuth.getCurrentUser().getEmail().replace('.', '!');
            System.out.println(listViewItem.getText().replace("!","."));
            textTextView.setText(listViewItem.getText().replace("!","."));
            textTextView1.setText(listViewItem.getText1());
            pointtext.setText(listViewItem.getPointtext());
            textdate.setText(listViewItem.getTextdate());

            LinearLayout btnarea = (LinearLayout)convertView.findViewById(R.id.btnarea);
            btnarea.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                    Intent intent = new Intent(trail_recommend.this, com.example.walkmap.trail_view.class);
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


                    Intent intent = new Intent(trail_recommend.this, com.example.walkmap.trail_view.class);

                    if (isChecked) {

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
                                                        if (snapshot.getValue().equals(username )) {
                                                            checkpoint = 1;
                                                        }
                                                        pointnum2++;
                                                    }
                                                    if (checkpoint == 0) {
                                                        mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(pos)).child("point").child(username ).setValue(username);
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

                    }
                    else {
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

                                                                mDatabase.child("users").child(username).child("trail_recommend").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot recosnapshot) {

                                                                        for (DataSnapshot resnapshot : recosnapshot.getChildren()) {

                                                                            if( (resnapshot.child("id").getValue().toString().equals(listViewItem.getText()))&&(resnapshot.child("trail_number").getValue().toString().equals(trail_number1.get(pos)))){
                                                                                System.out.println(resnapshot.child("trail_number").getValue().toString());
                                                                                System.out.println(trail_number1.get(pos));
                                                                                mDatabase.child("trails").child(listViewItem.getText()).child(trail_number1.get(pos)).child("point").child(username).setValue(null);
                                                                                mDatabase.child("users").child(username).child("trail_recommend").child(resnapshot.getKey()).setValue(null);


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

                    }

                }


            });



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
    private void writeNewUser_recommend(String id, String trail_number,String trailnum,String trailid) {
        trail_recommend.Users_trail_recommend user = new trail_recommend.Users_trail_recommend(trailid,trail_number);

        mDatabase.child("users").child(id).child("trail_recommend").child(trailnum).setValue(user);
    }
}
