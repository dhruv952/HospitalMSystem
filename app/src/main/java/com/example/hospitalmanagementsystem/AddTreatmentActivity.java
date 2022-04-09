package com.example.hospitalmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class AddTreatmentActivity extends AppCompatActivity {
    EditText edtTreatName,edtTreatFreq;
    Button btnAddTreatment;
    private String pid,saveCurrentDate,saveCurrentTime,treatmentKey;
    private DatabaseReference RootRef;
    ProgressDialog lodingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_treatment);
        Paper.init(this);

        pid=getIntent().getStringExtra("pid");
        edtTreatName=findViewById(R.id.edt_treatment_name);
        edtTreatFreq=findViewById(R.id.edt_treatment_frequency);
        btnAddTreatment=findViewById(R.id.btn_add_treatment);
        lodingBar=new ProgressDialog(this);

        btnAddTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });


    }

    private void validateData() {
        String Tname,Tfreq;
        Tname=edtTreatName.getText().toString();
        Tfreq=edtTreatFreq.getText().toString();

        if (TextUtils.isEmpty(Tname))
        {
            Toast.makeText(this, "Treatment Or Medicine Name Cannot be Empty...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Tfreq))
        {
            Toast.makeText(this, "Please add Frquency of Give Treatment...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AddTreatmentToDatabase(Tname,Tfreq);

        }
    }

    private void AddTreatmentToDatabase(String Tname,String Tfreq) {
        lodingBar.setTitle("Add New Treatment");
        lodingBar.setMessage("Dear Doctor:, please wait while we are adding the Treatment to Database.");
        lodingBar.setCanceledOnTouchOutside(false);
        lodingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        treatmentKey= saveCurrentDate+saveCurrentTime;


        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Treatment").child(pid).child(treatmentKey).exists()))
                {
                    HashMap<String, Object> TreatdataMap = new HashMap<>();
                    TreatdataMap.put("name", Tname);
                    TreatdataMap.put("frequency",Tfreq );
                    TreatdataMap.put("date", saveCurrentDate);
                    TreatdataMap.put("time",saveCurrentTime);
                    TreatdataMap.put("tid",treatmentKey);



                    RootRef.child("Treatment").child(pid).child(treatmentKey).updateChildren(TreatdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(), "Treatment Inserted to Database Sucessfully", Toast.LENGTH_SHORT).show();
                                        lodingBar.dismiss();


                                        Intent intent = new Intent(getApplicationContext(), PatientList.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        lodingBar.dismiss();
                                        Toast.makeText(getApplicationContext(), "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Referance Error", Toast.LENGTH_SHORT).show();


            }
        });


    }
}