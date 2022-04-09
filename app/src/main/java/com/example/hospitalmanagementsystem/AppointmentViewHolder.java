package com.example.hospitalmanagementsystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListner itemClickListner;
    public TextView txtPatientName,txtDoctorName,status;


    public AppointmentViewHolder(@NonNull View itemView) {
        super(itemView);
        txtPatientName=itemView.findViewById(R.id.txt_Patient_name);
        txtDoctorName=itemView.findViewById(R.id.txt_doctor_name);
        status=itemView.findViewById(R.id.txt_appointment_status);


    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(),false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;


    }
}
