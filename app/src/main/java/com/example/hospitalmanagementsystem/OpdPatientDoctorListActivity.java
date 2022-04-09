package com.example.hospitalmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class OpdPatientDoctorListActivity extends AppCompatActivity {
    private ImageButton imageButton;
    private RecyclerView RVOPD;
    private RecyclerView.LayoutManager OPDLM;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opd_patient_doctor_list);

        Paper.init(this);
        LoadingBar=new ProgressDialog(this);
        RVOPD = findViewById(R.id.OPD_rv_Doctors);
        RVOPD.setHasFixedSize(true);
        imageButton = findViewById(R.id.OPDBack);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecptHomeActivity.class);
                startActivity(i);

            }
        });


        OPDLM = new LinearLayoutManager(this);
        RVOPD.setLayoutManager(OPDLM);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ListRef= FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<DoctorList> options =
                new FirebaseRecyclerOptions.Builder<DoctorList>()
                        .setQuery(ListRef.child("Doctors")
                                ,DoctorList.class)
                        .build();
        FirebaseRecyclerAdapter<DoctorList, DoctorViewHolder> DoctorAdapter=
                new FirebaseRecyclerAdapter<DoctorList,DoctorViewHolder>(options) {


                    @NonNull
                    @Override
                    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_list_layout, parent, false);
                        return new DoctorViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull DoctorViewHolder doctorViewHolder, int i, @NonNull DoctorList doctorList) {

                        doctorViewHolder.DocName.setText(doctorList.getDname());
                        doctorViewHolder.DocQua.setText(doctorList.getQualification());
                        doctorViewHolder.DocDID.setText(doctorList.getDid());

                        doctorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence op[]=new CharSequence[]{
                                        "Request Appointment",

                                };
                                AlertDialog.Builder builder= new AlertDialog.Builder(OpdPatientDoctorListActivity.this);
                                builder.setTitle("Options");
                                builder.setItems(op, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i==0)
                                        {
                                            LoadingBar.setTitle("Requesting Appointments");
                                            LoadingBar.setMessage("Please wait, while we are Requesting Your Appointment.");
                                            LoadingBar.setCanceledOnTouchOutside(false);
                                            LoadingBar.show();

                                            ListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                {
                                                    if (!(dataSnapshot.child("PendingAppointment").child(Prevelent.currentOnlineUser.getPhone()).exists()))
                                                    {
                                                        HashMap<String, Object> userdataMap = new HashMap<>();
                                                        userdataMap.put("phone", Prevelent.currentOnlineUser.getPhone());
                                                        userdataMap.put("name", Prevelent.currentOnlineUser.getName());
                                                        userdataMap.put("status", "Pending");
                                                        userdataMap.put("Doctor",doctorList.getUkey());



                                                        ListRef.child("Appointment").child(Prevelent.currentOnlineUser.getPhone()).updateChildren(userdataMap)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                    {
                                                                        if (task.isSuccessful())
                                                                        {
                                                                            Toast.makeText(getApplicationContext(), "Congratulations, your Appointment has been Requested .", Toast.LENGTH_SHORT).show();
                                                                            LoadingBar.dismiss();


                                                                            Intent intent = new Intent(getApplicationContext(), OpdPatientDoctorListActivity.class);
                                                                            startActivity(intent);
                                                                        }
                                                                        else
                                                                        {
                                                                            LoadingBar.dismiss();
                                                                            Toast.makeText(getApplicationContext(), "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(), "Appointment With This Number Already Exist Plz Check Appoinments", Toast.LENGTH_SHORT).show();
                                                        LoadingBar.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(getApplicationContext(), OpdPatientDoctorListActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Toast.makeText(getApplicationContext(), "Database Referance Error", Toast.LENGTH_SHORT).show();


                                                }
                                            });






                                        }





                                    }
                                });
                                builder.show();

                            }
                        });

                    }







                };
        RVOPD.setAdapter(DoctorAdapter);
        DoctorAdapter.startListening();
    }
}