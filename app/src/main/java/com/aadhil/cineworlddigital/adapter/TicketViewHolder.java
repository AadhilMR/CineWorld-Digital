package com.aadhil.cineworlddigital.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aadhil.cineworlddigital.R;

public class TicketViewHolder extends RecyclerView.ViewHolder {
    ImageView qrCode;
    TextView movieName;
    TextView seatNumber;
    TextView showDate;
    TextView showTime;
    TextView price;
    TextView issuedDateTime;
    TextView refNo;

    public TicketViewHolder(@NonNull View itemView) {
        super(itemView);
        this.qrCode = itemView.findViewById(R.id.imageView6);
        this.movieName = itemView.findViewById(R.id.textView102);
        this.seatNumber = itemView.findViewById(R.id.textView106);
        this.showDate = itemView.findViewById(R.id.textView107);
        this.showTime = itemView.findViewById(R.id.textView109);
        this.price = itemView.findViewById(R.id.textView111);
        this.issuedDateTime = itemView.findViewById(R.id.textView113);
        this.refNo = itemView.findViewById(R.id.textView115);
    }
}
