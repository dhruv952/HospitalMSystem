package com.example.hospitalmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class OpdPatientHomeActivity extends AppCompatActivity {
    private ImageButton imgbtn;
    private TextView txtbookapt,txtviewapt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opd_patient_home);
        imgbtn=findViewById(R.id.Img_OpdBack);
        txtbookapt=findViewById(R.id.txt_opd_bookApt);
        txtviewapt=findViewById(R.id.txt_opd_viewApt);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        txtbookapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),OpdPatientDoctorListActivity.class);
            }
        });
    }
}