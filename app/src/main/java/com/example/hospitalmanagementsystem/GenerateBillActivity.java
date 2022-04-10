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
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class GenerateBillActivity extends AppCompatActivity {
    private ImageButton imgbutton;
    private String pid;
    private TextView FinalAmt;
    private Button GenBIll;
    RecyclerView rv_genbill;
    RecyclerView.LayoutManager lm_genbill;

    int TotalAmount = 0;
    private ProgressDialog LoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bill);
        pid = getIntent().getStringExtra("pid");

        LoadingBar=new ProgressDialog(this);
        imgbutton = findViewById(R.id.Img_recpGBBack);
        FinalAmt = findViewById(R.id.txtBill_FinalAmt);
        GenBIll = findViewById(R.id.btn_gen_bill);

        rv_genbill = findViewById(R.id.rv_PatientGenBill);
        lm_genbill = new LinearLayoutManager(this);
        rv_genbill.setHasFixedSize(true);
        rv_genbill.setLayoutManager(lm_genbill);

        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecptPatientList.class);
                startActivity(i);

            }
        });
        GenBIll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





            }

        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

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
                        TotalAmount += (Integer.valueOf(bills.getAmount()));
                        FinalAmt.setText("$" + String.valueOf(TotalAmount));






                    }
                };


        rv_genbill.setAdapter(BillsAdapter);
        BillsAdapter.startListening();



    }


   private void generateAndSendBills()
    {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Patients");
        LoadingBar.setTitle("Generating Bill");
        LoadingBar.setMessage("Please wait, while we are Generating Bill And Sending Message To Patient.");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pid).exists()) {
                    PatientList patientList = snapshot.child(pid).getValue(PatientList.class);



                    String msg = "Dear, " + patientList.getPname() + " \nYour Bill Amount is Due.\n Please Do Pay Bills To Proceed the Process Of Discharge";
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(String.valueOf(patientList.getUid()), null, msg, null, null);

                    Toast.makeText(getApplicationContext(), "Message Sent successfully! to " + patientList.getPname(), Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Patient Data NOt Exists",Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error :"+error,Toast.LENGTH_LONG).show();
                LoadingBar.dismiss();


            }
        });




    }
}