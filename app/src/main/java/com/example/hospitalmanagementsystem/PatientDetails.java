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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

public class PatientDetails extends AppCompatActivity {
    RecyclerView.LayoutManager lm_treatment, lm_bills;
    int bill_clickable = 0, treat_clickable = 0;
    private TextView txtuid, txtname, txtdesc, txtMedicalHist, txtBill, txtTreatment;
    private LinearLayout linearLayout;
    private Button addTreatment, addBills,genbills;
    private ImageView imageView;
    private ImageButton imgback;
    private RecyclerView rv_treatment, rv_bills;
    private String pid, mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        pid = getIntent().getStringExtra("pid");
        mode = getIntent().getStringExtra("mode");

        txtuid = findViewById(R.id.PatientUID);
        txtdesc = findViewById(R.id.PatientDetDesc);
        imgback=findViewById(R.id.Img_recpPBack);
        txtMedicalHist = findViewById(R.id.PatientMedHist);
        addTreatment = findViewById(R.id.btn_add_treatment);
        addBills = findViewById(R.id.btn_add_bill);
        linearLayout=findViewById(R.id.LLPD2);
        genbills = findViewById(R.id.btn_gen_bill);
        txtname = findViewById(R.id.PatientDetName);
        imageView = findViewById(R.id.PatientDetImg);
        rv_treatment = findViewById(R.id.rv_PatientDetTreatment);
        rv_bills = findViewById(R.id.rv_PatientBill);

        lm_treatment = new LinearLayoutManager(this);
        lm_bills = new LinearLayoutManager(this);

        rv_bills.setLayoutManager(lm_bills);
        rv_treatment.setLayoutManager(lm_treatment);

        if (mode.equals("Doctor")) {
            addTreatment.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            addBills.setVisibility(View.GONE);
            genbills.setVisibility(View.GONE);
            treat_clickable = 1;
            bill_clickable = 0;

        } else if (mode.equals("recp")) {
            addBills.setVisibility(View.VISIBLE);
            addTreatment.setVisibility(View.GONE);
            genbills.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            treat_clickable = 0;
            bill_clickable = 1;

        } else {
            addBills.setVisibility(View.GONE);
            addTreatment.setVisibility(View.GONE);
            treat_clickable = 0;
            bill_clickable = 0;

        }

        genbills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GenerateBillActivity.class);
                i.putExtra("pid",pid);
                startActivity(i);


            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode.equals("Doctor")) {
                    Intent i = new Intent(getApplicationContext(), Doctor_homescreen.class);
                    startActivity(i);
                }
                else if(mode.equals("recp"))
                {
                    Intent i = new Intent(getApplicationContext(), PatientList.class);
                    startActivity(i);

                }
            }
        });
        addBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Bill_data_Insertion.class);
                i.putExtra("pid", pid);
                startActivity(i);

            }
        });
        addTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddTreatmentActivity.class);
                i.putExtra("pid", pid);
                startActivity(i);

            }
        });
        getProductDetails(pid);
        getTreatmentAndBills();


    }


    private void getProductDetails(String productid) {
        DatabaseReference mbase = FirebaseDatabase.getInstance().getReference().child("Patients");
        mbase.child(productid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    PatientList patient = snapshot.getValue(PatientList.class);
                    txtname.setText(patient.getPname());
                    txtdesc.setText(patient.getDescription());
                    txtuid.setText(patient.getUid());
                    txtMedicalHist.setText("Admitted on" + patient.getDate() + "at " + patient.getTime() + "Medical Condition of Patient as follows \n" + patient.getMedicalcondition());
                    Picasso.get().load(patient.getImage()).into(imageView);


                } else {
                    Toast.makeText(getApplicationContext(), "Patient Data Not Found", Toast.LENGTH_SHORT).show();
                    if (mode.equals("Doctor")) {
                        Intent i = new Intent(getApplicationContext(), Doctor_homescreen.class);
                        startActivity(i);

                    } else if (mode.equals("recp")) {
                        Intent i = new Intent(getApplicationContext(), RecptPatientList.class);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    private void getTreatmentAndBills() {
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();


        FirebaseRecyclerOptions<Treatment> options =
                new FirebaseRecyclerOptions.Builder<Treatment>()
                        .setQuery(RootRef.child("Treatment").child(pid)
                                , Treatment.class)
                        .build();


        FirebaseRecyclerAdapter<Treatment, TreatmentViewHolder> TreatmentAdapter =
                new FirebaseRecyclerAdapter<Treatment, TreatmentViewHolder>(options) {
                    @NonNull
                    @Override
                    public TreatmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.treatment_layout, parent, false);


                        return new TreatmentViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull TreatmentViewHolder treatmentViewHolder, int i, @NonNull Treatment treatment) {
                        String tempDate = treatment.getDate() + "\n" + treatment.getTime();
                        treatmentViewHolder.DateTime.setText(tempDate);
                        treatmentViewHolder.Desc.setText(treatment.getName());
                        treatmentViewHolder.freq.setText(treatment.getFreq());


                        treatmentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mode.equals("Doctor")) {

                                    CharSequence[] Treatoptions = new CharSequence[]{
                                            "Remove",
                                            "Update"
                                    };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                    builder.setTitle("Options");
                                    builder.setItems(Treatoptions, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (i == 0) {

                                                RootRef.child("Treatment").child(pid)
                                                        .child(treatment.getTid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(getApplicationContext(), "Selected Treatment Removed Sucessfully", Toast.LENGTH_SHORT).show();


                                                            }
                                                        });

                                            }


                                        }
                                    });
                                    builder.show();

                                }


                            }
                        });
                    }
                };

        rv_treatment.setAdapter(TreatmentAdapter);
        TreatmentAdapter.startListening();


        FirebaseRecyclerOptions<Bills> Boptions =
                new FirebaseRecyclerOptions.Builder<Bills>()
                        .setQuery(RootRef.child("Bills").child(pid)
                                , Bills.class)
                        .build();
        FirebaseRecyclerAdapter<Bills, BillViewHolder> BillsAdapter =
                new FirebaseRecyclerAdapter<Bills, BillViewHolder>(Boptions) {
                    @NonNull
                    @Override
                    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_layout, parent, false);


                        return new BillViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull BillViewHolder billViewHolder, int i, @NonNull Bills bills) {

                        billViewHolder.txtBillDesc.setText(bills.getDesc());
                        billViewHolder.txtBillAmount.setText(bills.getAmount());

                        billViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mode.equals("recp")) {

                                    CharSequence[] Billoptions = new CharSequence[]{
                                            "Remove",
                                            "Update"
                                    };
                                    AlertDialog.Builder Bbuilder = new AlertDialog.Builder(getApplicationContext());
                                    Bbuilder.setTitle("Options");
                                    Bbuilder.setItems(Billoptions, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (i == 0) {

                                                RootRef.child("Bills").child(pid)
                                                        .child(bills.getBid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(getApplicationContext(), "Selected Bill Removed Sucessfully", Toast.LENGTH_SHORT).show();


                                                            }
                                                        });
                                            }


                                        }
                                    });
                                    Bbuilder.show();

                                }


                            }
                        });
                    }
                };


        rv_bills.setAdapter(BillsAdapter);
        BillsAdapter.startListening();


    }
}