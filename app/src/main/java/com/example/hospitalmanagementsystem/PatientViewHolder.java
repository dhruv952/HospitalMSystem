package com.example.hospitalmanagementsystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ItemClickListner itemClickListner;
    public TextView txtPatientName,txtPatientUID;
    public PatientViewHolder(@NonNull View itemView) {
        super(itemView);
        txtPatientName=itemView.findViewById(R.id.txt_patient_name);
        txtPatientUID=itemView.findViewById(R.id.txt_patient_uid);
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(),false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}