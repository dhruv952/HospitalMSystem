package com.example.hospitalmanagementsystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class BillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView txtBillDesc,txtBillAmount;
    ItemClickListner rvBillItemClick;
    public BillViewHolder(@NonNull View itemView) {
        super(itemView);
        txtBillAmount=itemView.findViewById(R.id.txt_bill_amount);
        txtBillDesc=itemView.findViewById(R.id.txt_bil_desc);

    }

    @Override
    public void onClick(View view) {

        rvBillItemClick.onClick(view,getAdapterPosition(),false);
    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.rvBillItemClick = itemClickListner;
    }

}
