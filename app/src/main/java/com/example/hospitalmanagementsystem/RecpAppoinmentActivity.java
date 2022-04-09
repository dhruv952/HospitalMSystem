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

public class RecpAppoinmentActivity extends AppCompatActivity {
    private ImageButton imageButton;
    private RecyclerView RvApt;
    private RecyclerView.LayoutManager LmApt;
    private ProgressDialog LoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recp_appoinment);


        imageButton = findViewById(R.id.Recp_Appointment_Img_Back);
        RvApt = findViewById(R.id.Recp_Appointment_RV);
        RvApt.setHasFixedSize(true);
        LoadingBar = new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecptHomeActivity.class);
                startActivity(i);

            }
        });


        LmApt = new LinearLayoutManager(this);
        RvApt.setLayoutManager(LmApt);


    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ListRef = FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<Appointments> options =
                new FirebaseRecyclerOptions.Builder<Appointments>()
                        .setQuery(ListRef.child("Appointment")
                                , Appointments.class)
                        .build();
        FirebaseRecyclerAdapter<Appointments, AppointmentViewHolder> AppointmentAdapter =
                new FirebaseRecyclerAdapter<Appointments, AppointmentViewHolder>(options) {


                    @NonNull
                    @Override
                    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_list_layout, parent, false);
                        return new AppointmentViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull AppointmentViewHolder appointmentViewHolder, int i, @NonNull Appointments appointments) {

                        appointmentViewHolder.txtDoctorName.setText(appointments.getDoctor());
                        appointmentViewHolder.txtPatientName.setText(appointments.getName());
                        appointmentViewHolder.status.setText(appointments.getStatus());

                        appointmentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence op[] = new CharSequence[]{
                                        "Approve Appointment",
                                        "Reject Appointment"

                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(RecpAppoinmentActivity.this);
                                builder.setTitle("Options");
                                builder.setItems(op, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {
                                            LoadingBar.setTitle("Approving Appointments");
                                            LoadingBar.setMessage("Please wait, while we are Approving this Appointment.");
                                            LoadingBar.setCanceledOnTouchOutside(false);
                                            LoadingBar.show();

                                            ListRef.child("Appointment").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.child(appointments.getPhone()).exists()) {
                                                        HashMap<String, Object> userdataMap = new HashMap<>();
                                                        userdataMap.put("phone", appointments.getPhone());
                                                        userdataMap.put("name", appointments.getName());
                                                        userdataMap.put("status", "Approved");
                                                        userdataMap.put("doctor", appointments.getDoctor());


                                                        ListRef.child("Appointment")
                                                                .child(appointments.getPhone())
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        ListRef.child("Appointment").child(appointments.getPhone()).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Toast.makeText(getApplicationContext(), "Appointment of " + appointments.getName() + " for " + appointments.getDoctor() + "has Sucessfully Approved", Toast.LENGTH_LONG).show();
                                                                                LoadingBar.dismiss();

                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    LoadingBar.dismiss();

                                                }
                                            });

                                        } else if (i == 1) {
                                            LoadingBar.setTitle("Rejecting Appointment");
                                            LoadingBar.setMessage("Please wait, while we are rejecting this Apppoinment.");
                                            LoadingBar.setCanceledOnTouchOutside(false);
                                            LoadingBar.show();

                                            ListRef.child("Appointment").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.child(appointments.getPhone()).exists()) {
                                                        HashMap<String, Object> userdataMap = new HashMap<>();
                                                        userdataMap.put("phone", appointments.getPhone());
                                                        userdataMap.put("name", appointments.getName());
                                                        userdataMap.put("status", "Rejected");
                                                        userdataMap.put("doctor", appointments.getDoctor());


                                                        ListRef.child("Appointment")
                                                                .child(appointments.getPhone())
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        ListRef.child("Appointment").child(appointments.getPhone()).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Toast.makeText(getApplicationContext(), "Appointment of " + appointments.getName() + " for " + appointments.getDoctor() + "has been Rejected", Toast.LENGTH_LONG).show();
                                                                                LoadingBar.dismiss();
                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    LoadingBar.dismiss();

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
        RvApt.setAdapter(AppointmentAdapter);
        AppointmentAdapter.startListening();
    }
}