package com.example.hospitalmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class RecptHomeActivity extends AppCompatActivity {
    private TextView PatientList, DoctorList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recpt_home);

        PatientList=findViewById(R.id.Text_Recp_pl);
        DoctorList=findViewById(R.id.Text_Recp_DL);


        PatientList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RecptPatientList.class);
                startActivity(intent);



            }
        });

        DoctorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RecptDoctorList.class);
                startActivity(intent);


            }
        });


    }
}