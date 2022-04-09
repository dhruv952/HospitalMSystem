package com.example.hospitalmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class OpdPatientHomeActivity extends AppCompatActivity {
    private TextView txtbookapt,txtviewapt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opd_patient_home);

        Paper.init(this);
        txtbookapt=findViewById(R.id.txt_opd_bookApt);
        txtviewapt=findViewById(R.id.txt_opd_viewApt);


        txtbookapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),OpdPatientDoctorListActivity.class);
                startActivity(intent);
            }
        });

        final DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference().child("Appointment");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(Prevelent.currentOnlineUser.getPhone()).exists())
                {
                    Appointments appointmentsDataMap=snapshot.child(Prevelent.currentOnlineUser.getPhone()).getValue(Appointments.class);
                    txtviewapt.setText("Appointment With "+appointmentsDataMap.getDoctor()+" has been "+appointmentsDataMap.getStatus());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}