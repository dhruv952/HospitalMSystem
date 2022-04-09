package com.example.hospitalmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Doctor_homescreen extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_homescreen);

        recyclerView = findViewById(R.id.rv_patient);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }
    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference PatientListRef= FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<PatientList> options =
                new FirebaseRecyclerOptions.Builder<PatientList>()
                        .setQuery(PatientListRef.child("Patients")
                                ,PatientList.class)
                        .build();
        FirebaseRecyclerAdapter<PatientList, PatientViewHolder> PatientAdapter=
                new FirebaseRecyclerAdapter<PatientList,PatientViewHolder>(options) {
                    @NonNull
                    @Override
                    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_layout, parent, false);


                        return new PatientViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull PatientViewHolder patientViewHolder, int i, @NonNull PatientList PL) {
                        patientViewHolder.txtPatientName.setText(PL.getPname());
                        patientViewHolder.txtPatientUID.setText(PL.getUid());




                        patientViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[]=new CharSequence[]{
                                        "Open Details",
                                        "Discharge"
                                };
                                AlertDialog.Builder builder= new AlertDialog.Builder(Doctor_homescreen.this);
                                builder.setTitle("Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i==0)
                                        {
                                            Intent intent = new Intent(getApplicationContext(), PatientDetails.class);
                                            intent.putExtra("pid",PL.getPid());
                                            intent.putExtra("mode","Doctor");
                                            startActivity(intent);
                                        }


                                    }
                                });
                                builder.show();





                            }
                        });
                    }
                };
        recyclerView.setAdapter(PatientAdapter);
        PatientAdapter.startListening();




    }

}