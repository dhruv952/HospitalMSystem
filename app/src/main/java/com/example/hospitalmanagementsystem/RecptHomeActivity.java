package com.example.hospitalmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import io.paperdb.Paper;

public class RecptHomeActivity extends AppCompatActivity {
    private TextView PatientList, DoctorList,AppoinmentList;
    private ImageButton signout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recpt_home);
        Paper.init(this);                                                                                                                                                                                                                                                                                                                                                                                                                                                                   

        PatientList=findViewById(R.id.Text_Recp_pl);
        DoctorList=findViewById(R.id.Text_Recp_DL);
        AppoinmentList=findViewById(R.id.Text_Recp_Appoinments);
        signout=findViewById(R.id.Pt_imgbtn_signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    Paper.book().destroy();

                    Intent intent = new Intent(RecptHomeActivity.this, Welcome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();





                }

            }
        });


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

        AppoinmentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RecpAppoinmentActivity.class);
                startActivity(intent);

            }
        });


    }
}