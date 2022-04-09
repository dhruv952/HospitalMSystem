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

public class Bill_data_Insertion extends AppCompatActivity {
    EditText edtDesc,edtprice;
    Button btnBillInsert;
    private String pid,saveCurrentDate,saveCurrentTime,Bkey;
    private ProgressDialog laodingbar;
    private DatabaseReference RootRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_data_insertion);

        pid=getIntent().getStringExtra("pid");


        edtprice=findViewById(R.id.Edt_bill_price);
        edtDesc=findViewById(R.id.Edt_bill_desc);
        btnBillInsert=findViewById(R.id.btn_bill_Insert);
        laodingbar = new ProgressDialog(this);
        btnBillInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateBill();
            }
        });

    }

    private void ValidateBill() {
        String Desc,Bprice;
        Desc=edtDesc.getText().toString();
        Bprice=edtprice.getText().toString();

        if (TextUtils.isEmpty(Desc))
        {
            Toast.makeText(this, "Bill Description or Expenditure name cannot be Empty...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Bprice))
        {
            Toast.makeText(this, "Please include Price of Above Expenditure", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AddBillToDatabase(Desc,Bprice);

        }
    }

    private void AddBillToDatabase(String desc, String bprice) {

        laodingbar.setTitle("Add New Treatment");
        laodingbar.setMessage("Dear Doctor:, please wait while we are adding the Treatment to Database.");
        laodingbar.setCanceledOnTouchOutside(false);
        laodingbar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        Bkey= saveCurrentDate+saveCurrentTime;


        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Bills").child(pid).child(Bkey).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("desc", desc);
                    userdataMap.put("amount",bprice );
                    userdataMap.put("bid",Bkey);
                    userdataMap.put("time",saveCurrentTime);
                    userdataMap.put("date",saveCurrentDate);



                    RootRef.child("Bills").child(pid).child(Bkey).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(), "Bill Inserted to Database Sucessfully", Toast.LENGTH_SHORT).show();
                                        laodingbar.dismiss();


                                        Intent intent = new Intent(getApplicationContext(), RecptPatientList.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        laodingbar.dismiss();
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