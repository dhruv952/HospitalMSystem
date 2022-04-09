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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecptPatientList extends AppCompatActivity {
    RecyclerView RV;
    RecyclerView.LayoutManager LM;
    ImageButton imagebutton;
    Button btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recpt_patient_list);

        RV = findViewById(R.id.Recp_rv_patient);
        RV.setHasFixedSize(true);
        imagebutton = findViewById(R.id.Img_recpPBack);
        btnAdd = findViewById(R.id.Recp_btn2);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddPatientActivity.class);
                startActivity(i);
            }
        });
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecptHomeActivity.class);
                startActivity(i);

            }
        });


        LM = new LinearLayoutManager(this);
        RV.setLayoutManager(LM);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference PatientListRef = FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<PatientList> options =
                new FirebaseRecyclerOptions.Builder<PatientList>()
                        .setQuery(PatientListRef.child("Patients")
                                , PatientList.class)
                        .build();
        FirebaseRecyclerAdapter<PatientList, PatientViewHolder> PatientAdapter =
                new FirebaseRecyclerAdapter<PatientList, PatientViewHolder>(options) {
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

                                CharSequence op[] = new CharSequence[]{
                                        "Open Details",
                                        "Add Bill",
                                        "Delete Data"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(RecptPatientList.this);
                                builder.setTitle("Options");
                                builder.setItems(op, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0)
                                        {
                                            Intent intent=new Intent(getApplicationContext(),PatientDetails.class);
                                            intent.putExtra("pid",PL.getPid());
                                            intent.putExtra("mode","recp");
                                            startActivity(intent);

                                        }
                                        else if(i==1)
                                        {
                                            Intent intent=new Intent(getApplicationContext(),Bill_data_Insertion.class);
                                            intent.putExtra("pid",PL.getPid());
                                            startActivity(intent);

                                        }
                                        else if (i == 2) {
                                            PatientListRef.child("Patient")
                                                    .child(PL.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(getApplicationContext(), "Patient Removed Sucessfully", Toast.LENGTH_SHORT).show();


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
        RV.setAdapter(PatientAdapter);
        PatientAdapter.startListening();

    }
}




