package com.example.walkmap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class applyActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;

    EditText applyname;
    EditText applyage;
    EditText applycareer;
    EditText applybbb;
    EditText applyarea;
    Button applyButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        fAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("apply");

        applyname = findViewById(R.id.applyname);
        applyage = findViewById(R.id.applyage);
        applycareer = findViewById(R.id.applycareer);
        applybbb = findViewById(R.id.applybbb);
        applyarea = findViewById(R.id.applyarea);
        applyButton = findViewById(R.id.applyButton);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertapplyData();
            }
        });




    }
    private void insertapplyData(){

        String username = fAuth.getCurrentUser().getEmail().replace('.', '!');

        String name = applyname.getText().toString();
        String age = applyage.getText().toString();
        String career = applycareer.getText().toString();
        String bbb = applybbb.getText().toString();
        String area = applyarea.getText().toString();

        apply apply = new apply(name,age,career,bbb,area);
        mDatabase.child(username).setValue(apply);
        Toast.makeText(applyActivity.this,"매니저 지원 완료",Toast.LENGTH_LONG).show();



    }

    public class apply{
        String name;
        String age;
        String career;
        String bbb;
        String area;

        public apply(String name, String age, String career, String bbb, String area) {
            this.name = name;
            this.age = age;
            this.career = career;
            this.bbb = bbb;
            this.area = area;
        }

        public String getName() {
            return name;
        }

        public String getAge() {
            return age;
        }

        public String getCareer() {
            return career;
        }

        public String getBbb() {
            return bbb;
        }

        public String getArea() {
            return area;
        }
    }
}