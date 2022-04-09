package com.example.hospitalmanagementsystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class TreatmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView DateTime,Desc,freq;
    ItemClickListner treatClickListner;

    public TreatmentViewHolder(@NonNull View itemView) {
        super(itemView);
        DateTime=itemView.findViewById(R.id.txt_treat_date);
        Desc=itemView.findViewById(R.id.txt_treat_desc);
        freq=itemView.findViewById(R.id.txt_Treat_freq);
    }
    @Override
    public void onClick(View view) {
        treatClickListner.onClick(view,getAdapterPosition(),false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.treatClickListner = itemClickListner;
    }
}
