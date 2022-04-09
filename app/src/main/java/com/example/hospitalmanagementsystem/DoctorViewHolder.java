package com.example.hospitalmanagementsystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView DocName,DocQua,DocDID;
    private ItemClickListner itemClickListner;
    public DoctorViewHolder(@NonNull View itemView) {
        super(itemView);
        DocName=itemView.findViewById(R.id.txt_doc_name);
        DocQua=itemView.findViewById(R.id.txt_doc_qua);
        DocDID=itemView.findViewById(R.id.txt_doctor_did);

    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(),false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    }

