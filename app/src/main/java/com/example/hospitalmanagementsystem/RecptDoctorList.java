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

public class RecptDoctorList extends AppCompatActivity {
    private RecyclerView RVDoc;
    private RecyclerView.LayoutManager LMDoc;
    private ImageButton imageButton;
    private Button AddDocbtn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recpt_doctor_list);
        RVDoc=findViewById(R.id.Recp_rv_Doctor);
        AddDocbtn=findViewById(R.id.Doc_Recp_btn2);
        imageButton=findViewById(R.id.Img_recpDocBack);
        RVDoc.setHasFixedSize(true);


        LMDoc=new LinearLayoutManager(this);
        RVDoc.setLayoutManager(LMDoc);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),RecptHomeActivity.class);
                startActivity(intent);


            }
        });

        AddDocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),RecptAddDocActivity.class);
                startActivity(intent);
            }
        });




    }
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
                                        "Edit Details",
                                        "Delete Data"
                                };
                                AlertDialog.Builder builder= new AlertDialog.Builder(RecptDoctorList.this);
                                builder.setTitle("Options");
                                builder.setItems(op, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                         if(i==1)
                                        {
                                            ListRef.child("Doctors")
                                                    .child(doctorList.getUkey())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(getApplicationContext(),"Doctor Removed Sucessfully",Toast.LENGTH_SHORT).show();
                                                            Intent intent=new Intent(getApplicationContext(),RecptDoctorList.class);
                                                            startActivity(intent);

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
        RVDoc.setAdapter(DoctorAdapter);
        DoctorAdapter.startListening();




    }

}
